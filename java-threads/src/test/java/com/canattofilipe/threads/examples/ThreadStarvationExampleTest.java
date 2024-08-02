package com.canattofilipe.threads.examples;

import com.canattofilipe.threads.examples.support.deadlock.FairLock;
import com.canattofilipe.threads.examples.support.deadlock.Synchronizer;
import com.canattofilipe.threads.examples.support.deadlock.UnfairLock;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ThreadStarvationExampleTest {

  /**
   * This test creates an environment that is prone to thread starvation. It continuously spawns
   * threads that attempt to acquire a lock held by long-running tasks. The number of active and
   * completed threads is logged periodically to observe potential starvation. The test runs for a
   * finite number of iterations to avoid indefinite execution.
   */
  @Test
  void unfairLockTest() throws InterruptedException {

    Synchronizer synchronizer = new Synchronizer(new UnfairLock());
    List<Thread> tasks = new ArrayList<>();

    int it = 0;
    final int load = 5;

    while (it < 50) {
      it++;
      List<Thread> aTask = new ArrayList<>();
      for (int i = 0; i < load; i++) {
        Runnable task =
            () -> {
              try {
                synchronizer.doSynchronized();
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            };
        aTask.add(new Thread(task));
      }
      tasks.addAll(aTask);

      aTask.forEach(Thread::start);
      Thread.sleep(4000L);
      System.out.println(
          "quantity of alive threads: " + tasks.stream().filter(Thread::isAlive).count());
      System.out.println(
          "quantity of finished threads: " + tasks.stream().filter(t -> !t.isAlive()).count());
    }
  }

  @Test
  void fairLockTest() throws InterruptedException {

    Synchronizer synchronizer = new Synchronizer(new FairLock());
    List<Thread> tasks = new ArrayList<>();

    int it = 0;
    final int load = 5;

    while (it < 30) {
      it++;
      List<Thread> aTask = new ArrayList<>();
      for (int i = 0; i < load; i++) {
        Runnable task =
            () -> {
              try {
                synchronizer.doSynchronized();
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            };
        aTask.add(new Thread(task));
      }
      tasks.addAll(aTask);

      aTask.forEach(Thread::start);
      for (Thread thread : aTask) {
        thread.join();
      }
      Thread.sleep(4000L);
      System.out.println(
          "quantity of alive threads: " + tasks.stream().filter(Thread::isAlive).count());
      System.out.println(
          "quantity of finished threads: " + tasks.stream().filter(t -> !t.isAlive()).count());
    }
  }
}
