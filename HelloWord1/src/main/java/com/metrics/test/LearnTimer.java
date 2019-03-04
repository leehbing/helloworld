package com.metrics.test;

/**
 * Created by IntelliJ IDEA.
 * Author lihongbing on 8/4/2018.
 */

import java.util.concurrent.TimeUnit;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;
import com.yammer.metrics.reporting.ConsoleReporter;
public class LearnTimer {
    private Timer timer = Metrics.newTimer(LearnTimer.class, "response-timer", TimeUnit.MILLISECONDS, TimeUnit.SECONDS);
    public void handleRequest() throws InterruptedException{
        TimerContext context = timer.time();
        for(int i = 0 ; i < 2 ; i++){
            Thread.sleep(1);
        }
        context.stop();
    }
    public static void main(String[] args) throws InterruptedException{
        ConsoleReporter.enable(1, TimeUnit.SECONDS);
        LearnTimer learnTimer = new LearnTimer();
        for(int time = 0 ; time < 10000 ; time++){
            learnTimer.handleRequest();
        }
        Thread.sleep(10000);
    }
}
