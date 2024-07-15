package com.example;

import org.junit.jupiter.api.Test;

public class ThreadLocalExampleTest {

  @Test
  void threadLocalTest() {

    ThreadLocal<String> threadLocal = new ThreadLocal<>();

    Thread thread1 =
        new Thread(
            () -> {
              threadLocal.set("Thread 1");
              try {
                Thread.sleep(2000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              final var value = threadLocal.get();
              System.out.println("Value: " + value);
            });

    Thread thread2 =
        new Thread(
            () -> {
              threadLocal.set("Thread 2");
              try {
                Thread.sleep(2000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              final var value = threadLocal.get();
              System.out.println("Value: " + value);
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
  void threadLocalRemovingValueTest() {

    ThreadLocal<String> threadLocal = new ThreadLocal<>();

    Thread thread1 =
        new Thread(
            () -> {
              threadLocal.set("Thread 1");
              var value = threadLocal.get();
              System.out.println("Thread 1 value: " + value);

              threadLocal.remove();
              value = threadLocal.get();
              System.out.println("Thread 1 value: " + value);
            });

    Thread thread2 =
        new Thread(
            () -> {
              threadLocal.set("Thread 2");
              var value = threadLocal.get();
              System.out.println("Thread 2 value: " + value);

              try {
                Thread.sleep(3000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }

              value = threadLocal.get();
              System.out.println("Thread 2 value: " + value);

              threadLocal.remove();
              value = threadLocal.get();
              System.out.println("Thread 2 value: " + value);
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
  void threadLocalWithInitialValueTest() {

    ThreadLocal<MyObject> threadLocal1 =
        new ThreadLocal<>() {
          @Override
          protected MyObject initialValue() {
            return new MyObject();
          }
        };

    ThreadLocal<MyObject> threadLocal2 = ThreadLocal.withInitial(MyObject::new);

    Thread thread1 =
        new Thread(
            () -> {
              System.out.println("Thread 1: " + threadLocal1.get());
              System.out.println("Thread 1: " + threadLocal2.get());
            });

    Thread thread2 =
        new Thread(
            () -> {
              System.out.println("Thread 2: " + threadLocal1.get());
              System.out.println("Thread 2: " + threadLocal2.get());
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
  void InheritedThreadLocalTest() {

    ThreadLocal<String> threadLocal = new ThreadLocal();
    InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

    Thread thread1 =
        new Thread(
            () -> {
              System.out.println("===== Thread 1 =====");
              threadLocal.set("Thread 1 - ThreadLocal");
              inheritableThreadLocal.set("Thread 1 - InheritableThreadLocal");

              System.out.println(threadLocal.get());
              System.out.println(inheritableThreadLocal.get());

              Thread childThread =
                  new Thread(
                      () -> {
                        System.out.println("===== Child thread =====");
                        System.out.println(threadLocal.get());
                        System.out.println(inheritableThreadLocal.get());
                      });
              childThread.start();
            });
    thread1.start();

    Thread thread2 =
        new Thread(
            () -> {
              try {
                Thread.sleep(3000);
              } catch (InterruptedException e) {
                e.printStackTrace();
                ;
              }

              System.out.println("===== Thread 2 =====");
              System.out.println(threadLocal.get());
              System.out.println(inheritableThreadLocal.get());
            });
    thread2.start();

    try {
      thread1.join();
      thread2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  class MyObject {}
}
