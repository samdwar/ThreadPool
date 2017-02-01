import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ThreadPool {
    private BlockingQueue<Task> taskBlockingQueue;
    private List<WorkerThread> workerThreadList;
    private final int numberOfThreads;

    public ThreadPool(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        taskBlockingQueue = new LinkedBlockingDeque<>();
        workerThreadList = new ArrayList<>(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            WorkerThread workerThread = new WorkerThread();
            workerThreadList.add(workerThread);
            workerThread.start();

        }
    }

    public void execute(Task task) {
        synchronized (taskBlockingQueue) {
            taskBlockingQueue.add(task);
            taskBlockingQueue.notify();
        }
    }

    public class WorkerThread extends Thread {
        @Override
        public void run() {
            super.run();
            Runnable task;
            while (true) {
                synchronized (taskBlockingQueue) {
                    while (taskBlockingQueue.isEmpty()) {
                        try {
                            taskBlockingQueue.wait();
                        } catch (InterruptedException e) {
                            System.out.println("An error occurred while queue is waiting: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    task = taskBlockingQueue.poll();
                    try {
                        task.run();
                    } catch (RuntimeException e) {
                        System.out.println("This thread pool is interrupted due to an issue: " + e.getMessage());
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}
