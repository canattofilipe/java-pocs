package com.canattofilipe.threads.examples;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import org.junit.jupiter.api.Test;

public class ExecutorServiceExampleTest {

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

  @Test
  void shuttingDownExamples() {

    // shutdown() waits for all tasks to finish
    ExecutorService executorService1 = Executors.newFixedThreadPool(1);
    executorService1.shutdown();
    assertTrue(executorService1.isShutdown());

    // shutdownNow() interrupts all running tasks
    ExecutorService executorService2 = Executors.newFixedThreadPool(1);
    for (int i = 0; i < 10; i++) {
      executorService2.execute(newRunnable("Task " + i));
    }
    List<Runnable> runnables = executorService2.shutdownNow();
    assertTrue(executorService2.isShutdown());
    System.out.println("[runnables]: " + runnables.size());
    assertTrue(runnables.size() > 0);

    // awaitTermination() waits for all tasks to finish or timeout
    ExecutorService executorService3 = Executors.newFixedThreadPool(1);
    for (int i = 0; i < 10; i++) {
      executorService3.execute(newRunnable("Task " + i));
    }
    try {
      final var result =
          executorService3.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS);
      System.out.println("[awaitTermination]: " + result);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void cancellingTaskViaFutureTest() {
    ExecutorService executorService = Executors.newFixedThreadPool(1);
    final var future = executorService.submit(newCallable("Task 1"));
    assertFalse(future.isDone());
    try {
      final var wasCancelled = future.cancel(true);
      assertTrue(wasCancelled);
      future.get();
    } catch (CancellationException e) {
      System.out.println("Cannot get result from cancelled task");
    } catch (Exception e) {
      fail("Should not reach here");
    }
  }

  @Test
  void unfairnessExecutorServiceTest() {

    // by unfairness, we mean that the executor will accept both fast and slow tasks
    ExecutorService unfairnessExecutorService = Executors.newFixedThreadPool(1);

    Runnable fastTask = () -> System.out.println("Executing fast task");
    Runnable slowTask =
        () -> {
          try {
            System.out.println("Executing slow task");
            Thread.sleep(5000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        };

    long start = System.currentTimeMillis();
    unfairnessExecutorService.execute(fastTask);
    unfairnessExecutorService.execute(slowTask);
    unfairnessExecutorService.execute(fastTask);
    unfairnessExecutorService.execute(fastTask);

    try {
      unfairnessExecutorService.shutdown();
      unfairnessExecutorService.awaitTermination(100, java.util.concurrent.TimeUnit.SECONDS);
      long end = System.currentTimeMillis();
      System.out.println(end - start);
    } catch (InterruptedException e) {
      fail("Should not reach here");
    }
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
