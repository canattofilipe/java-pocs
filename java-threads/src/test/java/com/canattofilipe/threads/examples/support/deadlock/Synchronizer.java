package com.canattofilipe.threads.examples.support.deadlock;

import com.canattofilipe.threads.examples.CustomLock;

public class Synchronizer {

  private final CustomLock lock;

  public Synchronizer(CustomLock lock) {
    this.lock = lock;
  }

  public void doSynchronized() throws InterruptedException {

    System.out.println(Thread.currentThread().getName() + " locking the lock");
    lock.lock();

    // critical section, do a lot of work which takes a long time
    Thread.sleep(1000L);

    System.out.println(Thread.currentThread().getName() + " unlocking the lock");
    lock.unlock();
  }
}
