package com.canattofilipe.threads.examples;

import org.junit.jupiter.api.Test;

public class FalseSharingTest {

  private class Counter1 {
    public volatile long count1 = 0;
    public volatile long count2 = 0;
  }

  @Test
  void falseSharingTest() {

    Counter1 counter1 = new Counter1();
    Counter1 counter2 = counter1;

    long iterations = 1_000_000_000;

    Thread thread1 =
        new Thread(
            () -> {
              long startTime = System.currentTimeMillis();
              for (int i = 0; i < iterations; i++) {
                counter1.count1++;
              }
              long endTime = System.currentTimeMillis();
              System.out.println("Thread 1 total time: " + (endTime - startTime) + "ms");
            });

    Thread thread2 =
        new Thread(
            () -> {
              long startTime = System.currentTimeMillis();
              for (int i = 0; i < iterations; i++) {
                counter2.count2++;
              }
              long endTime = System.currentTimeMillis();
              System.out.println("Thread 2 total time: " + (endTime - startTime) + "ms");
            });

    thread1.start();
    thread2.start();

    try {
      thread1.join();
      thread2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  void falseSharingMitigatedTest() {

    Counter1 counter1 = new Counter1();
    Counter1 counter2 = new Counter1();

    long iterations = 1_000_000_000;

    Thread thread1 =
        new Thread(
            () -> {
              long startTime = System.currentTimeMillis();
              for (int i = 0; i < iterations; i++) {
                counter1.count1++;
              }
              long endTime = System.currentTimeMillis();
              System.out.println("Thread 1 total time: " + (endTime - startTime) + "ms");
            });

    Thread thread2 =
        new Thread(
            () -> {
              long startTime = System.currentTimeMillis();
              for (int i = 0; i < iterations; i++) {
                counter2.count2++;
              }
              long endTime = System.currentTimeMillis();
              System.out.println("Thread 2 total time: " + (endTime - startTime) + "ms");
            });

    thread1.start();
    thread2.start();

    try {
      thread1.join();
      thread2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
