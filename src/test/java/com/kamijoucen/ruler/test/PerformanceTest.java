package com.kamijoucen.ruler.test;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.module.RuleRunner;

@State(Scope.Benchmark)
@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
public class PerformanceTest {

    public static final RulerConfigurationImpl configuration;

    public static final RuleRunner addTestRunner;

    static {

        final String addScript = "var i = 0; while i < 100 { i = i + 1 + 2 + 3 + 4 + 5; } return i;";
        configuration = new RulerConfigurationImpl();
        addTestRunner = Ruler.compileScript(addScript, configuration);
    }

    @Benchmark
    public void testMethod() {
        addTestRunner.run();
    }
    
    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(PerformanceTest.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
        
    }
    
}
