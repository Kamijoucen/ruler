package com.kamijoucen.ruler.test;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Protect state ownership and the CLI boundary without separating tightly coupled engine logic. */
public class ArchitectureTest {

    private static final Path SOURCES = Paths.get("src/main/java/com/kamijoucen/ruler");

    @Test
    public void productionUsesTheCurrentPackages() throws IOException {
        Pattern oldPackage = Pattern.compile(
                "\\bcom\\.kamijoucen\\.ruler\\.(domain|component|application|service)(?:\\.|;)");
        assertNoSourceReferences(javaSources(SOURCES), oldPackage);
    }

    @Test
    public void engineDoesNotDependOnItsEntryPoints() throws IOException {
        Pattern entryPoint = Pattern.compile(
                "\\b(com\\.kamijoucen\\.ruler\\.(api|cli)|org\\.jline)\\.");
        List<Path> coreSources = javaSources(SOURCES.resolve("logic"));
        coreSources.addAll(javaSources(SOURCES.resolve("types")));
        assertNoSourceReferences(coreSources, entryPoint);
    }

    @Test
    public void logicDoesNotKeepMutableExecutionStateInFields() throws Exception {
        List<String> violations = new ArrayList<>();
        for (Path source : javaSources(SOURCES.resolve("logic"))) {
            String relativeName = SOURCES.relativize(source).toString().replace('/', '.').replace('\\', '.');
            String className = "com.kamijoucen.ruler."
                    + relativeName.substring(0, relativeName.length() - ".java".length());
            Class<?> type = Class.forName(className, false, getClass().getClassLoader());
            inspectFields(type, violations);
        }
        Assert.assertTrue(String.join(System.lineSeparator(), violations), violations.isEmpty());
    }

    private void inspectFields(Class<?> type, List<String> violations) {
        for (Field field : type.getDeclaredFields()) {
            if (field.isSynthetic()) {
                continue;
            }
            int modifiers = field.getModifiers();
            String name = type.getName() + "." + field.getName();
            if (!Modifier.isFinal(modifiers)) {
                violations.add(name + " can retain state between calls; pass execution state explicitly");
            } else if (!Modifier.isStatic(modifiers) && isExecutionState(field.getType())) {
                violations.add(name + " retains mutable state even though its reference is final");
            }
        }
        for (Class<?> nested : type.getDeclaredClasses()) {
            inspectFields(nested, violations);
        }
    }

    private boolean isExecutionState(Class<?> type) {
        // Fixed strategy values and final stateless delegates are allowed. Collections in
        // logic belong to local execution state or shared immutable static lookup tables.
        if (type.isArray() || Collection.class.isAssignableFrom(type)
                || Map.class.isAssignableFrom(type) || ThreadLocal.class.isAssignableFrom(type)) {
            return true;
        }
        String name = type.getName();
        return name.startsWith("com.kamijoucen.ruler.types.runtime.")
                || name.startsWith("com.kamijoucen.ruler.types.config.")
                || name.startsWith("com.kamijoucen.ruler.types.module.")
                || name.startsWith("com.kamijoucen.ruler.types.parser.")
                || name.startsWith("com.kamijoucen.ruler.types.lexer.");
    }

    private List<Path> javaSources(Path root) throws IOException {
        try (Stream<Path> paths = Files.walk(root)) {
            return paths.filter(path -> path.toString().endsWith(".java"))
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    private void assertNoSourceReferences(List<Path> sources, Pattern forbidden) throws IOException {
        List<String> violations = new ArrayList<>();
        for (Path path : sources) {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (int i = 0; i < lines.size(); i++) {
                if (forbidden.matcher(lines.get(i)).find()) {
                    violations.add(path + ":" + (i + 1) + " " + lines.get(i).trim());
                }
            }
        }
        Assert.assertTrue(String.join(System.lineSeparator(), violations), violations.isEmpty());
    }
}
