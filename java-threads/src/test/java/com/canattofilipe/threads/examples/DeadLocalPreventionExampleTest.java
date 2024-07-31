package com.canattofilipe.threads.examples;

import com.canattofilipe.threads.examples.support.deadlock.Runnable1Timeout;
import com.canattofilipe.threads.examples.support.deadlock.Runnable2Timeout;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.jupiter.api.Test;

public class DeadLocalPreventionExampleTest {

  @Test
  void deadLockPreventionByTimeoutStrategyTest() {

    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();

    Thread t1 = new Thread(new Runnable1Timeout(lock1, lock2));
    Thread t2 = new Thread(new Runnable2Timeout(lock1, lock2));

    t1.start();
    t2.start();

    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
