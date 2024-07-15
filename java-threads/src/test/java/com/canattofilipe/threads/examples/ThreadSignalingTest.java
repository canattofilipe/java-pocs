package com.canattofilipe.threads.examples;

import org.junit.jupiter.api.Test;

public class ThreadSignalingTest {

  @Test
  void basicSignalingExample() {

    Object signalingObject = new Object();

    Thread waiterThread =
        new Thread(
            () -> {
              synchronized (signalingObject) {
                try {
                  signalingObject.wait();
                  System.out.println("waiterThread woke up!");
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            });

    Thread notifierThread =
        new Thread(
            () -> {
              synchronized (signalingObject) {
                signalingObject.notify();
                System.out.println("waiterThread notified!");
              }
            });

    waiterThread.start();
    notifierThread.start();

    try {
      waiterThread.join();
      notifierThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  void threadSignalingExampleTest() {

    SignalCarrier signalCarrier = new SignalCarrier();

    Thread waiterThread =
        new Thread(
            () -> {
              try {
                signalCarrier.doWait();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    Thread notifierThread =
        new Thread(
            () -> {
              try {
                signalCarrier.doNotify();
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            });

    waiterThread.start();
    notifierThread.start();

    try {
      waiterThread.join();
      notifierThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private class SignalCarrier {

    public void doWait() throws InterruptedException {
      synchronized (this) {
        System.out.println(Thread.currentThread().getName() + " calling wait()");
        this.wait();
        System.out.println(Thread.currentThread().getName() + " exited wait()");
      }
    }

    public void doNotify() throws InterruptedException {
      synchronized (this) {
        System.out.println(Thread.currentThread().getName() + " calling notify()");
        this.notify();
        System.out.println(Thread.currentThread().getName() + " exited notify()");
      }
    }

    public void doNotifyAll() throws InterruptedException {
      synchronized (this) {
        System.out.println(Thread.currentThread().getName() + " calling notifyAll()");
        this.notifyAll();
        System.out.println(Thread.currentThread().getName() + " exited notifyAll()");
      }
    }
  }
}
