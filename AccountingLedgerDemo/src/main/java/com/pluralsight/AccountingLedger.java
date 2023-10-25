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
            // Only puts the information into the transactions HashMap IF it is not the first line, as the first line contains "date"
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

        // Displays options for the Home Screen
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
                    // Adds a Deposit based on user-input to the transactions HashMap and to the transactions.csv file
                    addDeposit();
                    break;
                case 2:
                    // Adds a Payment based on user-input to the transactions HashMap and to the transactions.csv file
                    addPayment();
                    break;
                case 3:
                    // Sends to Ledger Screen
                    viewLedger();
                    break;
                case 4:
                    // Exits program
                    System.out.println("Exiting Program...");
                    break;
                default:
                    // Doesn't end program, but tells user to input a valid option
                    System.out.println("ERROR: Invalid Input (please enter 1, 2, 3, or 4)");
            }
        } while (input != 4);
    }



    // Make a Deposit method
    public static void addDeposit() throws  IOException{

        // Asks for Date of deposit, uses System Date if no date is entered
        System.out.println("Enter the date of deposit (use format MM/DD/YYYY or leave blank for system date): ");
        String date = scan.nextLine();
        if(date.equalsIgnoreCase("")){
            date = dateFormatter.format(LocalDate.now());
        }

        // Asks for Time of deposit, uses System Time if no time is entered
        System.out.println("Enter the time of deposit (use format HH:MM:SS or leave blank for system time): ");
        String time = scan.nextLine();
        if(time.equalsIgnoreCase("")){
            time = timeFormatter.format(LocalTime.now());
        }

        // Creates the String that will be used to make the LocalDateTime that is the key for the HashMap
        String dateTime = date + " " + time;

        // Asks for Description of deposit
        System.out.println("Enter the description of the deposit: ");
        String description = scan.nextLine();

        // Asks who the Vendor of the deposit is
        System.out.println("Enter the vendor of the deposit: ");
        String vendor = scan.nextLine();

        // Asks the amount of money is deposited
        System.out.println("Enter the amount deposited: $");
        double amount = Double.parseDouble(df.format(scan.nextDouble()));
        scan.nextLine();

        // If the deposit is a negative value, it will warn the user. The user can still input a negative deposit with confirmation
        boolean sure = amount >= 0;
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

        // Puts the values into a HashMap using LocalDateTime as the key and the Transaction object as the value as well as write the information to the transactions.csv file
        transactions.put(LocalDateTime.parse(dateTime, dateTimeFormatter), new Transaction(LocalDate.parse(date, dateFormatter), LocalTime.parse(time, timeFormatter), description, vendor, amount, 'D'));
        BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));
        writer.newLine();
        writer.write(transactions.get(LocalDateTime.parse(dateTime, dateTimeFormatter)).toString());
        writer.close();
    }



    // Add Payment Method
    public static void addPayment() throws IOException{

        // Asks for Date of payment, uses System Date if no date is entered
        System.out.println("Enter the date of payment (use format MM/DD/YYYY or leave blank for system date): ");
        String date = scan.nextLine();
        if(date.equalsIgnoreCase("")){
            date = dateFormatter.format(LocalDate.now());
        }

        // Asks for Time of payment, uses System Time if no time is entered
        System.out.println("Enter the time of payment (use format HH:MM:SS or leave blank for system time): ");
        String time = scan.nextLine();
        if(time.equalsIgnoreCase("")){
            time = timeFormatter.format(LocalTime.now());
        }

        // Creates the String that will be used to make the LocalDateTime that is the key for the HashMap
        String dateTime = date + " " + time;

        // Asks for Description of payment
        System.out.println("Enter the description of the payment: ");
        String description = scan.nextLine();

        // Asks who the Vendor of the payment is
        System.out.println("Enter the vendor of the payment: ");
        String vendor = scan.nextLine();

        // Asks the amount of money is paid
        System.out.println("Enter the amount payment: $");
        double amount = Double.parseDouble(df.format(scan.nextDouble()));
        scan.nextLine();

        // If the payment is a positive value, it will warn the user. The user can still input a positive payment with confirmation
        boolean sure = amount <= 0;
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

        // Puts the values into a HashMap using LocalDateTime as the key and the Transaction object as the value as well as write the information to the transactions.csv file
        transactions.put(LocalDateTime.parse(dateTime, dateTimeFormatter), new Transaction(LocalDate.parse(date, dateFormatter), LocalTime.parse(time, timeFormatter), description, vendor, amount, 'P'));
        BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));
        writer.newLine();
        writer.write(transactions.get(LocalDateTime.parse(dateTime, dateTimeFormatter)).toString());
        writer.close();
    }



    // View Ledger Method
    public static void viewLedger() {
        int input;

        // Sorts the transactions HashMap by the date, showing the newest date first
        transactions = SortByDate(transactions);

        // Displays options for the Ledger Screen
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
                    // Shows all entries
                    viewAllEntries();
                    break;
                case 2:
                    // Shows only the Deposits
                    viewDeposits();
                    break;
                case 3:
                    // Shows only the payments
                    viewPayments();
                    break;
                case 4:
                    // Sends to Reports Screen
                    Reports();
                    break;
                case 5:
                    // Sends to Custom Search Screen
                    CustomSearch();
                case 6:
                    // Returns to Home Screen
                    System.out.println("Returning to Home Screen...");
                    break;
                default:
                    // Tells the user to input a valid option
                    System.out.println("ERROR: Invalid Input (please enter 1, 2, 3, 4, 5, or 6)");
            }
        }while (input != 6);
    }



    // Views ALL Entries of the Ledger
    public static void viewAllEntries(){
        for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
            System.out.println(aa.getValue().toString());
        }
    }



    // Views ONLY Deposits of the Ledger
    public static void viewDeposits(){
        for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
            if(aa.getValue().getDP() == 'D') {
                System.out.println(aa.getValue().toString());
            }
        }
    }



    // Views ONLY Payments of the Ledger
    public static void viewPayments(){
        for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
            if(aa.getValue().getDP() == 'P') {
                System.out.println(aa.getValue().toString());
            }
        }
    }



    // View Reports Screen
    public static void Reports(){
        int input;

        // Displays options for the Reports Screen
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
                    // Shows ONLY this month's entries
                    for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
                        if(aa.getValue().getDate().getYear() == LocalDate.now().getYear()){
                            if(aa.getValue().getDate().getMonth() == LocalDate.now().getMonth()){
                                System.out.println(aa.getValue().toString());
                            }
                        }
                    }
                    break;
                case 2:
                    // Shows ONLY last month's entries
                    for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
                        if(aa.getValue().getDate().getYear() == LocalDate.now().getYear()){
                            if(aa.getValue().getDate().getMonthValue() == (LocalDate.now().getMonthValue() - 1)){
                                System.out.println(aa.getValue().toString());
                            }
                        }
                        // If the current month is January, it displays the entries for December of the previous year
                        else if (LocalDate.now().getMonthValue() == 1){
                            if(aa.getValue().getDate().getMonthValue() == 12 && aa.getValue().getDate().getYear() == (LocalDate.now().getYear() - 1)){
                                System.out.println(aa.getValue().toString());
                            }
                        }
                    }
                    break;
                case 3:
                    // Shows ONLY this year's entries
                    for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
                        if(aa.getValue().getDate().getYear() == LocalDate.now().getYear()){
                            System.out.println(aa.getValue().toString());
                        }
                    }
                    break;
                case 4:
                    // Shows ONLY last year's entries
                    for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
                        if(aa.getValue().getDate().getYear() == (LocalDate.now().getYear() - 1)){
                            System.out.println(aa.getValue().toString());
                        }
                    }
                    break;
                case 5:
                    // Allows for a search by Vendor
                    searchByVendor();
                    break;
                case 6:
                    // Returns to Ledger Screen
                    System.out.println("Returning to Ledger...");
                    break;
                default:
                    // Tells the user to input a valid option
                    System.out.println("ERROR: Invalid Input (please enter 1, 2, 3, 4, 5, or 6)");
            }
        }while (input != 6);
    }



    // Method to sort the transactions by date, most recent entry first
    public static HashMap<LocalDateTime, Transaction> SortByDate(HashMap<LocalDateTime, Transaction> transactions){
        List<Map.Entry<LocalDateTime, Transaction>> list = new LinkedList<>(transactions.entrySet());
        list.sort((o1, o2) -> o2.getKey().compareTo(o1.getKey()));
        HashMap<LocalDateTime, Transaction> temp = new LinkedHashMap<>();
        for(Map.Entry<LocalDateTime, Transaction> aa : list){
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }



    // Method that searches the HashMap for a specific vendor and displays the transactions for that vendor
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



    // Custom Search Screen
    public static void CustomSearch(){
        // Creates variables for input information
        String startDate, endDate, description, vendor, amount, stringAgain;
        LocalDate beginning, end;
        boolean again = true;

        // Two HashMaps, one to store the information based on dates, the other to pull from the one based on the information given
        HashMap<LocalDateTime, Transaction> temp1 = new HashMap<>();
        HashMap<LocalDateTime, Transaction> temp2 = new HashMap<>();

        // Repeats as long as the user wants to use the custom search
        while(again){

            // Asks the user for the start date of the search. If the user does not enter one, it will put the minimum LocalDate for the searching parameter
            System.out.print("Enter the start date (MM/DD/YYYY) of the search (Leave blank for search to begin from the earliest date in the ledger): ");
            startDate = scan.nextLine();
            if (startDate.equalsIgnoreCase("")){
                beginning = LocalDate.MIN;
            }
            else{
                beginning = LocalDate.parse(startDate, dateFormatter);
            }

            // Asks the user for the end date of the search. If the user does not enter one, it will put the maximum LocalDate for the searching parameter
            System.out.print("Enter the end date (MM/DD/YYYY) of the search (Leave blank for search to the last date in the ledger): ");
            endDate = scan.nextLine();
            if (endDate.equalsIgnoreCase("")){
                end = LocalDate.MAX;
            }
            else{
                end = LocalDate.parse(endDate, dateFormatter);
            }

            // Puts all relevant dates into the first HashMap
            for(Map.Entry<LocalDateTime, Transaction> aa : transactions.entrySet()){
                if (aa.getValue().getDate().compareTo(beginning) >= 0 && aa.getValue().getDate().compareTo(end) <= 0){
                    temp1.put(aa.getKey(), aa.getValue());
                }
            }

            // Asks the user for the description. If left blank, it will be ignored later
            System.out.print("Enter the description of the search (Leave blank to show all relevant descriptions): ");
            description = scan.nextLine();

            // Asks the user for the vendor. If left blank, it will be ignored later
            System.out.print("Enter the vendor of the search (Leave blank to show all relevant vendors): ");
            vendor = scan.nextLine();

            // Asks the user for the money amount. If left blank, it will be ignored later
            System.out.print("Enter the transaction amount of the search (Leave blank to show all relevant transaction amounts): $");
            amount = scan.nextLine();

            // If and Else If statements to put into the second HashMap from the first, based on if the parameters are blank
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

            // Sorts the second HashMap by Date, just to be certain it shows the recent ones first
            temp2 = SortByDate(temp2);

            // Displays the entries from the Custom Search
            for(Map.Entry<LocalDateTime, Transaction> aa : temp2.entrySet()){
                System.out.println(aa.getValue().toString());
            }

            // Empties both of the HashMaps, so they may be used again
            temp1.clear();
            temp2.clear();

            // Asks the user if they would like to do another custom search. If they answer anything with a Y or y, it repeats. If not, then it returns to the Ledger Screen
            System.out.print("Would you like to do another custom search (Y/N)? ");
            stringAgain = scan.nextLine();
            char answer = stringAgain.charAt(0);
            again = answer == 'y' || answer == 'Y';
        }
        System.out.println("Returning to Ledger...");
    }
}