public class Main {

    public static void main(String[] args) {

        // RunnerThread extends Thread, so the object itself is a Thread.
        // We can call start() directly on it.
        RunnerThread runner1 = new RunnerThread("Mario");

        // RunnerRunnable implements Runnable.
        // This object represents a TASK, not a thread
        RunnerRunnable runner2 = new RunnerRunnable("Luigi");

        // To execute a Runnable concurrently, we give the task
        // to a Thread object.
        Thread runner2Thread = new Thread(runner2);

        // start() creates a new execution path and then Java
        // automatically calls the run() method of runner1.
        runner1.start();
        
        // This Thread executes the run() method defined
        // inside the RunnerRunnable object.
        runner2Thread.start();
    }
}