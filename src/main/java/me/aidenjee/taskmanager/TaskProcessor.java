package me.aidenjee.taskmanager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class TaskProcessor {
    private final ExecutorService taskProcessor = Executors.newFixedThreadPool(3);
    private final TaskCounter counter = new TaskCounter();
    private static final int COUNTER_TASKS = 100_000;

    public void submitTask(Task task) {
        taskProcessor.submit(() -> {
            long id = Thread.currentThread().threadId();
            LockSupport.parkNanos(2_000_000_000);
            IO.println(id + " " + task.getTitle());
        });
    }

    public boolean closeProcessor() {
        try {
            taskProcessor.shutdown();
            boolean terminated = taskProcessor.awaitTermination(10, TimeUnit.SECONDS);
            return terminated;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public void submitCounterTasks() {
        for (int i = 0; i < COUNTER_TASKS; i++) {
            taskProcessor.execute(() -> counter.increment());
        }
    }

    public static void main(String[] args) {
        System.out.println("MAIN STARTED");
        TaskProcessor processor = new TaskProcessor();
        processor.submitCounterTasks();
        processor.closeProcessor();
        System.out.println(processor.counter.getCount());
    }
}
