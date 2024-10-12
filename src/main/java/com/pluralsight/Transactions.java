package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Transactions {

    /**
     * Loads all transactions, turns each into an object and puts them in a List.
     * @param Filename
     * @return List of Transaction objects
     * @throws FileNotFoundException
     */
    public List<Transaction> loadTransactions(String Filename) throws IOException {
        List<Transaction> loadedTransactions = new ArrayList<>();
        // Load file into buffer
        FileReader fr = new FileReader(Filename);
        BufferedReader br = new BufferedReader(fr);

        // Close file in case of error
        try {
            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split("\\|"); // split line into components

                loadedTransactions.add(new Transaction(
                        lineSplit[0],
                        lineSplit[1],
                        lineSplit[2],
                        lineSplit[3],
                        Double.parseDouble(lineSplit[4])
                ));

            }
        }
        finally {
            br.close();
        }
        return loadedTransactions;

    }


    public static class Transaction {
        private String date, time, description, vendor;
        private double amount;

        public Transaction(String date, String time, String description, String vendor, double amount) {
            this.date = date;
            this.time = time;
            this.description = description;
            this.vendor = vendor;
            this.amount = amount;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "Transaction{" +
                    "date='" + date + '\'' +
                    ", time='" + time + '\'' +
                    ", description='" + description + '\'' +
                    ", vendor='" + vendor + '\'' +
                    ", amount=" + amount +
                    '}';
        }
    }
}

