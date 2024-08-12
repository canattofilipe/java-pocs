package com.canattofilipe.threads.examples;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class VirtualThreadsExampleTest {

  @Test
  void vThreadExampleTest() {

    List<Thread> vThreads = new ArrayList<>();

    int vThreadCont = 100_000;

    for (int i = 0; i < vThreadCont; i++) {
      int vThreadIndex = i;
      Thread vthread =
          Thread.ofVirtual()
              .start(
                  () -> {
                    int result = 1;
                    for (int j = 0; j < 10; j++) {
                      result *= (j + 1);
                    }
                    System.out.println("Result[" + vThreadIndex + "]: " + result);
                  });
      vThreads.add(vthread);
    }

    for (Thread vThread : vThreads) {
      try {
        vThread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
