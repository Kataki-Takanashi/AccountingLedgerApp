package com.pluralsight.gui;

import com.pluralsight.Main;
import com.pluralsight.Transactions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static com.pluralsight.Main.Filename;

public class LedgerScene {

    private Stage stage;

    @FXML
    private TextField vendorSearchInput;

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
    private TableView<Transactions.Transaction> ledgerTable;

    public void initialize() throws IOException {
        List<Transactions.Transaction> transactions = Main.reloadTransactions();
        fillLedgerTable(transactions);
    }

    public void fillLedgerTable(List<Transactions.Transaction> transactions) {
        ledgerTable.getColumns().clear(); // Empty

        TableColumn<Transactions.Transaction, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Transactions.Transaction, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Transactions.Transaction, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Transactions.Transaction, String> vendorColumn = new TableColumn<>("Vendor");
        vendorColumn.setCellValueFactory(new PropertyValueFactory<>("vendor"));

        TableColumn<Transactions.Transaction, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

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
        clearInputs(event); // Just passed it event idk
    }

    public void clearInputs(ActionEvent event) {
        dateInput.clear(); timeInput.clear(); dateInput.clear(); vendorInput.clear(); ammountInput.clear();
    }

    public void exitApp(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        System.exit(0); // Quits the whole program not just the gui, this is to escape the main loop
    }
}
