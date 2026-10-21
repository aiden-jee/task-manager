package me.aidenjee.taskmanager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskCounter {
    private int completedCount = 0;

    public synchronized void increment() {
        completedCount++;
    }

    public int getCount() {
        return completedCount;
    }
}
  