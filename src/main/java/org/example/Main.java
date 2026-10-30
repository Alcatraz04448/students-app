package org.example;

import java.io.Console;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.example.util.InputUtils;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = createScanner()) {
            Menu menu = new Menu(new InputUtils(scanner));
            menu.start();
        } catch (NoSuchElementException e) {
            System.out.println();
            System.out.println("Ввод закрыт, программа завершена.");
        }
    }

    // в консоли Windows читаем через Console: она знает кодировку окна, и кириллица не ломается
    private static Scanner createScanner() {
        Console console = System.console();
        if (console != null) {
            return new Scanner(console.reader());
        }
        return new Scanner(System.in, StandardCharsets.UTF_8);
    }
}
