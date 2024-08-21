package com.canattofilipe.threads.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

    assertTrue(executorService.isShutdown());
  }

  private static Runnable newRunnable(String msg) {
    return () -> {
      String completeMsg = Thread.currentThread().getName() + " - " + msg;
      System.out.println(completeMsg);
    };
  }
}
