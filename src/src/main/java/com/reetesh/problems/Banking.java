package com.reetesh.problems;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Account {

    int id;
    long balance;
    final Object lock = new Object();
    Account(int id, long balance){
        this.id= id;
        this.balance=balance;
    }
    long getBalance(){
        return balance;
    }

}
class Bank{

    Map<Integer, Account> map = new ConcurrentHashMap<>();

     void deposit(int id, long amount){
         if(amount<=0) return;
         Account account = map.computeIfAbsent(id,k -> new Account(k,0));

         synchronized (account.lock){
            account.balance= account.balance+amount;
         }

        System.out.println("AccountId="+id+" deposit amount="+amount+" balance="+map.get(id).balance);

    }
     void withdraw(int id, long amount){
         Account acct= map.get(id);
         if(acct==null){
            System.out.println("Invalid Account");
            return;
        }


        synchronized (acct.lock){
            if(amount>acct.balance){
                return;
            }
            acct.balance = acct.balance - amount;
        }

        System.out.println("AccountId="+id+" withdraw| balance="+acct.balance);


    }
     void transfer(int fromId, int toId, long amount){
         if(amount<=0) return;
         if(fromId==toId) return;
         if(!map.containsKey(fromId)){
            System.out.println("Invalid Account");
            return;
        }
        map.putIfAbsent(toId, new Account(toId,0));
        Account a1 = map.get(fromId);
         if(a1==null) return;
        Account a2= map.get(toId);
         if(a2==null) return;

        Account first = fromId < toId ? a1 : a2;
        Account second = fromId < toId ? a2 : a1;

        synchronized (first.lock){
            synchronized (second.lock){
                if(a1.balance<amount){
                    return;
                }

                a1.balance = a1.balance - amount;
                a2.balance = a2.balance + amount;
            }
        }


        System.out.println("transfer sucessfully from Account="+fromId+" balance="+a1.balance);
        System.out.print(" to  Account="+toId+" balance="+a2.balance);

    }

}
public class Banking{

   public static void main(String args[]){
       Account a1= new Account(11,0l);

       Bank b1= new Bank();
       b1.deposit(11,100);
       b1.withdraw(11,50);

       Account a2= new Account(22,0l);
       b1.transfer(a1.id,a2.id,50);

   }

}