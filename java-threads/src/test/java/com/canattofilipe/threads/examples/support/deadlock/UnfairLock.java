package com.canattofilipe.threads.examples.support.deadlock;

import com.canattofilipe.threads.examples.CustomLock;
import java.time.Instant;

public class UnfairLock implements CustomLock {

  private boolean isLocked = false;
  private Thread lockingThread = null;

  public synchronized void lock() throws InterruptedException {
    Instant startTime = Instant.now().now();
    while (isLocked) {
      System.out.println(Thread.currentThread().getName() + " waiting.");
      // start waiting and release the lock/sync block
      wait();
      System.out.println(Thread.currentThread().getName() + " awoken.");
    }
    isLocked = true;
    lockingThread = Thread.currentThread();
    System.out.println(
        Instant.now()
            + " - Lock acquired by "
            + lockingThread.getName()
            + " thread after waiting for "
            + (Instant.now().toEpochMilli() - startTime.toEpochMilli())
            + " ms.");
  }

  public synchronized void unlock() {
    if (Thread.currentThread() != lockingThread) {
      throw new IllegalMonitorStateException("Calling thread has not locked this lock");
    }
    isLocked = false;
    lockingThread = null;
    notify();
    System.out.println("Lock released by " + Thread.currentThread().getName() + " thread.");
  }
}
