package com.canattofilipe.threads.examples.support.deadlock;

public class Synchronizer {

  private UnfairnessLock lock = new UnfairnessLock();

  public void doSynchronized() throws InterruptedException {

    System.out.println(Thread.currentThread().getName() + " locking the lock");
    lock.lock();

    // critical section, do a lot of work which takes a long time
    Thread.sleep(2000L);

    System.out.println(Thread.currentThread().getName() + " unlocking the lock");
    lock.unlock();
  }
}
