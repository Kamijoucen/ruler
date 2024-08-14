package com.kamijoucen.ruler.module;

import com.kamijoucen.ruler.compiler.impl.RulerCompiler;
import com.kamijoucen.ruler.compiler.impl.RulerInterpreter;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.parameter.RuleResultValue;
import com.kamijoucen.ruler.parameter.RulerResult;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.DefaultScope;
import com.kamijoucen.ruler.util.IOUtil;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShellRunner {

    private static final Logger logger = LoggerFactory.getLogger(ShellRunner.class);

    private final RulerConfiguration configuration;
    private final RuntimeContext runtimeContext;
    private int line = 0;

    public ShellRunner(RulerConfiguration configuration) {
        this.configuration = configuration;
        this.runtimeContext = configuration.createDefaultRuntimeContext(null);
    }

    public void run() {
        System.out.println("Ruler shell start.");
        try {
            Terminal terminal = TerminalBuilder.builder().build();
            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal)
                    .option(LineReader.Option.BRACKETED_PASTE, false)
                    .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true).build();
            while (true) {
                try {
                    String code = lineReader.readLine("ruler[" + line + "]> ");
                    if (IOUtil.isBlank(code)) {
                        continue;
                    }
                    RulerResult result = runScript(code);
                    printResult(result);
                    line++;
                } catch (UserInterruptException | EndOfFileException e) {
                    break;
                } catch (Exception e) {
                    logger.error("Error occurred while running shell.", e);
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Error occurred while running shell.", e);
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.println("Ruler shell end.");
    }

    private RulerResult runScript(String code) {

        RulerCompiler compiler = new RulerCompiler(new RulerScript("shell", code), configuration);
        RulerModule module = compiler.compileStatement();

        RulerInterpreter interpreter = new RulerInterpreter(module, configuration);
        List<Object> values = interpreter.runStatement(this.runtimeContext);

        List<RuleResultValue> ruleResultValues = new ArrayList<>(values.size());
        for (Object value : values) {
            ruleResultValues.add(new RuleResultValue(value));
        }
        return new RulerResult(ruleResultValues);
    }

    private void printResult(RulerResult result) {
        if (result.isEmpty()) {
            System.out.println("Empty result.");
            return;
        }
        System.out.println("< " + result.getResult().stream()
                .map(RuleResultValue::toFormattedString).collect(Collectors.joining(", ")));
    }

}
