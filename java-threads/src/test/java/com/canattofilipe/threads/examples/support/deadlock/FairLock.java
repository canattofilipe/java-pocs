package com.canattofilipe.threads.examples.support.deadlock;

import com.canattofilipe.threads.examples.CustomLock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FairLock implements CustomLock {
  private boolean isLocked = false;
  private Thread lockingThread = null;
  private List<QueueObject> waitingThreads = new ArrayList<>();

  public void lock() throws InterruptedException {
    Instant startTime = Instant.now().now();
    QueueObject queueObject = new QueueObject();
    boolean isLockedForThisThread = true;
    synchronized (this) {
      waitingThreads.add(queueObject);
    }

    while (isLockedForThisThread) {
      synchronized (this) {
        isLockedForThisThread = isLocked || waitingThreads.get(0) != queueObject;
        if (!isLockedForThisThread) {
          isLocked = true;
          waitingThreads.remove(queueObject);
          lockingThread = Thread.currentThread();

          System.out.println(
              Instant.now()
                  + " - Lock acquired by "
                  + Thread.currentThread().getName()
                  + " thread after waiting for "
                  + (Instant.now().toEpochMilli() - startTime.toEpochMilli())
                  + " ms.");

          return;
        }
      }
      try {
        queueObject.doWait();
      } catch (InterruptedException e) {
        synchronized (this) {
          waitingThreads.remove(queueObject);
        }
        throw e;
      }

      System.out.println(
          Instant.now()
              + " - Lock acquired by "
              + Thread.currentThread().getName()
              + " thread after waiting for "
              + (Instant.now().toEpochMilli() - startTime.toEpochMilli())
              + " ms.");
    }
  }

  public synchronized void unlock() {
    if (this.lockingThread != Thread.currentThread()) {
      throw new IllegalMonitorStateException("Calling thread has not locked this lock");
    }
    isLocked = false;
    lockingThread = null;
    if (waitingThreads.size() > 0) {
      waitingThreads.get(0).doNotify();
    }
  }
}
