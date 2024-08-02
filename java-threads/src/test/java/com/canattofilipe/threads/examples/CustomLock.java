package com.canattofilipe.threads.examples;

public interface CustomLock {

  void lock() throws InterruptedException;

  void unlock();
}
