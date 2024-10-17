package com.pluralsight.Utils.gui;

import com.pluralsight.Main;
import com.pluralsight.Reports;
import com.pluralsight.Transactions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.pluralsight.Main.Filename;

public class LedgerScene {

    private Stage stage;

// Theese vars are refrences to Nodes of different types in the fxml file, the @FXML connects them to the one in the file
    @FXML
    private TextField vendorSearchInput;
    @FXML
    private TextField startDateInput;
    @FXML
    private TextField endDateInput;
    @FXML
    private TextField descriptionFilterInput;
    @FXML
    private TextField vendorFilterInput;
    @FXML
    private TextField amountFilterInput;

    @FXML
    private Label moneyLabel;

    @FXML
    TextField dateInput;
    @FXML
    TextField timeInput;
    @FXML
    TextField descInput;
    @FXML
    TextField vendorInput;
    @FXML
    TextField ammountInput;

    @FXML
    ToggleGroup filterGroup;

    @FXML
    private TableView<Transactions.Transaction> ledgerTable;
    // Special method that runs when the scene is loaded
    public void initialize() throws IOException {
        List<Transactions.Transaction> transactions = Main.reloadTransactions();
        fillLedgerTable(transactions);
    }

    public void fillLedgerTable(List<Transactions.Transaction> transactions) {
        ledgerTable.getColumns().clear(); // Empty

        TableColumn<Transactions.Transaction, String> dateColumn = new TableColumn<>("Date"); // This Date is a column name
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date")); // This tells it to take the date from each transaction, its like Transaction.getDate()

        TableColumn<Transactions.Transaction, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Transactions.Transaction, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Transactions.Transaction, String> vendorColumn = new TableColumn<>("Vendor");
        vendorColumn.setCellValueFactory(new PropertyValueFactory<>("vendor"));

        TableColumn<Transactions.Transaction, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        // Adds it all to the table
        ledgerTable.getColumns().addAll(dateColumn, timeColumn, descriptionColumn, vendorColumn, amountColumn);

        ObservableList<Transactions.Transaction> observableTransactions = FXCollections.observableArrayList(transactions);
        ledgerTable.setItems(observableTransactions);
        double sum = 0;
        for (Transactions.Transaction transaction : transactions) {
            sum += transaction.getAmount();
        }
        moneyLabel.setText('$' + String.valueOf(Math.round(sum * 100.0) / 100.0));
    }

    public void makeDeposit(ActionEvent event) throws IOException {

        List<Transactions.Transaction> output = Main.reloadTransactions();
        output.add(0, new Transactions.Transaction( // Add to beginning
                dateInput.getText(),
                timeInput.getText(),
                descInput.getText(),
                vendorInput.getText(),
                Double.parseDouble(ammountInput.getText())));
        new Transactions().saveTransactions(Filename, output);
        fillLedgerTable(output);
        clearInputs(event); // same here
    }

    public void makePayment(ActionEvent event) throws IOException {

        List<Transactions.Transaction> output = Main.reloadTransactions();
        double amount = Double.parseDouble(ammountInput.getText());
        amount *= -1;
        output.add(0, new Transactions.Transaction( // Add to beginning
                dateInput.getText(),
                timeInput.getText(),
                descInput.getText(),
                vendorInput.getText(),
                amount));
        new Transactions().saveTransactions(Filename, output);
        fillLedgerTable(output);
        clearInputs(event); // Just passed it event idk, maybe illl make a clear button idk
    }

    public void filterLeger(ActionEvent event) throws IOException {
        RadioButton selectedFilterButton = (RadioButton) filterGroup.getSelectedToggle();
        String selectedFilter = selectedFilterButton.getText();
        List<Transactions.Transaction> transactions = Main.reloadTransactions();

        switch (selectedFilter) {
            case "None":
                fillLedgerTable(transactions); break;
            case "Deposits":
                fillLedgerTable(transactions.stream()
                        .filter(transaction -> transaction.getAmount() > 0)
                        .collect(Collectors.toList())); break;
            case "Payments":
                fillLedgerTable(transactions.stream()
                        .filter(transaction -> transaction.getAmount() < 0)
                        .collect(Collectors.toList())); break;
            case "Month To Date":
                fillLedgerTable(Reports.TransactionFilters.filterMonthToDate(transactions)); break;
            case "Previous Month":
                fillLedgerTable(Reports.TransactionFilters.filterPreviousMonth(transactions)); break;
            case "Year To Date":
                fillLedgerTable(Reports.TransactionFilters.filterYearToDate(transactions)); break;
            case "Previous Year":
                fillLedgerTable(Reports.TransactionFilters.filterPreviousYear(transactions)); break;
            case "Search By Vendor":
                fillLedgerTable(Reports.TransactionFilters.filterByVendor(transactions, vendorSearchInput.getText())); break;
            case "Custom Search":
                fillLedgerTable(Reports.TransactionFilters.customSearch(transactions,
                        startDateInput.getText(), endDateInput.getText(), descriptionFilterInput.getText(),
                        vendorFilterInput.getText(), amountFilterInput.getText())); break;
        }
    }

    public void clearInputs(ActionEvent event) {
        dateInput.clear(); timeInput.clear(); descInput.clear(); vendorInput.clear(); ammountInput.clear();
    }

    public void exitApp(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        System.exit(0); // Quits the whole program not just the gui, this is to escape the main loop
    }
}
