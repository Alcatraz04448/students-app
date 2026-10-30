package org.example.util;

import java.util.Scanner;

public class InputUtils {

    private final Scanner scanner;

    public InputUtils(Scanner scanner) {
        this.scanner = scanner;
    }

    public String ask(String question) {
        System.out.print(question);
        String answer = scanner.nextLine();
        // при перенаправлении ввода в начале строки иногда оказывается BOM
        if (answer.startsWith("﻿")) {
            answer = answer.substring(1);
        }
        return answer.trim();
    }

    public int askNumber(String question, int from, int to) {
        while (true) {
            String answer = ask(question);
            try {
                int number = Integer.parseInt(answer);
                if (number >= from && number <= to) {
                    return number;
                }
                System.out.printf("Нужно число от %d до %d%n", from, to);
            } catch (NumberFormatException e) {
                System.out.println("Это не число, попробуйте ещё раз");
            }
        }
    }
}
