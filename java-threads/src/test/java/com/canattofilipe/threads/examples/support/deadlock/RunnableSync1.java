package com.canattofilipe.threads.examples.support.deadlock;

public class RunnableSync1 implements Runnable {

  private final Object lock1;
  private final Object lock2;

  public RunnableSync1(final Object lock1, final Object lock2) {
    this.lock1 = lock1;
    this.lock2 = lock2;
  }

  @Override
  public void run() {
    final String threadName = Thread.currentThread().getName();

    System.out.println(threadName + " attempting to lock Lock 1");
    synchronized (lock1) {
      System.out.println(threadName + " locked Lock 1");

      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        // igonre
      }

      System.out.println(threadName + " attempting to lock Lock 2");
      synchronized (lock2) {
        System.out.println(threadName + " locked Lock 2");

        // do the work - now that both locks have been acquired

      }
      System.out.println(threadName + " unlocking Lock 2");
    }
    System.out.println(threadName + " unlocking Lock 1");
  }
}
