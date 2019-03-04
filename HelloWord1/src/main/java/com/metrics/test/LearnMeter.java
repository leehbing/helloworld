package com.metrics.test;

/**
 * Created by IntelliJ IDEA.
 * Author lihongbing on 8/4/2018.
 */

import java.util.concurrent.TimeUnit;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.reporting.ConsoleReporter;
public class LearnMeter {
    private Meter meter = Metrics.newMeter(LearnMeter.class, "meter-event", "request", TimeUnit.SECONDS);
    public void handleRequest(){
        meter.mark();
    }
    public static void main(String[] args) throws InterruptedException{
        ConsoleReporter.enable(1, TimeUnit.SECONDS);
        LearnMeter learnMeter = new LearnMeter();
        for(int times = 0; times < 200; times++){
            learnMeter.handleRequest();
            Thread.sleep(100);
        }
    }
}
