package com.pluralsight;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Transaction {

    // Variables for Transactions
    private LocalDate date;
    private LocalTime time;
    private String vendor, description;
    private double amount;
    private char DP;
    public static DecimalFormat df = new DecimalFormat("0.00");
    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");



    // Constructor
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount, char DP){
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.vendor = vendor;
        this.description = description;
        this.DP = DP;
    }




    // Get and Set for Date of Transaction
    public LocalDate getDate() {
        return this.date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    // Get and Set for Time of Transaction
    public LocalTime getTime() {
        return this.time;
    }
    public void setTime(LocalTime time) {
        this.time = time;
    }

    // Get and Set for Vendor of Transaction
    public String getVendor() {
        return this.vendor;
    }
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    // Get and Set for Description of Transaction
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    // Get and Set for Amount in Transaction
    public double getAmount() {
        return this.amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Get and Set for Deposit or Payment in Transaction
    public char getDP(){
        return this.DP;
    }
    public void setDP(char DP){
        this.DP = DP;
    }




    // Custom toString() method to simplify the output process
    @Override
    public String toString() {
        return this.date + "|" + timeFormatter.format(this.time) + "|" + this.description + "|" + this.vendor + "|" + df.format(this.amount) + "|" + this.DP;
    }
}
