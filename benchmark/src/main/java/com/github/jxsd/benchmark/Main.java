package com.github.jxsd.benchmark;

import com.github.jxsd.benchmark.dom.DOMRead;
import com.github.jxsd.benchmark.jxsd.JXSDRead;
import com.github.jxsd.benchmark.simple.SimpleXMLRead;

public class Main {
    public static void main(String[] args) {
        Benchmark benchmark = new Benchmark();
        Benchmark.Result[] results = benchmark.run(new BenchmarkCase[]{
                new JXSDRead(),
                new DOMRead(),
                new SimpleXMLRead(),
        }, 10);
        System.out.println(benchmark.generateMDReport(results));
    }
}
