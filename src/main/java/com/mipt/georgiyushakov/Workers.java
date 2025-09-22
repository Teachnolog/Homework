package com.mipt.georgiyushakov;

public abstract class Workers {
    // Метод work без реализации
    public abstract void work(int anyInt);

    // Метод goHome с реализацией
    public boolean goHome(String str1, String str2) {
        return str1.equals(str2);
    }
}