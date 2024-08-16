package com.canattofilipe.threads.examples.support.lock;

import java.util.concurrent.locks.Lock;

public class Database {

  private final Lock lock;
  private int version = 0;
  private boolean forceBottleNeck = true;

  public Database(Lock lock) {
    this.lock = lock;
  }

  public boolean save(String thing) {
    lock.lock();
    try {
      System.out.println(
          "[thread on save]: "
              + Thread.currentThread().getName()
              + ", [version]:"
              + version
              + "[Thing]: "
              + thing);

      if (forceBottleNeck) {
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          forceBottleNeck = false;
        }
      }

      audit(thing);
      version++;
      return true;
    } finally {
      lock.unlock();
    }
  }

  public boolean audit(String thing) {
    lock.lock();
    try {
      System.out.println(
          "[thread on audit]: "
              + Thread.currentThread().getName()
              + ", [version]:"
              + version
              + "[Thing]: "
              + thing);
      return true;
    } finally {
      lock.unlock();
    }
  }
}
