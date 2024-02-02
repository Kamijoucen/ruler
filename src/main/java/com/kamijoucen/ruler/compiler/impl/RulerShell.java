package com.kamijoucen.ruler.compiler.impl;

import java.util.stream.Collectors;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.parameter.RuleResult;
import com.kamijoucen.ruler.parameter.RuleResultValue;

public class RulerShell {

    private final RulerConfiguration configuration;
    private int line = 0;

    public RulerShell(RulerConfiguration configuration) {
        this.configuration = configuration;
    }

    public void start() {
        System.out.println("Ruler shell start.");
        try {
            Terminal terminal = TerminalBuilder.builder().build();
            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();
            while (true) {
                try {
                    String code = lineReader.readLine("ruler[" + line + "]> ");
                    RuleResult result = Ruler.compileExpression(code, configuration).run();
                    printResult(result);
                    line++;
                } catch (UserInterruptException e) {
                    break;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printResult(RuleResult result) {
        if (result.isEmpty()) {
            System.out.println("Empty result.");
            return;
        }
        // 使用 stream api 通过, 逗号分隔
        System.out.println(result.getResult().stream().map(RuleResultValue::getValue)
                .map(Object::toString).collect(Collectors.joining(", ")));
    }


    public static void main(String[] args) {
        RulerShell shell = new RulerShell(new RulerConfigurationImpl());
        shell.start();
    }

}
