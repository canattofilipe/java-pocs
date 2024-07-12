package com.example;

import org.junit.jupiter.api.Test;

public class ThreadLocalExampleTest {

    @Test
    void testThreadLocal() {

        ThreadLocal<String> threadLocal = new ThreadLocal<>();

        Thread t1 = new Thread(() -> {
            threadLocal.set("Thread 1");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final var value = threadLocal.get();
            System.out.println("Value: " + value);
        });

        Thread t2 = new Thread(() -> {
            threadLocal.set("Thread 2");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final var value = threadLocal.get();
            System.out.println("Value: " + value);
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Test
    void testThreadLocalRemovingValue() {

        ThreadLocal<String> threadLocal = new ThreadLocal<>();

        Thread t1 = new Thread(() -> {

            threadLocal.set("Thread 1");
            var value = threadLocal.get();
            System.out.println("Thread 1 value: " + value);

            threadLocal.remove();
            value = threadLocal.get();
            System.out.println("Thread 1 value: " + value);
        });

        Thread t2 = new Thread(() -> {

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
