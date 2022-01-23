package ggc;

import java.io.Serializable;

public class Acquisition extends Transaction implements Serializable{

    private double _paidValue;

    public Acquisition(int id,String partnerId,String productId,int amount,int paymentDate, double paidValue) {
        super(id,partnerId,productId,amount,paymentDate);
        _paidValue=paidValue;
    }

    public double getPaidValue() {return _paidValue;}
    public void setPaidValue(double paidValue) {_paidValue=paidValue;}

    @Override
    public String toString() {
        return "COMPRA" + "|" + super.toString() + "|" + Math.round(getPaidValue()) + "|"
                + getPaymentDate();
    } 

}