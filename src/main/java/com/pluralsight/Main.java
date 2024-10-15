package com.pluralsight;

import com.pluralsight.Utils.Console;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String Filename = "testTransactions.csv";
    public static void main(String[] args) throws IOException {

        char userSelection = 0;

        // Main Loop
        do {
            // Load the transactions file
            Transactions t = new Transactions();
            List<Transactions.Transaction>  transactions = t.loadTransactions(Filename);
            try {
                userSelection = displayOptions();
                switch (userSelection) {
                    case 'D':
                        updateBalance(true); continue;
                    case 'P':
                        updateBalance(false); continue;
                    case 'L':
                        Ledger.ledgerHome(transactions); continue;
                    case 'G':
                        guitest.main(args); // should i be passing this?
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println("command not found");
            }

        } while (userSelection != 'X');
    }


    private static char displayOptions() throws IllegalArgumentException {
        String options = """
                Welcome to The Bean Counter Ledger!
                Please select from the following choices:
                \tAdd     [D]eposit
                \tMake    [P]ayment
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
        // ask user for
        boolean manualTime = Console.PromptForYesNo("Enter time and date manually"); // ask if user wants to enter time manually
        System.out.println("Press \"Q\" at anytime to quit!");
        LocalDate date = LocalDate.now();LocalTime time = LocalTime.now(); // Get current time and date
        DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Moved lines out of if block to change scope
        DateTimeFormatter timeFormater = DateTimeFormatter.ofPattern("HH:mm:ss");
        if (manualTime) { // if so get the date and time from user
            String dateInput, timeInput;

            do {
                dateInput = Console.PromptForString("Enter date (yyyy-MM-dd): ");
                if (dateInput.equalsIgnoreCase("Q")) {return;} // Q to exit the method
                try {
                    date = LocalDate.parse(dateInput, dateFormater);
                }
                catch (DateTimeParseException e) {
                    System.out.println("Invalid Date Format!"); // Interesting error here it ignored the continue statement here because the input isn't blank
                    dateInput = "";
                }

            }while (dateInput.isBlank());

            do {
                timeInput = Console.PromptForString("Enter time (HH:mm:ss): ");
                if (timeInput.equalsIgnoreCase("Q")) {return;} // Q to exit the method
                try {
                    time = LocalTime.parse(timeInput); // Parse time input directly
                    time = time.withNano(0);  // Strip fractional seconds
                }
                catch (DateTimeParseException e) {
                    System.out.println("Invalid Time Format!");
                    timeInput = "";
                }

            }while (timeInput.isBlank());
        }
        String descriptionInput = "", vendorInput = "", amountInput;
        double amount = 0;


        do {
            descriptionInput = Console.PromptForString("Enter description: ");
            if (descriptionInput.equalsIgnoreCase("Q")) {return;}
        } while (descriptionInput.isBlank());
        do {
            vendorInput = Console.PromptForString("Enter vendor: ");
            if (vendorInput.equalsIgnoreCase("Q")) {return;}
        } while (vendorInput.isBlank());
        do {
            amountInput = Console.PromptForString("Enter amount: ");
            if (amountInput.equalsIgnoreCase("Q")) {return;}
            if (amountInput.isBlank()) {continue;}
            amount = Double.parseDouble(amountInput);
            if (!isDeposit) {amount *= -1;}
        } while (amount == 0);

        // Save to disk
        List<Transactions.Transaction> output = new Transactions().loadTransactions(Filename);
        output.add(0, new Transactions.Transaction( // Add to beginning
                date.toString(),
                time.format(timeFormater),  // ensure consistent time format to the fix the nanosecond and removing seconds bugs
                descriptionInput,
                vendorInput,
                amount));
        new Transactions().saveTransactions(Filename, output);
    }
}
