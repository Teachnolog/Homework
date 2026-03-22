package multithreading.homework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {

  @Test
  void testSendToAccountSuccessful() {
    Bank bank = new Bank();
    BankAccount account1 = new BankAccount(1, 1000);
    BankAccount account2 = new BankAccount(2, 500);

    boolean result = bank.sendToAccount(account1, account2, 300);

    assertTrue(result);
    assertEquals(700, account1.getBalance());
    assertEquals(800, account2.getBalance());
  }

  @Test
  void testSendToAccountInsufficientFunds() {
    Bank bank = new Bank();
    BankAccount account1 = new BankAccount(1, 100);
    BankAccount account2 = new BankAccount(2, 500);

    boolean result = bank.sendToAccount(account1, account2, 300);

    assertFalse(result);
    assertEquals(100, account1.getBalance());
    assertEquals(500, account2.getBalance());
  }

  @Test
  void testSendToAccountNegativeAmount() {
    Bank bank = new Bank();
    BankAccount account1 = new BankAccount(1, 1000);
    BankAccount account2 = new BankAccount(2, 500);

    assertThrows(IllegalArgumentException.class, () -> {
      bank.sendToAccount(account1, account2, -100);
    });
  }

  @Test
  void testSendToAccountNullAccounts() {
    Bank bank = new Bank();

    assertThrows(IllegalArgumentException.class, () -> {
      bank.sendToAccount(null, new BankAccount(1, 100), 100);
    });

    assertThrows(IllegalArgumentException.class, () -> {
      bank.sendToAccount(new BankAccount(1, 100), null, 100);
    });
  }

  @Test
  void testSendToAccountSameAccount() {
    Bank bank = new Bank();
    BankAccount account1 = new BankAccount(1, 1000);

    assertThrows(IllegalArgumentException.class, () -> {
      bank.sendToAccount(account1, account1, 100);
    });
  }

  @Test
  void testConcurrentTransfersCorrectMethod() throws InterruptedException {
    Bank bank = new Bank();
    BankAccount account1 = new BankAccount(1, 10000);
    BankAccount account2 = new BankAccount(2, 10000);

    int threadCount = 10;
    int transfersPerThread = 100;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    for (int i = 0; i < threadCount / 2; i++) {
      executor.submit(() -> {
        for (int j = 0; j < transfersPerThread; j++) {
          bank.sendToAccount(account1, account2, 10);
        }
      });

      executor.submit(() -> {
        for (int j = 0; j < transfersPerThread; j++) {
          bank.sendToAccount(account2, account1, 10);
        }
      });
    }

    executor.shutdown();
    boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);

    assertTrue(finished, "Все потоки должны завершиться за 10 секунд");

    int totalBalance = account1.getBalance() + account2.getBalance();
    assertEquals(20000, totalBalance, "Общий баланс должен сохраниться");
  }

  @Test
  @Timeout(5)
  void testDeadlockOccurs() throws InterruptedException {
    Bank bank = new Bank();
    BankAccount account1 = new BankAccount(1, 10000);
    BankAccount account2 = new BankAccount(2, 10000);

    Thread thread1 = new Thread(() -> {
      for (int i = 0; i < 100; i++) {
        bank.sendToAccountDeadlock(account1, account2, 10);
      }
    }, "Thread-1-to-2");

    Thread thread2 = new Thread(() -> {
      for (int i = 0; i < 100; i++) {
        bank.sendToAccountDeadlock(account2, account1, 10);
      }
    }, "Thread-2-to-1");

    thread1.start();
    thread2.start();

    thread1.join(2000);
    thread2.join(2000);

    boolean deadlockOccurred = thread1.isAlive() || thread2.isAlive();

    thread1.interrupt();
    thread2.interrupt();

    if (deadlockOccurred) {
      System.out.println("✓ Дедлок обнаружен (как и ожидалось)");
    } else {
      System.out.println("⚠️ Дедлок не произошел в этом запуске");
    }

    Thread.sleep(100);
  }

  @Test
  void testMultipleAccountsDeadlockFree() throws InterruptedException {
    Bank bank = new Bank();
    int accountCount = 5;
    BankAccount[] accounts = new BankAccount[accountCount];

    for (int i = 0; i < accountCount; i++) {
      accounts[i] = new BankAccount(i + 1, 10000);
    }

    int threadCount = 10;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    AtomicInteger successfulTransfers = new AtomicInteger(0);

    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        for (int j = 0; j < 100; j++) {
          int fromIdx = ThreadLocalRandom.current().nextInt(accountCount);
          int toIdx;
          do {
            toIdx = ThreadLocalRandom.current().nextInt(accountCount);
          } while (fromIdx == toIdx);

          int amount = ThreadLocalRandom.current().nextInt(1, 100);

          if (bank.sendToAccount(accounts[fromIdx], accounts[toIdx], amount)) {
            successfulTransfers.incrementAndGet();
          }
        }
      });
    }

    executor.shutdown();
    boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);

    assertTrue(finished, "Все потоки должны завершиться за 10 секунд");

    int totalBalance = 0;
    for (BankAccount account : accounts) {
      totalBalance += account.getBalance();
    }

    assertEquals(10000 * accountCount, totalBalance, "Общий баланс должен сохраниться");
    System.out.println("Успешных переводов: " + successfulTransfers.get());
  }

  @Test
  void testOrderingByAccountId() {
    Bank bank = new Bank();

    BankAccount account1 = new BankAccount(1, 1000);
    BankAccount account2 = new BankAccount(2, 1000);
    BankAccount account3 = new BankAccount(3, 1000);

    Thread thread1 = new Thread(() -> {
      for (int i = 0; i < 50; i++) {
        bank.sendToAccount(account1, account2, 10);
      }
    });

    Thread thread2 = new Thread(() -> {
      for (int i = 0; i < 50; i++) {
        bank.sendToAccount(account2, account3, 10);
      }
    });

    Thread thread3 = new Thread(() -> {
      for (int i = 0; i < 50; i++) {
        bank.sendToAccount(account3, account1, 10);
      }
    });

    thread1.start();
    thread2.start();
    thread3.start();

    try {
      thread1.join(3000);
      thread2.join(3000);
      thread3.join(3000);

      assertFalse(thread1.isAlive());
      assertFalse(thread2.isAlive());
      assertFalse(thread3.isAlive());

    } catch (InterruptedException e) {
      fail("Тест был прерван");
    }

    int totalBalance = account1.getBalance() + account2.getBalance() + account3.getBalance();
    assertEquals(3000, totalBalance, "Общий баланс должен сохраниться");
  }

  @Test
  void testStressTestWithManyThreads() throws InterruptedException {
    Bank bank = new Bank();
    BankAccount account1 = new BankAccount(1, 100000);
    BankAccount account2 = new BankAccount(2, 100000);

    int threadCount = 20;
    int operationsPerThread = 500;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    AtomicInteger successCount = new AtomicInteger(0);
    AtomicInteger failureCount = new AtomicInteger(0);

    for (int i = 0; i < threadCount; i++) {
      final boolean direction = i % 2 == 0;
      executor.submit(() -> {
        for (int j = 0; j < operationsPerThread; j++) {
          boolean result;
          if (direction) {
            result = bank.sendToAccount(account1, account2, 1);
          } else {
            result = bank.sendToAccount(account2, account1, 1);
          }

          if (result) {
            successCount.incrementAndGet();
          } else {
            failureCount.incrementAndGet();
          }
        }
      });
    }

    executor.shutdown();
    boolean finished = executor.awaitTermination(30, TimeUnit.SECONDS);

    assertTrue(finished, "Стресс-тест должен завершиться за 30 секунд");

    int totalBalance = account1.getBalance() + account2.getBalance();
    assertEquals(200000, totalBalance, "Общий баланс должен сохраниться");

    System.out.println("Стресс-тест: успешных операций = " + successCount.get() +
        ", неудачных = " + failureCount.get());
  }
}