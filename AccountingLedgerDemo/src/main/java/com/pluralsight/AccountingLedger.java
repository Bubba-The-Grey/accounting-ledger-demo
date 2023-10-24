package com.pluralsight;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.time.*;
import java.time.format.*;
public class AccountingLedger {

    // Creating a HashMap of Transactions as well as a universal formats
    public static HashMap<Integer, Transaction> transactions = new HashMap<>();
    public static int count = 0;
    public static Scanner scan = new Scanner(System.in);
    public static DecimalFormat df = new DecimalFormat("0.00");
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");


    // Load Ledger Method
    public static void loadTransactions() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("transactions.csv"));
        String input = "";
        String[] inputs;
        LocalDate date;
        LocalTime time;
        String vendor, description;
        double amount;
        char DP;
        while((input = reader.readLine()) != null){
            inputs  = input.split("\\|");
            if(!inputs[0].equalsIgnoreCase("date")){
                date = LocalDate.parse(inputs[0]);
                time = LocalTime.parse(inputs[1]);
                description = inputs[2];
                vendor = inputs[3];
                amount = Double.parseDouble(inputs[4]);
                DP = inputs[5].charAt(0);
                transactions.put(count, new Transaction(date, time, description, vendor, amount, DP));
                count++;
            }
        }
        reader.close();
    }

    // Main Method, also Home Screen
    public static void main(String[] args) throws IOException {

        // Loads Ledger
        loadTransactions();

        // Greetings
        System.out.println("Welcome to the Ledger!");

        // User Options for Home Screen
        int input = 0;
        do{
            System.out.println("What would you like to do?");
            System.out.println("\t1 - Add a Deposit");
            System.out.println("\t2 - Add a Payment(Debit)");
            System.out.println("\t3 - View Ledger");
            System.out.println("\t4 - Exit Program");
            System.out.print("Your Choice: ");
            input = scan.nextInt();
            scan.nextLine();


            // Switch Case for selecting option based on User Input
            switch (input){
                case 1:
                    addDeposit();
                    break;
                case 2:
                    addPayment();
                    break;
                case 3:
                    viewLedger();
                    break;
                case 4:
                    System.out.println("Exiting Program...");
                    break;
                default:
                    System.out.println("ERROR: Invalid Input (please enter 1, 2, 3, or 4)");
            }
        } while (input != 4);
    }



    // Make a Deposit method
    public static void addDeposit() throws  IOException{
        System.out.println("Enter the date of deposit (use format MM/DD/YYYY or leave blank for system date): ");
        String date = scan.nextLine();
        if(date.equalsIgnoreCase("")){
            date = dateFormatter.format(LocalDate.now());
        }
        System.out.println("Enter the time of deposit (use format HH:MM or leave blank for system time): ");
        String time = scan.nextLine();
        if(time.equalsIgnoreCase("")){
            time = timeFormatter.format(LocalTime.now());
        }
        System.out.println("Enter the description of the deposit: ");
        String description = scan.nextLine();
        System.out.println("Enter the vendor of the deposit: ");
        String vendor = scan.nextLine();
        System.out.println("Enter the amount deposited: $");
        double amount = Double.parseDouble(df.format(scan.nextDouble()));
        scan.nextLine();
        boolean sure = false;
        if(amount > 0){
            sure = true;
        }
        while (!sure){
            System.out.print("Are you sure you want to enter a negative value? Deposits are usually positive (Y/N): ");
            String positive = scan.nextLine().substring(0, 1).toUpperCase();
            if(positive.equals("Y")){
                sure = true;
            }
            else{
                System.out.println("Enter the amount deposited: $");
                amount = Double.parseDouble(df.format(scan.nextDouble()));
                scan.nextLine();
                if(amount > 0){
                    sure = true;
                }
            }
        }
        transactions.put(count, new Transaction(LocalDate.parse(date, dateFormatter), LocalTime.parse(time, timeFormatter), description, vendor, amount, 'D'));
        BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));
        writer.newLine();
        writer.write(transactions.get(count).toString());
        writer.close();
        count++;
    }
    public static void addPayment() throws IOException{
        System.out.println("Enter the date of payment (use format MM/DD/YYYY or leave blank for system date): ");
        String date = scan.nextLine();
        System.out.println("Enter the time of payment (use format HH:MM or leave blank for system time): ");
        String time = scan.nextLine();
        System.out.println("Enter the description of the payment: ");
        String description = scan.nextLine();
        System.out.println("Enter the vendor of the payment: ");
        String vendor = scan.nextLine();
        System.out.println("Enter the amount payment: $");
        double amount = Double.parseDouble(df.format(scan.nextDouble()));
        scan.nextLine();
        boolean sure = false;
        if(amount < 0){
            sure = true;
        }
        while (!sure){
            System.out.print("Are you sure you want to enter a positive value? Payments are usually negative (Y/N): ");
            String positive = scan.nextLine().trim().substring(0, 1).toUpperCase();
            if(positive.equals("Y")){
                sure = true;
            }
            else{
                System.out.println("Enter the amount paid: $");
                amount = Double.parseDouble(df.format(scan.nextDouble()));
                scan.nextLine();
                if(amount < 0){
                    sure = true;
                }
            }
        }
        transactions.put(count, new Transaction(LocalDate.parse(date, dateFormatter), LocalTime.parse(time, timeFormatter), description, vendor, amount, 'D'));
        BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));
        writer.newLine();
        writer.write(transactions.get(count).toString());
        writer.close();
        count++;
    }

    public static void viewLedger() throws IOException {
        int input = 0;
        transactions = SortByDate();
        do {
            System.out.println("You are viewing the Ledger. Please choose an option");
            System.out.println("\t1 - View All Entries");
            System.out.println("\t2 - View Deposits");
            System.out.println("\t3 - View Payments(Debits)");
            System.out.println("\t4 - Pre-Defined Reports");
            System.out.println("\t5 - Return to Home Screen");
            System.out.print("Your Choice: ");
            input = scan.nextInt();
            scan.nextLine();
            switch(input){
                case 1:
                    viewAllEntries();
                    break;
                case 2:
                    viewDeposits();
                    break;
                case 3:
                    viewPayments();
                    break;
                case 4:
                    Reports();
                    break;
                case 5:
                    System.out.println("Returning to Home Screen...");
                    break;
                default:
                    System.out.println("ERROR: Invalid Input (please enter 1, 2, 3, 4, or 5)");
            }
        }while (input != 5);
    }
    public static void viewAllEntries(){
        for(Map.Entry<Integer, Transaction> aa : transactions.entrySet()){
            System.out.println(aa.getValue().toString());
        }
    }
    public static void viewDeposits(){
        for(Map.Entry<Integer, Transaction> aa : transactions.entrySet()){
            if(aa.getValue().getDP() == 'D') {
                System.out.println(aa.getValue().toString());
            }
        }
    }
    public static void viewPayments(){
        for(Map.Entry<Integer, Transaction> aa : transactions.entrySet()){
            if(aa.getValue().getDP() == 'P') {
                System.out.println(aa.getValue().toString());
            }
        }
    }
    public static void Reports(){
        int input = 0;
        do{
            System.out.println("You are viewing the Pre-defined Reports. Please choose an option");
            System.out.println("\t1 - Month-to-Date");
            System.out.println("\t2 - Previous Month");
            System.out.println("\t3 - Year-to-Date");
            System.out.println("\t4 - Previous Year");
            System.out.println("\t5 - Search by Vendor");
            System.out.println("\t6 - Return to Ledger");
            System.out.print("Your Choice: ");
            input = scan.nextInt();
            scan.nextLine();
            switch(input){
                case 1:
                    for(Map.Entry<Integer, Transaction> aa : transactions.entrySet()){
                        if(aa.getValue().getDate().getYear() == LocalDate.now().getYear()){
                            if(aa.getValue().getDate().getMonth() == LocalDate.now().getMonth()){
                                System.out.println(aa.getValue().toString());
                            }
                        }
                    }
                    break;
                case 2:
                    for(Map.Entry<Integer, Transaction> aa : transactions.entrySet()){
                        if(aa.getValue().getDate().getYear() == LocalDate.now().getYear()){
                            if(aa.getValue().getDate().getMonthValue() == (LocalDate.now().getMonthValue() - 1)){
                                System.out.println(aa.getValue().toString());
                            }
                        }
                    }
                    break;
                case 3:
                    for(Map.Entry<Integer, Transaction> aa : transactions.entrySet()){
                        if(aa.getValue().getDate().getYear() == LocalDate.now().getYear()){
                            System.out.println(aa.getValue().toString());
                        }
                    }
                    break;
                case 4:
                    for(Map.Entry<Integer, Transaction> aa : transactions.entrySet()){
                        if(aa.getValue().getDate().getYear() == (LocalDate.now().getYear() - 1)){
                            System.out.println(aa.getValue().toString());
                        }
                    }
                    break;
                case 5:
                    searchByVendor();
                    break;
                case 6:
                    System.out.println("Returning to Ledger...");
                    break;
                default:
                    System.out.println("ERROR: Invalid Input (please enter 1, 2, 3, 4, 5, or 6)");
            }
        }while (input != 6);
    }
    public static HashMap<Integer, Transaction> SortByDate(){
        List<Map.Entry<Integer, Transaction>> list = new LinkedList<>(transactions.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Transaction>>() {
            @Override
            public int compare(Map.Entry<Integer, Transaction> o1, Map.Entry<Integer, Transaction> o2) {
                return (o1.getValue().getDate().compareTo(o2.getValue().getDate()));
            }
        });
        HashMap<Integer, Transaction> temp = new HashMap<>();
        for(Map.Entry<Integer, Transaction> aa : list){
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
    public static void searchByVendor(){
        System.out.println("Who are you searching for?");
        System.out.print("Vendor: ");
        String vendor = scan.nextLine();
        for(Map.Entry<Integer, Transaction> aa : transactions.entrySet()){
            if(aa.getValue().getVendor().equalsIgnoreCase(vendor)){
                System.out.println(aa.getValue().toString());
            }
        }
    }
}
