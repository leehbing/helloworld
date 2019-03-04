package com.metrics.test;

/**
 * http://ju.outofmemory.cn/entry/105544
 *
 *
 * Created by IntelliJ IDEA.
 * Author lihongbing on 8/4/2018.
 */

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.reporting.ConsoleReporter;
public class LearnCounter {
    private List<String> stringList = new LinkedList<String>();
    private Counter listSizeCounter = Metrics.newCounter(LearnCounter.class, "string-list-counter");
    private void push(String input){
        listSizeCounter.inc();
        stringList.add(input);
    }
    private void pop(String output){
        listSizeCounter.dec();
        stringList.remove(output);
    }
    public static void main(String[] args) throws InterruptedException{
        ConsoleReporter.enable(1, TimeUnit.SECONDS);
        LearnCounter learnCounter = new LearnCounter();
        for(int times = 0; times < 5; times++){
            learnCounter.push(String.valueOf(times));
            Thread.sleep(1000);
        }
        for(int times = 0; times < 5; times++){
            learnCounter.pop(String.valueOf(times));
            Thread.sleep(1000);
        }
    }
}
