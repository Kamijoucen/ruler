package com.kamijoucen.ruler.test;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AtomicArchitectureDependencyTest {

    @Test
    public void logicDoesNotImportArchitectureLayers() throws IOException {
        Pattern forbidden = Pattern.compile(
                "^\\s*import\\s+(static\\s+)?com\\.kamijoucen\\.ruler\\.(application|component|service)\\.");

        List<String> violations = findImportViolations(
                Paths.get("src/main/java/com/kamijoucen/ruler/logic"),
                forbidden);

        Assert.assertTrue(String.join(System.lineSeparator(), violations), violations.isEmpty());
    }

    @Test
    public void componentDoesNotImportService() throws IOException {
        Pattern forbidden = Pattern.compile(
                "^\\s*import\\s+(static\\s+)?com\\.kamijoucen\\.ruler\\.service\\.");

        List<String> violations = findImportViolations(
                Paths.get("src/main/java/com/kamijoucen/ruler/component"),
                forbidden);

        Assert.assertTrue(String.join(System.lineSeparator(), violations), violations.isEmpty());
    }

    private List<String> findImportViolations(Path root, Pattern forbidden) throws IOException {
        List<String> violations = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(root)) {
            paths.filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> collectViolations(path, forbidden, violations));
        }
        return violations;
    }

    private void collectViolations(Path path, Pattern forbidden, List<String> violations) {
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (int i = 0; i < lines.size(); i++) {
                if (forbidden.matcher(lines.get(i)).find()) {
                    violations.add(path + ":" + (i + 1) + " " + lines.get(i).trim());
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("failed to read " + path, e);
        }
    }
}
