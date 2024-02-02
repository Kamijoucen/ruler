package com.kamijoucen.ruler.module;

import java.util.stream.Collectors;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.parameter.RulerResult;
import com.kamijoucen.ruler.parameter.RuleResultValue;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.IOUtil;

public class ShellRunner {

    private final RulerConfiguration configuration;
    private final Scope runScope;
    private int line = 0;

    public ShellRunner(RulerConfiguration configuration) {
        this.configuration = configuration;
        this.runScope = new Scope("runtime root", true, configuration.getGlobalScope(), null);
    }

    public void run() {
        System.out.println("Ruler shell start.");
        try {
            Terminal terminal = TerminalBuilder.builder().build();
            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();
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
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("Ruler shell end.");
    }

    private RulerResult runScript(String code) {

        return Ruler.compileExpression(code, configuration).run();
    }

    private void printResult(RulerResult result) {
        if (result.isEmpty()) {
            System.out.println("Empty result.");
            return;
        }
        System.out.println("< " + result.getResult().stream()
                .map(RuleResultValue::toFormattedString).collect(Collectors.joining(", ")));
    }

    public static void main(String[] args) {
        ShellRunner shell = new ShellRunner(new RulerConfigurationImpl());
        shell.run();
    }

}
