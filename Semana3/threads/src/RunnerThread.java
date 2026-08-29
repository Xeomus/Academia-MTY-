
/*
* Extends from Thread, which means that RunnerThread
* represents both the task to execute and the thread that
* executes it.
*
* A disadvantage oh this approach is that Java only supports
* a single inheritance. Since this class already extends from
* Thread, it cannot extend another class such a Person.
* */

public class RunnerThread extends Thread {

    private final String name;

    public RunnerThread(String name) {
        this.name = name;
    }

    /*
    * Contains the code that will be executed when the Thread starts
    * */
    @Override
    public void run() {
        for (int distance = 10; distance <= 50; distance += 10) {
            System.out.println(name + " runs " + distance + " meters");

            try {
                //pauses the current thread for one second,
                //others thread continue working
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(name + " stopped");
                return;
            }
        }

        System.out.println(name + " finished the race");
    }
}