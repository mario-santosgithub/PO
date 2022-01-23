package ggc;

import java.io.Serializable;

public class Sale extends Transaction implements Serializable{

    private double _value;
    private double _paidValue;
    private int _paymentDeadline;
    private int _saleDate;
    private boolean _paid=false;
    
    public Sale(int id,String partnerId,String productId,int amount,
                int paymentDate, double value, double paidValue, int paymentDeadline,int saleDate) {
        super(id,partnerId,productId,amount,paymentDate);
        _value=value;
        _paidValue=paidValue;
        _paymentDeadline=paymentDeadline;
        _saleDate=saleDate;
    }

    public Sale(int id,String partnerId,String productId,int amount, double value,  int paymentDeadline,int saleDate) {
        super(id,partnerId,productId,amount);
        _value=value;
        _paymentDeadline=paymentDeadline;
        _saleDate=saleDate;
        _paidValue=value;
    }

    public double getValue() {return _value;}
    public void setValue(double value) {_value=value;}

    public double getPaidValue() {return _paidValue;}
    public void setPaidValue(double paidValue) {_paidValue=paidValue;}

    public int getPaymentDeadline() {return _paymentDeadline;}
    public void setPaymentDeadline(int paymentDeadline) {_paymentDeadline=paymentDeadline;}

    public int getSaleDate() {return _saleDate;}
    public void setSaleDate(int date) {_saleDate=date;}

    public boolean paid() {return _paid;}
    public void setPaid() {_paid=true;}

    public void calculateValueToPay (int date,Partner partner, Product product) {
        double valueToPay=0;
        int deadline = _paymentDeadline;
        if ((deadline - date)>=product.getN())
            valueToPay = partner.calculatePriceP1(deadline -date,_value);
        if (0<=(deadline-date) && (deadline-date)<product.getN())
            valueToPay= partner.calculatePriceP2(deadline-date,_value);
        if (0<(date-deadline) && (date-deadline)<=product.getN())
            valueToPay= partner.calculatePriceP3(date-deadline,_value);
        if (date - deadline>product.getN())
            valueToPay= partner.calculatePriceP4(date-deadline,_value);
        _paidValue=valueToPay;
  }

    @Override
    public String toString() {
        String result="";
        result+= "VENDA" + "|" + super.toString() + "|" + Math.round(getValue()) + "|" + Math.round(getPaidValue()) + "|" + getPaymentDeadline();
        if (_paid)
            result+= "|" + getPaymentDate();
        return result;
    } 
}