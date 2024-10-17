package com.pluralsight;

import com.pluralsight.Utils.Console;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Reports {
    public static void reportsHome(List<Transactions.Transaction> transactions) {
        byte userSelection = 0;


        // Main Loop
        do {
            try {
                userSelection = displayOptions();
                switch (userSelection) {
                    case 1:
                        displayList(TransactionFilters.filterMonthToDate(transactions)); continue;
                    case 2:
                        displayList(TransactionFilters.filterPreviousMonth(transactions)); continue;
                    case 3:
                        displayList(TransactionFilters.filterYearToDate(transactions)); continue;
                    case 4:
                        displayList(TransactionFilters.filterPreviousYear(transactions));
                    case 5:
                        // Get vendor name
                        String vendor;
                        do {
                            vendor = Console.PromptForString("Enter vendor: ");
                        } while (vendor.isBlank());
                        displayList(TransactionFilters.filterByVendor(transactions, vendor)); continue;
                    case 6:
                        displayList(TransactionFilters.customSearch(transactions));
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println("command not found");
            }

        } while (userSelection != 0);
    }

    public static void displayList(List<Transactions.Transaction> transactions) {
        if (transactions.isEmpty()) {System.out.println("No results found for your search!"); return;}
        System.out.printf("%-13s %-15s %-50s %-40s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        for (Transactions.Transaction transaction : transactions) {
            System.out.printf("%-13s %-15s %-50s %-40s %-10s%n",
                    transaction.getDate(),
                    transaction.getTime(),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    String.format("$%.2f", transaction.getAmount()));
        }
    }

    private static byte displayOptions() throws IllegalArgumentException {
        String options = """
                    Please select from the following choices:
                    \t[1] Month To Date
                    \t[2] Previous Month
                    \t[3] Year To Date
                    \t[4] Previous Year
                    \t[5] Search By Vendor
                    \t[6] Custom Search
                    \t[0] Back
                    Enter Command:\s""";
        String selection;

        do {
            selection = Console.PromptForString(options);
        } while (selection.isEmpty());

        return switch (Byte.parseByte(selection)) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            case 4 -> 4;
            case 5 -> 5;
            case 6 -> 6;
            case 0 -> 0;
            default -> throw new IllegalArgumentException("Invalid selection: " + selection);
        };

    }

    // Filters
    public class TransactionFilters {

        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Constant is in all caps

        // 1. Month To Date Filter
        public static List<Transactions.Transaction> filterMonthToDate(List<Transactions.Transaction> transactions) {
            LocalDate now = LocalDate.now();
            return transactions.stream()
                    .filter(transaction -> {
                        LocalDate transactionDate = LocalDate.parse(transaction.getDate(), DATE_FORMATTER);
                        return transactionDate.getYear() == now.getYear() && transactionDate.getMonth() == now.getMonth() && !transactionDate.isAfter(now);
                    })
                    .collect(Collectors.toList());
        }

        // 2. Previous Month Filter
        public static List<Transactions.Transaction> filterPreviousMonth(List<Transactions.Transaction> transactions) {
            YearMonth previousMonth = YearMonth.now().minusMonths(1);
            return transactions.stream()
                    .filter(transaction -> {
                        LocalDate transactionDate = LocalDate.parse(transaction.getDate(), DATE_FORMATTER);
                        return YearMonth.from(transactionDate).equals(previousMonth);
                    })
                    .collect(Collectors.toList());
        }

        // 3. Year to Date Filter
        public static List<Transactions.Transaction> filterYearToDate(List<Transactions.Transaction> transactions) {
            LocalDate now = LocalDate.now();
            return transactions.stream()
                    .filter(transaction -> {
                        LocalDate transactionDate = LocalDate.parse(transaction.getDate(), DATE_FORMATTER);
                        return transactionDate.getYear() == now.getYear() && !transactionDate.isAfter(now);
                    })
                    .collect(Collectors.toList());
        }

        // 4. Previous Year Filter
        public static List<Transactions.Transaction> filterPreviousYear(List<Transactions.Transaction> transactions) {
            int previousYear = LocalDate.now().getYear() - 1;
            return transactions.stream()
                    .filter(transaction -> {
                        LocalDate transactionDate = LocalDate.parse(transaction.getDate(), DATE_FORMATTER);
                        return transactionDate.getYear() == previousYear;
                    })
                    .collect(Collectors.toList());
        }

        // 5. Search by Vendor
        public static List<Transactions.Transaction> filterByVendor(List<Transactions.Transaction> transactions, String vendor) {
            return transactions.stream()
                    .filter(transaction -> transaction.getVendor().equalsIgnoreCase(vendor))
                    .collect(Collectors.toList());
        }

        // 6. Custom Search
        public static List<Transactions.Transaction> customSearch(List<Transactions.Transaction> transactions) {
            List<Transactions.Transaction> resultList = transactions;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Get Start date
            String startDateString = Console.PromptForString("Start Date: ");
            if (!startDateString.isBlank()) {
                LocalDate startDate = LocalDate.parse(startDateString, formatter);

                // Use stream to filter transactions by start date
                resultList = resultList.stream()
                        .filter(transaction -> {
                            LocalDate transactionDate = LocalDate.parse(transaction.getDate(), formatter);
                            return transactionDate.isAfter(startDate);
                        }).collect(Collectors.toList());
            }

            // Get End Date
            String endDateString = Console.PromptForString("End Date: ");
            if (!endDateString.isBlank()) {
                LocalDate startDate = LocalDate.parse(endDateString, formatter);

                // Use stream to filter transactions by end date
                resultList = resultList.stream()
                        .filter(transaction -> {
                            LocalDate transactionDate = LocalDate.parse(transaction.getDate(), formatter);
                            return transactionDate.isBefore(startDate);
                        }).collect(Collectors.toList());
            }

            // Get Description
            String description = Console.PromptForString("Description: ");
            if (!description.isBlank()) {
                resultList = resultList.stream().filter(transaction -> {
                    return transaction.getDescription().equals(description);
                }).collect(Collectors.toList());
            }

            // Get Vendor
            String vendor = Console.PromptForString("Vendor: ");
            if (!vendor.isBlank()) {
                resultList = resultList.stream().filter(transaction -> {
                    return transaction.getVendor().equals(vendor);
                }).collect(Collectors.toList());
            }

            // Get Amount
            String inputDouble = Console.PromptForString("Amount: ");
            if (!inputDouble.isBlank()) {
                double amount = Double.parseDouble(inputDouble);
                resultList = resultList.stream().filter(transaction -> {
                    return transaction.getAmount() == amount;
                }).collect(Collectors.toList());
            }

            return resultList;
        }
        public static List<Transactions.Transaction> customSearch(List<Transactions.Transaction> transactions,
                                                                  String startDateString, String endDateString,
                                                                  String description, String vendor, String inputDouble) {
            List<Transactions.Transaction> resultList = transactions;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Get Start date
            if (!startDateString.isBlank()) {
                LocalDate startDate = LocalDate.parse(startDateString, formatter);

                // Use stream to filter transactions by start date
                resultList = resultList.stream()
                        .filter(transaction -> {
                            LocalDate transactionDate = LocalDate.parse(transaction.getDate(), formatter);
                            return transactionDate.isAfter(startDate);
                        }).collect(Collectors.toList());
            }

            // Get End Date
            if (!endDateString.isBlank()) {
                LocalDate startDate = LocalDate.parse(endDateString, formatter);

                // Use stream to filter transactions by end date
                resultList = resultList.stream()
                        .filter(transaction -> {
                            LocalDate transactionDate = LocalDate.parse(transaction.getDate(), formatter);
                            return transactionDate.isBefore(startDate);
                        }).collect(Collectors.toList());
            }

            // Get Description
            if (!description.isBlank()) {
                resultList = resultList.stream().filter(transaction -> {
                    return transaction.getDescription().equals(description);
                }).collect(Collectors.toList());
            }

            // Get Vendor
            if (!vendor.isBlank()) {
                resultList = resultList.stream().filter(transaction -> {
                    return transaction.getVendor().equals(vendor);
                }).collect(Collectors.toList());
            }

            // Get Amount
            if (!inputDouble.isBlank()) {
                double amount = Double.parseDouble(inputDouble);
                resultList = resultList.stream().filter(transaction -> {
                    return transaction.getAmount() == amount;
                }).collect(Collectors.toList());
            }

            return resultList;
        }
    }
}

