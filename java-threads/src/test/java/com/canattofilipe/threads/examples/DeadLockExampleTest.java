package com.canattofilipe.threads.examples;

import com.canattofilipe.threads.examples.support.deadlock.Runnable1;
import com.canattofilipe.threads.examples.support.deadlock.Runnable2;
import com.canattofilipe.threads.examples.support.deadlock.RunnableSync1;
import com.canattofilipe.threads.examples.support.deadlock.RunnableSync2;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.jupiter.api.Test;

public class DeadLockExampleTest {

  @Test
  void explicitDeadLockTest() {

    System.out.println(Thread.currentThread().getName());

    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();

    Runnable runnable1 = new Runnable1(lock1, lock2);
    Runnable runnable2 = new Runnable2(lock1, lock2);

    Thread thread1 = new Thread(runnable1);
    Thread thread2 = new Thread(runnable2);

    thread1.start();
    thread2.start();

    try {
      thread1.join();
      thread2.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void syncDeadLockTest() {

    System.out.println(Thread.currentThread().getName());

    Object lock1 = new Object();
    Object lock2 = new Object();

    Runnable runnable1 = new RunnableSync1(lock1, lock2);
    Runnable runnable2 = new RunnableSync2(lock1, lock2);

    Thread thread1 = new Thread(runnable1);
    Thread thread2 = new Thread(runnable2);

    thread1.start();
    thread2.start();

    try {
      thread1.join();
      thread2.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
