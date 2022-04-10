package com.example.demo.models;

import com.google.gson.annotations.SerializedName;

public class Transaction {
    @SerializedName("Account Number")
    private  String accountNumber;
    @SerializedName("Type")
    private  String type;
    @SerializedName("Amount")
    private  String amount;
    @SerializedName("Currency")
    private  String currency;
    @SerializedName("AccountFrom")
    private  String accountFrom;
    
    public void setAccountNumber(String a)
    {
        accountNumber=a;
    }
    public void setType(String a)
    {
        type=a;
    }
    public void setAmount(String a)
    {
        amount=a;
    }
    public void setCurrency(String a)
    {
        currency=a;
    }
    public void setAccountFrom(String a)
    {
        accountFrom=a;
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }
    public String getType()
    {
        return type;
    }
    public String getAmount()
    {
        return amount;
    }
    public String getCurrency()
    {
        return currency;
    }
    public String getAccountFrom()
    {
        return accountFrom;
    }
   
}
