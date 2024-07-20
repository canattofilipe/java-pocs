package com.canattofilipe.threads.examples;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

public class ThreadSignalingTest {

  @Test
  void basicSignalingExample() {

    Object signalingObject = new Object();
    final boolean[] isWaiterThreadWokenUp = {false};
    final boolean[] isNotifierThreadCompleted = {false};

    Thread waiterThread =
        new Thread(
            () -> {
              synchronized (signalingObject) {
                try {
                  signalingObject.wait();
                  isWaiterThreadWokenUp[0] = true;
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
                isNotifierThreadCompleted[0] = true;
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

    assertTrue(isWaiterThreadWokenUp[0], "waiterThread should have woken up");
    assertTrue(isNotifierThreadCompleted[0], "notifierThread should have completed");
  }

  @Test
  void threadSignalingExampleTest() {

    SignalCarrier signalCarrier = new SignalCarrier();
    final boolean[] isWaiterThreadWokenUp = {false};
    final boolean[] isNotifierThreadCompleted = {false};

    Thread waiterThread =
        new Thread(
            () -> {
              try {
                signalCarrier.doWait();
                isWaiterThreadWokenUp[0] = true;
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    Thread notifierThread =
        new Thread(
            () -> {
              try {
                signalCarrier.doNotify();
                isNotifierThreadCompleted[0] = true;
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

    assertTrue(isWaiterThreadWokenUp[0], "waiterThread should have woken up");
    assertTrue(isNotifierThreadCompleted[0], "notifierThread should have completed");
  }

  // The test will get stuck because wait was called before notify, causing the signal to be lost.
  @Test
  void missedSignalingExampleTest() {

    String shouldRun = System.getenv("RUN_STUCKABLE_TESTS");
    Assumptions.assumeTrue("true".equalsIgnoreCase(shouldRun));

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
    final boolean[] isWaiterThreadWokenUp = {false};
    final boolean[] isNotifierThreadCompleted = {false};

    Thread waiterThread =
        new Thread(
            () -> {
              try {
                signalCarrier.doWait();
                isWaiterThreadWokenUp[0] = true;
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    Thread notifierThread =
        new Thread(
            () -> {
              try {
                signalCarrier.doNotify();
                isNotifierThreadCompleted[0] = true;
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

    assertTrue(isWaiterThreadWokenUp[0], "waiterThread should have woken up");
    assertTrue(isNotifierThreadCompleted[0], "notifierThread should have completed");
  }

  @Test
  void missedSignalingMitigatedWithCounterExampleTest() {

    SignalCarrier signalCarrier = new SignalCounter();
    final boolean[] isWaiterThreadWokenUp = {false};
    final boolean[] isNotifierThreadCompleted = {false};

    Thread waiterThread =
        new Thread(
            () -> {
              try {
                signalCarrier.doWait();
                isWaiterThreadWokenUp[0] = true;
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    Thread notifierThread =
        new Thread(
            () -> {
              try {
                signalCarrier.doNotify();
                isNotifierThreadCompleted[0] = true;
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

    assertTrue(isWaiterThreadWokenUp[0], "waiterThread should have woken up");
    assertTrue(isNotifierThreadCompleted[0], "notifierThread should have completed");
  }

  @Test
  void notifyManyExampleTest() throws InterruptedException {

    SignalCarrier signalCarrier = new SignalCarrier();
    final AtomicBoolean isWaiterThread1WokenUp = new AtomicBoolean(false);
    AtomicBoolean isWaiterThread2WokenUp = new AtomicBoolean(false);
    AtomicBoolean isNotifierThreadCompleted = new AtomicBoolean(false);

    Thread waiterThread1 =
        new Thread(
            () -> {
              try {
                signalCarrier.doWait();
                isWaiterThread1WokenUp.set(true);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    Thread waiterThread2 =
        new Thread(
            () -> {
              try {
                signalCarrier.doWait();
                isWaiterThread2WokenUp.set(true);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    Thread notifierThread =
        new Thread(
            () -> {
              try {
                signalCarrier.doNotifyAll();
                isNotifierThreadCompleted.set(true);
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

    assertTrue(isWaiterThread1WokenUp.get(), "waiter1Thread should have woken up");
    assertTrue(isWaiterThread1WokenUp.get(), "waiter2Thread should have woken up");
    assertTrue(isNotifierThreadCompleted.get(), "notifierThread should have completed");
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

  private class SignalHolder extends SignalCarrier {
    private boolean signalRaised = false;
    private boolean isThreadWaiting = false;

    @Override
    public void doNotify() throws InterruptedException {
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
  private class SignalCounter extends SignalCarrier {

    private int signals = 0;
    private int threadsWaiting = 0;

    @Override
    public void doNotify() throws InterruptedException {
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
}
