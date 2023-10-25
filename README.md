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

![AccountingLedgerDemo – AccountingLedger java 10_25_2023 11_36_52 AM](https://github.com/Bubba-The-Grey/accounting-ledger-demo/assets/146876325/6a2e352a-d9f4-47a5-bc90-87d874c1d3ad)

![AccountingLedgerDemo – AccountingLedger java 10_25_2023 11_37_44 AM](https://github.com/Bubba-The-Grey/accounting-ledger-demo/assets/146876325/1d552389-7173-45c9-9266-de2269ce6224)

![AccountingLedgerDemo – AccountingLedger java 10_25_2023 11_37_54 AM](https://github.com/Bubba-The-Grey/accounting-ledger-demo/assets/146876325/1be99425-63dc-41ec-be89-88f8ce7176c5)

![AccountingLedgerDemo – AccountingLedger java 10_25_2023 11_38_01 AM](https://github.com/Bubba-The-Grey/accounting-ledger-demo/assets/146876325/92c8bed8-37b1-4c40-b008-79f8127473b4)

![AccountingLedgerDemo – AccountingLedger java 10_25_2023 11_38_16 AM](https://github.com/Bubba-The-Grey/accounting-ledger-demo/assets/146876325/5169e14a-3551-4e42-b51e-6010e7a23b3a)

![AccountingLedgerDemo – AccountingLedger java 10_25_2023 11_38_23 AM](https://github.com/Bubba-The-Grey/accounting-ledger-demo/assets/146876325/1bc56e38-ca30-4dc9-932a-2df0c728eaa5)

![AccountingLedgerDemo – AccountingLedger java 10_25_2023 11_39_08 AM](https://github.com/Bubba-The-Grey/accounting-ledger-demo/assets/146876325/8b9d6995-6938-4fa3-9e03-e75117ebbdab)

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