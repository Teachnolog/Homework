package multithreading.homework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

  @Test
  void testDeposit() {
    BankAccount account = new BankAccount(1, 100);
    account.deposit(50);
    assertEquals(150, account.getBalance());
  }

  @Test
  void testDepositNegativeAmount() {
    BankAccount account = new BankAccount(1, 100);

    assertThrows(IllegalArgumentException.class, () -> {
      account.deposit(-50);
    });

    assertThrows(IllegalArgumentException.class, () -> {
      account.deposit(0);
    });
  }

  @Test
  void testWithdrawSuccessful() {
    BankAccount account = new BankAccount(1, 100);
    boolean result = account.withdraw(50);

    assertTrue(result);
    assertEquals(50, account.getBalance());
  }

  @Test
  void testWithdrawInsufficientFunds() {
    BankAccount account = new BankAccount(1, 100);
    boolean result = account.withdraw(150);

    assertFalse(result);
    assertEquals(100, account.getBalance());
  }

  @Test
  void testWithdrawNegativeAmount() {
    BankAccount account = new BankAccount(1, 100);

    assertThrows(IllegalArgumentException.class, () -> {
      account.withdraw(-50);
    });
  }

  @Test
  void testHasSufficientFunds() {
    BankAccount account = new BankAccount(1, 100);

    assertTrue(account.hasSufficientFunds(50));
    assertTrue(account.hasSufficientFunds(100));
    assertFalse(account.hasSufficientFunds(150));
  }

  @Test
  void testConcurrentDeposits() throws InterruptedException {
    BankAccount account = new BankAccount(1, 0);
    int threadCount = 10;
    int depositsPerThread = 1000;

    Thread[] threads = new Thread[threadCount];

    for (int i = 0; i < threadCount; i++) {
      threads[i] = new Thread(() -> {
        for (int j = 0; j < depositsPerThread; j++) {
          account.deposit(1);
        }
      });
      threads[i].start();
    }

    for (Thread thread : threads) {
      thread.join();
    }

    assertEquals(threadCount * depositsPerThread, account.getBalance(),
        "Все депозиты должны быть учтены");
  }

  @Test
  void testConcurrentWithdraws() throws InterruptedException {
    BankAccount account = new BankAccount(1, 10000);
    int threadCount = 10;
    int withdrawsPerThread = 100;

    Thread[] threads = new Thread[threadCount];

    for (int i = 0; i < threadCount; i++) {
      threads[i] = new Thread(() -> {
        for (int j = 0; j < withdrawsPerThread; j++) {
          account.withdraw(1);
        }
      });
      threads[i].start();
    }

    for (Thread thread : threads) {
      thread.join();
    }

    int expectedMinBalance = 10000 - (threadCount * withdrawsPerThread);
    int actualBalance = account.getBalance();

    assertTrue(actualBalance >= 0, "Баланс не может быть отрицательным");
    assertTrue(actualBalance <= 10000, "Баланс не может быть больше начального");

    assertEquals(10000 - (threadCount * withdrawsPerThread), actualBalance,
        "Все снятия должны быть выполнены");
  }

  @Test
  void testThreadSafety() throws InterruptedException {
    BankAccount account = new BankAccount(1, 1000);

    Thread depositThread = new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        account.deposit(1);
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    });

    Thread withdrawThread = new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        account.withdraw(1);
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    });

    depositThread.start();
    withdrawThread.start();

    depositThread.join();
    withdrawThread.join();

    assertEquals(1000, account.getBalance(),
        "Баланс должен остаться неизменным после равного количества пополнений и снятий");
  }
}