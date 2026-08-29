/*
* Unlike RunnerThread, this class doesn't extend Thread,
* its extends Person, so it can reuse the properties and behavior
* defined in person, such as the name.
*
* At the same time implements interface Runnable, meaning that
* objects of this class represents a task that can be executed by a Thread.
* */

public class RunnerRunnable extends Person implements Runnable {

    public RunnerRunnable(String name) {
        super(name);
    }

    // Runnable requires us to implement the run() method.
    //
    // This method defines what the task does, but it doesn't
    // create or start a new thread by itself.
    @Override
    public void run() {
        for (int distance = 10; distance <= 50; distance += 10) {
            System.out.println(
                    getName() + " runs " + distance + " meters"
            );

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(getName() + " stopped");
                return;
            }
        }

        System.out.println(getName() + " finished the race");
    }
}