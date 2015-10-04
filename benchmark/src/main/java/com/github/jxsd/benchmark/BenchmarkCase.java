package com.github.jxsd.benchmark;

import static java.lang.System.currentTimeMillis;

public abstract class BenchmarkCase {
    public abstract String getDescription();

    public abstract void prepare() throws Exception;

    public abstract void run();

    long time(int n) {
        try {
            prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long t = currentTimeMillis();
        for (int i = 0; i < n; i++) {
            run();
        }
        return currentTimeMillis() - t;
    }

}
