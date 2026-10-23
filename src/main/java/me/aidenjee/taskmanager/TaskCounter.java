package me.aidenjee.taskmanager;

import java.util.concurrent.atomic.AtomicInteger;


public class TaskCounter {
    private final AtomicInteger completedCount = new AtomicInteger(0);

    public void increment() {
        completedCount.getAndIncrement();
    }

    public int getCount() {
        return completedCount.get();
    }
}
  