package com.pluralsight;

import com.pluralsight.Utils.Console;

import java.io.IOException;
import java.util.List;

public class Ledger {

    public static void ledgerHome(String Filename) throws IOException {
        // Load the transactions file
        Transactions t = new Transactions();
        List<Transactions.Transaction> transactions = t.loadTransactions(Filename);

        char userSelection = 0;


        // Main Loop
        do {
            try {
                userSelection = displayOptions();
                switch (userSelection) {
                    case 'A':
                        displayAll(transactions); continue;
                    case 'D':
                        displayAll(transactions, true); continue;
                    case 'P':
                        displayAll(transactions, false); continue;
                    case 'R':
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println("command not found");
            }

        } while (userSelection != 'X');
    }

    private static void displayAll(List<Transactions.Transaction> transactions) {
        // Print Header
        System.out.printf("%-13s %-15s %-50s %-40s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");

        // print in a formatted way
        for (Transactions.Transaction transaction : transactions) {
            System.out.printf("%-13s %-15s %-50s %-40s %-10s%n",
                    transaction.getDate(),
                    transaction.getTime(),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    String.format("$%.2f", transaction.getAmount()));
        }
    }

    private static void displayAll(List<Transactions.Transaction> transactions, boolean isDeposit) {
        // Print Header
        System.out.printf("%-13s %-15s %-50s %-40s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");

        // print in a formatted way
        for (Transactions.Transaction transaction : transactions) {
            if ((isDeposit && transaction.getAmount() > 0)
                    ||
                    (!isDeposit && transaction.getAmount() < 0)) {
                System.out.printf("%-13s %-15s %-50s %-40s %-10s%n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        String.format("$%.2f", transaction.getAmount()));
            }
        }
    }


    private static char displayOptions() throws IllegalArgumentException {
        String options = """
                    Please select from the following choices:
                    \tDisplay [A]ll Entries
                    \tDisplay [D]eposits
                    \tDisplay [P]ayments
                    \tDisplay [R]eports
                    \tExit    [X]
                    Enter Command:\s""";
        String selection;

        do {
            selection = Console.PromptForString(options);
        } while (selection.isEmpty());

        return switch (selection.toUpperCase()) { // New way to do a switch statement!
            case "A" -> 'A';
            case "D" -> 'D';
            case "P" -> 'P';
            case "R" -> 'R';
            case "X", "EXIT", "E", "Q", "QUIT" -> 'X';
            default -> throw new IllegalArgumentException("Invalid selection: " + selection);
        };

    }


}
