package com.pluralsight;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
        Transactions t = new Transactions();
        List<Transactions.Transaction>  tLoaded = t.loadTransactions("transactions.csv");
        for (Transactions.Transaction i : tLoaded) {
            System.out.println(i.toString());
        }
    }
}