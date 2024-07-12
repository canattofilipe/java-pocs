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

    @Test
    void testThreadLocalWithInitialValue() {

        ThreadLocal<MyObject> threadLocal1 = new ThreadLocal<>() {
            @Override
            protected MyObject initialValue() {
                return new MyObject();
            }
        };

        ThreadLocal<MyObject> threadLocal2 = ThreadLocal.withInitial(MyObject::new);


        Thread t1 = new Thread(() -> {
            System.out.println("Thread 1: " + threadLocal1.get());
            System.out.println("Thread 1: " + threadLocal2.get());
        });

        Thread t2 = new Thread(() -> {
            System.out.println("Thread 2: " + threadLocal1.get());
            System.out.println("Thread 2: " + threadLocal2.get());
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

    class MyObject {

    }
}
