package com.github.jxsd.benchmark;

public class Main {
    public static void main(String[] args) {
        Benchmark benchmark = new Benchmark();
        Benchmark.Result[] results = benchmark.run(new BenchmarkCase[]{
                new JXSDRead(),
        }, 10);
        System.out.println(benchmark.generateMDReport(results));
    }
}
