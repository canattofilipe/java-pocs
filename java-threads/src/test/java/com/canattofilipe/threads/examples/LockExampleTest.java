package com.canattofilipe.threads.examples;

import com.canattofilipe.threads.examples.support.lock.Database;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.jupiter.api.Test;

public class LockExampleTest {

  @Test
  void saveThingDbTest() {

    String thing = "thing";

    // Uncomment the line below to use the DummyLock and check the logs to see the difference.
    // Database db = new Database(new DummyLock());

    Database db = new Database(new ReentrantLock());

    Thread t1 = new Thread(() -> db.save(thing));
    Thread t2 = new Thread(() -> db.save(thing));
    Thread t3 = new Thread(() -> db.save(thing));

    t1.start();
    t2.start();
    t3.start();

    try {
      t1.join();
      t2.join();
      t3.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
