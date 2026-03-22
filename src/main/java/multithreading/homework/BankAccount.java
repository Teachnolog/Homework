package multithreading.homework;

public class BankAccount {
  private final int id;
  private int balance;

  public BankAccount(int id, int initialBalance) {
    this.id = id;
    this.balance = initialBalance;
  }

  public int getId() {
    return id;
  }

  public synchronized int getBalance() {
    return balance;
  }

  public synchronized boolean withdraw(int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Сумма должна быть положительной");
    }
    if (balance >= amount) {
      balance -= amount;
      return true;
    }
    return false;
  }

  public synchronized void deposit(int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Сумма должна быть положительной");
    }
    balance += amount;
  }

  public synchronized boolean hasSufficientFunds(int amount) {
    return balance >= amount;
  }

  @Override
  public String toString() {
    return "BankAccount{id=" + id + ", balance=" + balance + "}";
  }
}