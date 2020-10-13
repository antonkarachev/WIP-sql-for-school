package ru.karachev.sqlforschool.view;

import java.util.Scanner;

public class ViewProvider {

    private final Scanner scanner;

    public ViewProvider(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readString() {
        return scanner.nextLine();
    }

    public int readInt() {
        return scanner.nextInt();
    }

    public void print(String input) {
        System.out.println(input);
    }

    public void printError() {
        System.out.println("No such command, please repeat");
    }
}
