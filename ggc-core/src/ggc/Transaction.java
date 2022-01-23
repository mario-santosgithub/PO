package ggc;

import java.io.Serializable;
import java.util.Comparator;

public abstract class Transaction implements Serializable{
 
    private int _id;
    private String _partnerId;
    private String _productId;
    private int _amount;
    private int _paymentDate;
     

    /* Comparator used to sort Transactions */
    public final static Comparator<Transaction> TRANSACTION_COMPARATOR = new TransactionComparator();

        private static class TransactionComparator implements Comparator<Transaction> {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                return t1.getId() - t2.getId();
            }
        }

    

    public Transaction(int id,String partnerId,String productId,int amount,int paymentDate) {
        _id=id;
        _partnerId=partnerId;
        _productId=productId;
        _amount=amount;
        _paymentDate=paymentDate;
    }

    public Transaction(int id,String partnerId,String productId,int amount) {
        _id=id;
        _partnerId=partnerId;
        _productId=productId;
        _amount=amount;
    }


    public int getId() {return _id;}
    public void setId(int id) {_id=id;}

    public String getPartnerId() {return _partnerId;}
    public void setPartnerId(String partnerId) {_partnerId=partnerId;} 

    public String getProductId() {return _productId;}
    public void setProductId(String productId) {_productId=productId;} 

    public int getAmount() {return _amount;}
    public void setAmount(int amount) {_amount=amount;} 

    public int getPaymentDate() {return _paymentDate;}
    public void setPaymentDate(int paymentDate) {_paymentDate=paymentDate;} 

    @Override
    public String toString() {
        return getId() + "|" + getPartnerId() + "|" + getProductId() + "|"
                + getAmount();
    } 


}