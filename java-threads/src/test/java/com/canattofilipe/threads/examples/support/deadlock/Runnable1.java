package com.canattofilipe.threads.examples.support.deadlock;

import java.util.concurrent.locks.Lock;

public class Runnable1 implements Runnable {

  private final Lock lock1;
  private final Lock lock2;

  public Runnable1(final Lock lock1, final Lock lock2) {
    this.lock1 = lock1;
    this.lock2 = lock2;
  }

  @Override
  public void run() {
    final String threadName = Thread.currentThread().getName();

    System.out.println(threadName + " attempting to lock Lock 1");
    lock1.lock();
    System.out.println(threadName + " locked Lock 1");

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      // igonre
    }

    System.out.println(threadName + " attempting to lock Lock 2");
    lock2.lock();
    System.out.println(threadName + " locked Lock 2");

    // do the work - now that both locks have been acquired

    // unlock
    System.out.println(threadName + " unlocking Lock 1");
    lock1.unlock();

    System.out.println(threadName + " unlocking Lock 2");
    lock2.unlock();
  }
}
