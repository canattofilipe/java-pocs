package com.canattofilipe.threads.examples;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import org.junit.jupiter.api.Test;

public class ExecutorServiceExample1Test {

  @Test
  void simpleExecServiceWithRunnableAndVoidReturn() {

    ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    for (int i = 0; i < 30; i++) {
      executorService.execute(newRunnable("Task " + i));
    }

    System.out.println("[activeCount]: " + executorService.getActiveCount());
    System.out.println("[completedTaskCount]: " + executorService.getCompletedTaskCount());
    System.out.println("[corePoolSize]: " + executorService.getCorePoolSize());
    System.out.println("[largestPoolSize]: " + executorService.getLargestPoolSize());
    System.out.println("[maximumPoolSize]: " + executorService.getMaximumPoolSize());
    System.out.println("[poolSize]: " + executorService.getPoolSize());
    System.out.println("[taskCount]: " + executorService.getTaskCount());

    executorService.shutdown();

    assertTrue(executorService.isShutdown());
  }

  @Test
  void simpleExecServiceWithRunnableAndNonVoidReturn() throws ExecutionException {

    ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    List<Future> futures = new ArrayList<>();

    for (int i = 0; i < 30; i++) {
      final var task = executorService.submit(newRunnable("Task " + i));
      futures.add(task);
    }

    for (int i = 0; i < futures.size(); i++) {
      try {
        futures.get(i).get();
        System.out.println(futures.get(i).isDone());
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    executorService.shutdown();

    futures.forEach(future -> assertTrue(future.isDone()));
    assertTrue(executorService.isShutdown());
  }

  @Test
  void simpleExecServiceWithCallable() {

    ExecutorService executorService = Executors.newFixedThreadPool(1);

    Future future = executorService.submit(newCallable("Task 1"));

    try {
      final var result = future.get();
      System.out.println(result);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }

    executorService.shutdown();

    assertTrue(executorService.isShutdown());
    assertTrue(future.isDone());
  }

  private static Runnable newRunnable(String msg) {
    return () -> {
      String completeMsg = Thread.currentThread().getName() + " - " + msg;
      System.out.println(completeMsg);
    };
  }

  private static Callable newCallable(String msg) {
    return () -> {
      String completeMsg = Thread.currentThread().getName() + " - " + msg;
      return completeMsg;
    };
  }
}
