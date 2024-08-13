package com.canattofilipe.threads.examples;

import com.canattofilipe.threads.examples.support.threadpool.ThreadPool;
import org.junit.jupiter.api.Test;

public class ThreadPoolExampleTest {

  @Test
  void threadPollExampleTest() throws Exception {

    ThreadPool threadPool = new ThreadPool(3, 10);

    for (int i = 0; i < 10; i++) {
      int taskNo = i;
      threadPool.execute(
          () -> {
            String message = Thread.currentThread().getName() + ": Task " + taskNo;
            System.out.println(message);
          });
    }

    threadPool.waitUntilAllTasksFinished();
    threadPool.stop();
  }
}
