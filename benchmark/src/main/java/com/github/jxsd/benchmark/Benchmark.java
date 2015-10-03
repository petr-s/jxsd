package com.github.jxsd.benchmark;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.getProperty;

public class Benchmark {
    Result[] run(BenchmarkCase[] cases, int count) {
        List<Result> results = new ArrayList<>();
        for (BenchmarkCase benchmarkCase : cases) {
            results.add(new Result(count, benchmarkCase.getDescription(), benchmarkCase.time(count)));
        }
        return results.toArray(new Result[]{});
    }

    String generateMDReport(Result[] results) {
        StringBuilder sb = new StringBuilder();
        sb.append("| Test name | Total time  | Time per single run |\n");
        sb.append("| --- | --- | --- |\n");
        for (Result result : results) {
            sb.append("|");
            sb.append(result.name);
            sb.append("|");
            sb.append(result.time + "ms");
            sb.append("|");
            sb.append(result.time / result.count + "ms");
            sb.append("|\n");
        }
        sb.append("```\nJava: ");
        sb.append(getProperty("java.version"));
        sb.append(" ");
        sb.append(getProperty("java.vendor"));
        sb.append("\nOS: ");
        sb.append(getProperty("os.name"));
        sb.append(" ");
        sb.append(getProperty("os.arch"));
        sb.append(" ");
        sb.append("\nCPU: ");
        sb.append(System.getenv("PROCESSOR_IDENTIFIER"));
        sb.append("\n```");
        return sb.toString();
    }

    public static class Result {
        String name;
        long time;
        int count;

        public Result(int count, String name, long time) {
            this.count = count;
            this.name = name;
            this.time = time;
        }
    }
}
