package multithreading.homework;

public class Main {
  public static void main(String[] args) {
    Bank bank = new Bank();

    BankAccount account1 = new BankAccount(1, 1000);
    BankAccount account2 = new BankAccount(2, 1000);

    System.out.println("=== Демонстрация корректного метода ===");
    System.out.println("Баланс до перевода:");
    System.out.println("Счет 1: " + account1.getBalance());
    System.out.println("Счет 2: " + account2.getBalance());

    boolean success = bank.sendToAccount(account1, account2, 500);
    System.out.println("Перевод успешен: " + success);

    System.out.println("Баланс после перевода:");
    System.out.println("Счет 1: " + account1.getBalance());
    System.out.println("Счет 2: " + account2.getBalance());

    System.out.println("\n=== Демонстрация дедлока ===");
    System.out.println("Запускаем два потока с дедлоком...");

    Thread thread1 = new Thread(() -> {
      bank.sendToAccountDeadlock(account1, account2, 100);
    }, "Thread-1");

    Thread thread2 = new Thread(() -> {
      bank.sendToAccountDeadlock(account2, account1, 100);
    }, "Thread-2");

    thread1.start();
    thread2.start();

    try {
      thread1.join(2000);
      thread2.join(2000);

      if (thread1.isAlive() || thread2.isAlive()) {
        System.out.println("\n⚠️ Обнаружен дедлок! Один или оба потока не завершились за 2 секунды.");
        thread1.interrupt();
        thread2.interrupt();
      } else {
        System.out.println("\n✓ Оба потока завершились успешно");
      }

    } catch (InterruptedException e) {
      System.out.println("Главный поток прерван");
    }

    System.out.println("\n=== Завершено ===");
  }
}