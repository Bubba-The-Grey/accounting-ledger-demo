package com.pluralsight;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.time.*;
import java.time.format.*;
public class AccountingLedger {

    // Creating a HashMap of Transactions as well as a universal formats
    public static HashMap<LocalDateTime, Transaction> transactions = new HashMap<>();
    public static Scanner scan = new Scanner(System.in);
    public static DecimalFormat df = new DecimalFormat("0.00");
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy HH:mm:ss");


    // Load Ledger Method
    public static void loadTransactions() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("transactions.csv"));
        String input;
        String[] inputs;
        String date;
        String time;
        String vendor, description;
        double amount;
        String dateTime;
        char DP;
        while((input = reader.readLine()) != null){
            inputs  = input.split("\\|");
            if(!inputs[0].equalsIgnoreCase("date")){
                date = dateFormatter.format(LocalDate.parse(inputs[0]));
                time = inputs[1];
                dateTime = date + " " + time;
                description = inputs[2];
                vendor = inputs[3];
                amount = Double.parseDouble(inputs[4]);
                DP = inputs[5].charAt(0);
                transactions.put(LocalDateTime.parse(dateTime, dateTimeFormatter), new Transaction(LocalDate.parse(date, dateFormatter), LocalTime.parse(time, timeFormatter), description, vendor, amount, DP));
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
        int input;
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
        System.out.println("Enter the time of deposit (use format HH:MM:SS or leave blank for system time): ");
        String time = scan.nextLine();
        if(time.equalsIgnoreCase("")){
            time = timeFormatter.format(LocalTime.now());
        }
        String dateTime = date + " " + time;
        System.out.println("Enter the description of the deposit: ");
        String description = scan.nextLine();
        System.out.println("Enter the vendor of the deposit: ");
        String vendor = scan.nextLine();
        System.out.println("Enter the amount deposited: $");
        double amount = Double.parseDouble(df.format(scan.nextDouble()));
        scan.nextLine();
        boolean sure = amount > 0;
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
        transactions.put(LocalDateTime.parse(dateTime, dateTimeFormatter), new Transaction(LocalDate.parse(date, dateFormatter), LocalTime.parse(time, timeFormatter), description, vendor, amount, 'D'));
        BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));
        writer.newLine();
        writer.write(transactions.get(LocalDateTime.parse(dateTime, dateTimeFormatter)).toString());
        writer.close();
    }
    public static void addPayment() throws IOException{
        System.out.println("Enter the date of payment (use format MM/DD/YYYY or leave blank for system date): ");
        String date = scan.nextLine();
        if(date.equalsIgnoreCase("")){
            date = dateFormatter.format(LocalDate.now());
        }
        System.out.println("Enter the time of payment (use format HH:MM:SS or leave blank for system time): ");
        String time = scan.nextLine();
        if(time.equalsIgnoreCase("")){
            time = timeFormatter.format(LocalTime.now());
        }
        String dateTime = date + " " + time;
        System.out.println("Enter the description of the payment: ");
        String description = scan.nextLine();
        System.out.println("Enter the vendor of the payment: ");
        String vendor = scan.nextLine();
        System.out.println("Enter the amount payment: $");
        double amount = Double.parseDouble(df.format(scan.nextDouble()));
        scan.nextLine();
        boolean sure = amount < 0;
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
        transactions.put(LocalDateTime.parse(dateTime, dateTimeFormatter), new Transaction(LocalDate.parse(date, dateFormatter), LocalTime.parse(time, timeFormatter), description, vendor, amount, 'P'));
        BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));
        writer.newLine();
        writer.write(transactions.get(LocalDateTime.parse(dateTime, dateTimeFormatter)).toString());
        writer.close();
    }

    public static void viewLedger() {
        int input;
        transactions = SortByDate(transactions);
        do {
            System.out.println("You are viewing the Ledger. Please choose an option");
            System.out.println("\t1 - View All Entries");
            System.out.println("\t2 - View Deposits");
            System.out.println("\t3 - View Payments(Debits)");
            System.out.println("\t4 - Pre-Defined Reports");
            System.out.println("\t5 - Custom Search");
            System.out.println("\t6 - Return to Home Screen");
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
                    CustomSearch();
                case 6:
                    System.out.println("Returning to Home Screen...");
                    break;
                default:
                    System.out.println("ERROR: Invalid Input (please enter 1, 2, 3, 4, 5, or 6)");
            }
        }while (input != 6);
    }
    public static void viewAllEntries(){
        for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
            System.out.println(aa.getValue().toString());
        }
    }
    public static void viewDeposits(){
        for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
            if(aa.getValue().getDP() == 'D') {
                System.out.println(aa.getValue().toString());
            }
        }
    }
    public static void viewPayments(){
        for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
            if(aa.getValue().getDP() == 'P') {
                System.out.println(aa.getValue().toString());
            }
        }
    }
    public static void Reports(){
        int input;
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
                    for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
                        if(aa.getValue().getDate().getYear() == LocalDate.now().getYear()){
                            if(aa.getValue().getDate().getMonth() == LocalDate.now().getMonth()){
                                System.out.println(aa.getValue().toString());
                            }
                        }
                    }
                    break;
                case 2:
                    for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
                        if(aa.getValue().getDate().getYear() == LocalDate.now().getYear()){
                            if(aa.getValue().getDate().getMonthValue() == (LocalDate.now().getMonthValue() - 1)){
                                System.out.println(aa.getValue().toString());
                            }
                        }
                        else if (LocalDate.now().getMonthValue() == 1){
                            if(aa.getValue().getDate().getMonthValue() == 12 && aa.getValue().getDate().getYear() == (LocalDate.now().getYear() - 1)){
                                System.out.println(aa.getValue().toString());
                            }
                        }
                    }
                    break;
                case 3:
                    for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
                        if(aa.getValue().getDate().getYear() == LocalDate.now().getYear()){
                            System.out.println(aa.getValue().toString());
                        }
                    }
                    break;
                case 4:
                    for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
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
    public static HashMap<LocalDateTime, Transaction> SortByDate(HashMap<LocalDateTime, Transaction> transactions){
        List<Map.Entry<LocalDateTime, Transaction>> list = new LinkedList<>(transactions.entrySet());
        list.sort((o1, o2) -> o2.getKey().compareTo(o1.getKey()));
        HashMap<LocalDateTime, Transaction> temp = new LinkedHashMap<>();
        for(Map.Entry<LocalDateTime, Transaction> aa : list){
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
    public static void searchByVendor(){
        System.out.println("Who are you searching for?");
        System.out.print("Vendor: ");
        String vendor = scan.nextLine();
        for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
            if(aa.getValue().getVendor().equalsIgnoreCase(vendor)){
                System.out.println(aa.getValue().toString());
            }
        }
    }
    public static void CustomSearch(){
        String startDate, endDate, description, vendor, amount, stringAgain;
        LocalDate beginning, end;
        boolean again = true;
        HashMap<LocalDateTime, Transaction> temp1 = new HashMap<>();
        HashMap<LocalDateTime, Transaction> temp2 = new HashMap<>();
        while(again){
            System.out.print("Enter the start date (MM/DD/YYYY) of the search (Leave blank for search to begin from the earliest date in the ledger): ");
            startDate = scan.nextLine();
            if (startDate.equalsIgnoreCase("")){
                beginning = LocalDate.MIN;
            }
            else{
                beginning = LocalDate.parse(startDate, dateFormatter);
            }
            System.out.print("Enter the end date (MM/DD/YYYY) of the search (Leave blank for search to the last date in the ledger): ");
            endDate = scan.nextLine();
            if (endDate.equalsIgnoreCase("")){
                end = LocalDate.MAX;
            }
            else{
                end = LocalDate.parse(endDate, dateFormatter);
            }
            for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
                if (aa.getValue().getDate().compareTo(beginning) >= 0 && aa.getValue().getDate().compareTo(end) <= 0){
                    temp1.put(aa.getKey(), aa.getValue());
                }
            }
            System.out.print("Enter the description of the search (Leave blank to show all relevant descriptions): ");
            description = scan.nextLine();
            System.out.print("Enter the vendor of the search (Leave blank to show all relevant vendors): ");
            vendor = scan.nextLine();
            System.out.print("Enter the transaction amount of the search (Leave blank to show all relevant transaction amounts): $");
            amount = scan.nextLine();
            if(description.isEmpty() && vendor.isEmpty() && amount.isEmpty()){
                for(Map.Entry<LocalDateTime, Transaction> aa : temp1.entrySet()){
                    temp2.put(aa.getKey(), aa.getValue());
                }
            }
            else if(!description.isEmpty() && vendor.isEmpty() && amount.isEmpty()){
                for(Map.Entry<LocalDateTime, Transaction> aa : temp1.entrySet()){
                    if(aa.getValue().getDescription().equalsIgnoreCase(description)) {
                        temp2.put(aa.getKey(), aa.getValue());
                    }
                }
            }
            else if(!description.isEmpty() && !vendor.isEmpty() && amount.isEmpty()){
                for(Map.Entry<LocalDateTime, Transaction> aa : temp1.entrySet()){
                    if(aa.getValue().getDescription().equalsIgnoreCase(description) && aa.getValue().getVendor().equalsIgnoreCase(vendor)) {
                        temp2.put(aa.getKey(), aa.getValue());
                    }
                }
            }
            else if(!description.isEmpty() && vendor.isEmpty() && !amount.isEmpty()){
                for(Map.Entry<LocalDateTime, Transaction> aa : temp1.entrySet()){
                    if(aa.getValue().getDescription().equalsIgnoreCase(description) && aa.getValue().getAmount() == Double.parseDouble(amount)) {
                        temp2.put(aa.getKey(), aa.getValue());
                    }
                }
            }
            else if(description.isEmpty() && !vendor.isEmpty() && amount.isEmpty()){
                for(Map.Entry<LocalDateTime, Transaction> aa : temp1.entrySet()){
                    if(aa.getValue().getVendor().equalsIgnoreCase(vendor)) {
                        temp2.put(aa.getKey(), aa.getValue());
                    }
                }
            }
            else if(description.isEmpty() && !vendor.isEmpty() && !amount.isEmpty()){
                for(Map.Entry<LocalDateTime, Transaction> aa : temp1.entrySet()){
                    if(aa.getValue().getVendor().equalsIgnoreCase(vendor) && aa.getValue().getAmount() == Double.parseDouble(amount)) {
                        temp2.put(aa.getKey(), aa.getValue());
                    }
                }
            }
            else if(description.isEmpty() && vendor.isEmpty() && !amount.isEmpty()){
                for(Map.Entry<LocalDateTime, Transaction> aa : temp1.entrySet()){
                    if(aa.getValue().getAmount() == Double.parseDouble(amount)) {
                        temp2.put(aa.getKey(), aa.getValue());
                    }
                }
            }
            else if(!description.isEmpty() && !vendor.isEmpty() && !amount.isEmpty()){
                for(Map.Entry<LocalDateTime, Transaction> aa : temp1.entrySet()){
                    if(aa.getValue().getDescription().equalsIgnoreCase(description) && aa.getValue().getVendor().equalsIgnoreCase(vendor) && aa.getValue().getAmount() == Double.parseDouble(amount)) {
                        temp2.put(aa.getKey(), aa.getValue());
                    }
                }
            }
            temp2 = SortByDate(temp2);
            for(Map.Entry<LocalDateTime, Transaction> aa : temp2.entrySet()){
                System.out.println(aa.getValue().toString());
            }
            temp1.clear();
            temp2.clear();
            System.out.print("Would you like to do another search (Y/N)? ");
            stringAgain = scan.nextLine();
            char answer = stringAgain.charAt(0);
            again = answer == 'y' || answer == 'Y';
        }
    }
}