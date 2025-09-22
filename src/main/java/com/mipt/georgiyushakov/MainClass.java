package com.mipt.georgiyushakov;

public class MainClass {
    // Приватные поля int, String без инициализации
    private int privateInt;
    private String privateString;

    // Protected статическое поле double без инициализации
    protected static double protectedStaticDouble;

    // Публичное неизменяемое (final) поле long с инициализацией (12345)
    public final long publicFinalLong = 12345L;

    // Метод точки входа
    public static void main(String[] args) {
        // Цикл от 0 до 15
        for (int i = 0; i <= 15; i++) {
            System.out.println("Iter: " + i);
        }
    }
}