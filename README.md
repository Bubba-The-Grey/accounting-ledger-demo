# Accounting Ledger Demo

## Base Description

This program is merely a demo to show practical and conceptual knowledge of Java.  Skills included from Java are:
* Reading and displaying information from a file
* Displaying information from a file based on user criteria
* Writing to a file based on user input
* Sorting by date, showing the most recent information first

This "Account Ledger Demo" allows for inputs based on date, time, description, vendor, and the amount of money of the  
transaction. It sorts the information by the date and displays the newest entries first. The program features multiple  
pre-defined reports as well as a function for a custom search report.  Pre-defined reports include:
* Month-to-Date
* Previous Month
* Year-to-Date
* Previous Year
* Vendor Search
Custom Search includes option for:
* Start Date
* End Date
* Description of Transaction
* Vendor of Transaction
* Money Amount in Transaction

## Images

**THESE ARE DEMO IMAGES**

![](C:\Users\Bubba The Grey\Videos\Captures\AccountingLedgerDemo – AccountingLedger.java 10_25_2023 11_36_52 AM.png)
![](C:\Users\Bubba The Grey\Videos\Captures\AccountingLedgerDemo – AccountingLedger.java 10_25_2023 11_37_44 AM.png)
![](C:\Users\Bubba The Grey\Videos\Captures\AccountingLedgerDemo – AccountingLedger.java 10_25_2023 11_37_54 AM.png)
![](C:\Users\Bubba The Grey\Videos\Captures\AccountingLedgerDemo – AccountingLedger.java 10_25_2023 11_38_01 AM.png)
![](C:\Users\Bubba The Grey\Videos\Captures\AccountingLedgerDemo – AccountingLedger.java 10_25_2023 11_38_16 AM.png)
![](C:\Users\Bubba The Grey\Videos\Captures\AccountingLedgerDemo – AccountingLedger.java 10_25_2023 11_38_23 AM.png)
![](C:\Users\Bubba The Grey\Videos\Captures\AccountingLedgerDemo – AccountingLedger.java 10_25_2023 11_39_08 AM.png)

## Interesting Code

```
public static HashMap<LocalDateTime, Transaction> SortByDate(HashMap<LocalDateTime, Transaction> transactions){
        List<Map.Entry<LocalDateTime, Transaction>> list = new LinkedList<>(transactions.entrySet());
        list.sort((o1, o2) -> o2.getKey().compareTo(o1.getKey()));
        HashMap<LocalDateTime, Transaction> temp = new LinkedHashMap<>();
        for(Map.Entry<LocalDateTime, Transaction> aa : list){
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
```

The code above is interesting as it sorts the HashMap by the date in descending order, allowing it to display the  
information from the most recent date to the oldest date