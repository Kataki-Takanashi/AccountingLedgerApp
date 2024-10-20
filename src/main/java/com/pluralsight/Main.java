package com.pluralsight;
// Imports
import com.pluralsight.Utils.Console;
import com.pluralsight.Utils.gui.AppMain;
import javafx.application.Application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Main {
    public static final String FILENAME = "testTransactions.csv"; // This is the name of the csv file we will use
    static final String RESET = "\u001B[0m";
    static final String RED = "\u001B[31m";
    static final String YELLOW = "\u001B[33m";
    static final String GREEN = "\u001B[32m";
    public static final String LOGO = GREEN + """
             _             _                \s
            | |    ___  __| | __ _  ___ _ __\s
            | |   / _ \\/ _` |/ _` |/ _ \\ '__|
            | |__|  __/ (_| | (_| |  __/ |  \s
            |_____\\___|\\__,_|\\__, |\\___|_|  \s
                             |___/          \s""" + RESET;
    public static void main(String[] args) throws IOException {
        // Load the transactions file
        Transactions t = new Transactions();
        List<Transactions.Transaction>  transactions = t.loadTransactions(FILENAME);
        System.out.println(LOGO); // Displaying the logo once

        char userSelection = 0;

        // Main Menu Loop
        do {
            // Reload transaction to reflect changes
            transactions = reloadTransactions();
            try {
                userSelection = displayOptions();
                switch (userSelection) {
                    case 'D': //Deposit
                        updateBalance(true); continue;
                    case 'P': //Payment
                        updateBalance(false); continue;
                    case 'L':
                        Ledger.ledgerHome(transactions); continue;
                    case 'G':
                        Application.launch(AppMain.class, args); // this is how you run it from a different file to pass the right args
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println("command not found");
            }

        } while (userSelection != 'X');
    }

    // Loads Transactions from file again to account for changes
    public static List<Transactions.Transaction> reloadTransactions() throws IOException {
        return new Transactions().loadTransactions(FILENAME);
    }


    private static char displayOptions() throws IllegalArgumentException {
        String options = """
                Welcome to The Bean Counter Ledger!
                Please select from the following choices:
                """ +
                GREEN + "\tAdd     [D]eposit" + '\n' + RESET +
                RED + "\tMake    [P]ayment" + '\n' + RESET +
                """
                \tView    [L]edger
                \tDisplay [G]UI
                \tExit    [X]
                Enter Command:\s""";
        String selection;

        do {
            selection = Console.PromptForString(options);
        } while (selection.isEmpty());

        return switch (selection.toUpperCase()) { // New way to do a switch statement!
            case "D" -> 'D';
            case "P" -> 'P';
            case "L" -> 'L';
            case "G" -> 'G';
            case "X", "EXIT", "E", "Q", "QUIT" -> 'X';
            default -> throw new IllegalArgumentException("Invalid selection: " + selection);
        };

    }

    private static void updateBalance(boolean isDeposit) throws IllegalArgumentException, IOException {
        boolean manualTime = Console.PromptForYesNo("Enter time and date manually");
        System.out.println("Press \"Q\" at anytime to quit!");

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormater = DateTimeFormatter.ofPattern("HH:mm:ss");

        if (manualTime) {
            date = getDateFromUser(dateFormater);
            if (date == null) return;

            time = getTimeFromUser();
            if (time == null) return;
        }

        String descriptionInput = getDescriptionFromUser();
        if (descriptionInput == null) return;

        String vendorInput = getVendorFromUser();
        if (vendorInput == null) return;

        double amount = getAmountFromUser(isDeposit);
        if (amount == 0) return;

        if (!isDeposit) {amount *= -1;}

        saveTransaction(date, time, timeFormater, descriptionInput, vendorInput, amount);
    }

    private static LocalDate getDateFromUser(DateTimeFormatter dateFormater) {
        String dateInput;
        LocalDate date = null;
        do {
            dateInput = Console.PromptForString("Enter date (yyyy-MM-dd): ");
            if (dateInput.equalsIgnoreCase("Q")) return null;
            try {
                date = LocalDate.parse(dateInput, dateFormater);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid Date Format!");
                dateInput = "";
            }
        } while (dateInput.isBlank());
        return date;
    }

    private static LocalTime getTimeFromUser() {
        String timeInput;
        LocalTime time = null;
        do {
            timeInput = Console.PromptForString("Enter time (HH:mm:ss): ");
            if (timeInput.equalsIgnoreCase("Q")) return null;
            try {
                time = LocalTime.parse(timeInput).withNano(0);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid Time Format!");
                timeInput = "";
            }
        } while (timeInput.isBlank());
        return time;
    }

    private static String getDescriptionFromUser() {
        String descriptionInput;
        do {
            descriptionInput = Console.PromptForString("Enter description: ");
            if (descriptionInput.equalsIgnoreCase("Q")) return null;
        } while (descriptionInput.isBlank());
        return descriptionInput;
    }

    private static String getVendorFromUser() {
        String vendorInput;
        do {
            vendorInput = Console.PromptForString("Enter vendor: ");
            if (vendorInput.equalsIgnoreCase("Q")) return null;
        } while (vendorInput.isBlank());
        return vendorInput;
    }

    private static double getAmountFromUser(boolean isDeposit) {
        String amountInput;
        double amount = 0;
        do {
            amountInput = Console.PromptForString("Enter amount: ");
            if (amountInput.equalsIgnoreCase("Q")) return 0;
            if (amountInput.isBlank()) continue;
            amount = Double.parseDouble(amountInput);
        } while (amount == 0);
        return amount;
    }

    private static void saveTransaction(LocalDate date, LocalTime time, DateTimeFormatter timeFormater, String descriptionInput, String vendorInput, double amount) throws IOException {
        List<Transactions.Transaction> output = new Transactions().loadTransactions(FILENAME);
        output.add(0, new Transactions.Transaction(
                date.toString(),
                time.format(timeFormater),
                descriptionInput,
                vendorInput,
                amount));
        new Transactions().saveTransactions(FILENAME, output);
    }
}
