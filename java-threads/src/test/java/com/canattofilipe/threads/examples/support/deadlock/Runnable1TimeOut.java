package com.canattofilipe.threads.examples.support.deadlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class Runnable1TimeOut implements Runnable {

  private final Lock lock1;
  private final Lock lock2;

  public Runnable1TimeOut(Lock lock1, Lock lock2) {
    this.lock1 = lock1;
    this.lock2 = lock2;
  }

  @Override
  public void run() {

    final String threadName = Thread.currentThread().getName();

    int retries = 0;
    while (retries <= 100) {
      retries++;
      int failureCount = 0;
      while (!tryLockBothLocks()) {
        failureCount++;
        System.err.println(
            threadName
                + " failed to lock both Locks. "
                + "Waiting a bit before retrying ["
                + failureCount
                + " tries]");
        sleep(100L * ((long) Math.random()));
      }
      if (failureCount > 0) {
        System.out.println(
            threadName + " succeeded in locking both locks - after " + failureCount + " failures.");
      }
      // do the work - now that both locks have been acquired (locked by this thread)

      // unlock
      lock2.unlock();
      lock1.unlock();
    }
  }

  private void sleep(long timeMills) {
    try {
      Thread.sleep(timeMills);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private boolean tryLockBothLocks() {

    final String threadName = Thread.currentThread().getName();

    try {
      boolean lock1Acquired = lock1.tryLock(1000, TimeUnit.MILLISECONDS);
      if (!lock1Acquired) {
        return false;
      }
    } catch (InterruptedException e) {
      System.out.println(threadName + " interrupted trying to lock Lock 1");
      return false;
    }

    try {
      boolean lock2Acquired = lock2.tryLock(1000, TimeUnit.MILLISECONDS);
      if (!lock2Acquired) {
        lock1.unlock();
        return false;
      }
    } catch (InterruptedException e) {
      System.out.println(threadName + " interrupted trying to lock Lock 2");
      lock1.unlock();
      return false;
    }

    return true;
  }
}
