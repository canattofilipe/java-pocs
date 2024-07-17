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

  @Test
  void missedSignalingExampleTest() {

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

    // force notify to start before wait
    notifierThread.start();
    waiterThread.start();

    try {
      waiterThread.join();
      notifierThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  void missedSignalingMitigatedExampleTest() {

    SignalCarrier signalCarrier = new SignalHolder();

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

    // force notify to start before wait
    notifierThread.start();
    waiterThread.start();

    try {
      waiterThread.join();
      notifierThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  void missedSignalingMitigatedWithCounterExampleTest() {

    SignalCarrier signalCarrier = new SignalCounter();

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

    // force notify to start before wait
    notifierThread.start();
    waiterThread.start();

    try {
      waiterThread.join();
      notifierThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  void notifyManyExampleTest() throws InterruptedException {

    SignalCarrier signalCarrier = new SignalCarrier();

    Thread waiterThread1 =
        new Thread(
            () -> {
              try {
                signalCarrier.doWait();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    Thread waiterThread2 =
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
                signalCarrier.doNotifyAll();
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            });

    waiterThread1.start();
    waiterThread2.start();
    // force wait to start before notify
    Thread.sleep(9000);
    notifierThread.start();

    try {
      waiterThread1.join();
      waiterThread2.join();
      notifierThread.join();

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  void spuriousSignalingExample() throws InterruptedException {

    System.out.println("Parent thread name: " + Thread.currentThread().getName());

    SpuriousWakeUpHandler signalingObject = new SpuriousWakeUpHandler();

    Thread waiterThread =
        new Thread(
            () -> {
              try {
                signalingObject.doWait();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    Thread notifierThread = new Thread(() -> signalingObject.doNotify());

    waiterThread.start();
    Thread.sleep(1000);
    notifierThread.start();

    try {
      waiterThread.join();
      notifierThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static class SignalCarrier {

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

  private static class SignalHolder extends SignalCarrier {
    private boolean signalRaised = false;
    private boolean isThreadWaiting = false;

    @Override
    public void doNotify() {
      synchronized (this) {
        System.out.println(Thread.currentThread().getName() + " calling notify()");
        if (!this.isThreadWaiting) {
          signalRaised = true;
        }
        this.notify();
        System.out.println(Thread.currentThread().getName() + " exited notify()");
      }
    }

    @Override
    public void doWait() throws InterruptedException {
      synchronized (this) {
        if (this.signalRaised) {
          System.out.println(Thread.currentThread().getName() + " signal already raised");
          return;
        }
        System.out.println(Thread.currentThread().getName() + " calling wait()");
        this.isThreadWaiting = true;
        this.wait();
        this.isThreadWaiting = false;
        System.out.println(Thread.currentThread().getName() + " exited wait()");
      }
    }
  }

  // The SignalHolder class does not know how many signals were raised.
  private static class SignalCounter extends SignalCarrier {

    private int signals = 0;
    private int threadsWaiting = 0;

    @Override
    public void doNotify() {
      synchronized (this) {
        System.out.println(Thread.currentThread().getName() + " calling notify()");
        if (this.threadsWaiting == 0) {
          signals++;
        }
        this.notify();
        System.out.println(Thread.currentThread().getName() + " exited notify()");
      }
    }

    @Override
    public void doWait() throws InterruptedException {
      synchronized (this) {
        if (this.signals > 0) {
          System.out.println(
              Thread.currentThread().getName()
                  + this.signals
                  + " signals was already raised - decrementing signals and returning");
          this.signals--;
          return;
        }
        System.out.println(Thread.currentThread().getName() + " calling wait()");
        this.threadsWaiting++;
        this.wait();
        this.threadsWaiting--;
        System.out.println(Thread.currentThread().getName() + " exited wait()");
      }
    }
  }

  private static class SpuriousWakeUpHandler {

    private final Object monitorObject = new Object();
    private boolean signalRaised = false;

    public void doNotify() {
      synchronized (monitorObject) {
        System.out.println(Thread.currentThread().getName() + " Notifying ...");
        signalRaised = true;
        monitorObject.notify();
        System.out.println(Thread.currentThread().getName() + " Notified ...");
      }
    }

    public void doWait() throws InterruptedException {
      synchronized (monitorObject) {
        while (!signalRaised) {
          System.out.println(Thread.currentThread().getName() + " Signal not raised - waiting ...");
          monitorObject.wait();
          System.out.println(Thread.currentThread().getName() + " Signal raised - exiting wait");
        }
        signalRaised = false;
      }
    }
  }
}
