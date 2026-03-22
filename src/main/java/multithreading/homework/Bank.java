package multithreading.homework;

public class Bank {

  public boolean sendToAccountDeadlock(BankAccount from, BankAccount to, int amount) {
    if (from == null || to == null) {
      throw new IllegalArgumentException("Счета не могут быть null");
    }
    if (amount <= 0) {
      throw new IllegalArgumentException("Сумма перевода должна быть положительной");
    }

    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    synchronized (from) {
      System.out.println(Thread.currentThread().getName() + " заблокировал счет " + from.getId());

      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      synchronized (to) {
        System.out.println(Thread.currentThread().getName() + " заблокировал счет " + to.getId());

        if (from.hasSufficientFunds(amount)) {
          from.withdraw(amount);
          to.deposit(amount);
          System.out.println(Thread.currentThread().getName() + " перевел " + amount + " с " + from.getId() + " на " + to.getId());
          return true;
        } else {
          System.out.println(Thread.currentThread().getName() + " недостаточно средств на счете " + from.getId());
          return false;
        }
      }
    }
  }

  public boolean sendToAccount(BankAccount from, BankAccount to, int amount) {
    if (from == null || to == null) {
      throw new IllegalArgumentException("Счета не могут быть null");
    }
    if (from == to) {
      throw new IllegalArgumentException("Нельзя переводить на тот же счет");
    }
    if (amount <= 0) {
      throw new IllegalArgumentException("Сумма перевода должна быть положительной");
    }

    BankAccount firstLock = from.getId() < to.getId() ? from : to;
    BankAccount secondLock = from.getId() < to.getId() ? to : from;

    synchronized (firstLock) {
      synchronized (secondLock) {
        if (from.hasSufficientFunds(amount)) {
          from.withdraw(amount);
          to.deposit(amount);
          System.out.println(Thread.currentThread().getName() + " перевел " + amount + " с " + from.getId() + " на " + to.getId());
          return true;
        } else {
          System.out.println(Thread.currentThread().getName() + " недостаточно средств на счете " + from.getId());
          return false;
        }
      }
    }
  }

  public boolean sendToAccountWithReentrantLock(BankAccount from, BankAccount to, int amount) {
    if (from == null || to == null) {
      throw new IllegalArgumentException("Счета не могут быть null");
    }
    if (amount <= 0) {
      throw new IllegalArgumentException("Сумма перевода должна быть положительной");
    }

    BankAccount firstLock = from.getId() < to.getId() ? from : to;
    BankAccount secondLock = from.getId() < to.getId() ? to : from;

    synchronized (firstLock) {
      synchronized (secondLock) {
        if (!from.hasSufficientFunds(amount)) {
          return false;
        }
        from.withdraw(amount);
        to.deposit(amount);
        return true;
      }
    }
  }
}