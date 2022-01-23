package ggc;
import java.io.Serializable;

public class Breakdown extends Transaction implements Serializable{

    private double _value;
    private double _paidValue;
    private String _recipe;

    public Breakdown(int id,String partnerId,String productId,int amount,
                    int paymentDate, double value, String recipe) {
        super(id,partnerId,productId,amount,paymentDate);
        _value=value;
        if (value>0)
            _paidValue=value;
        else 
            _paidValue=0;
        _recipe=recipe;
    }
    
    public double getValue() {return _value;}
    public void setValue(double value) {_value=value;}

    public double getPaidValue() {return _paidValue;}
    public void setPaidValue(double paidValue) {_paidValue=paidValue;}

    public String getRecipe() {return _recipe;}
    public void setRecipe(String recipe) {_recipe=recipe;}

    @Override
    public String toString() {
        return "DESAGREGAÇÃO" + "|" + super.toString() + "|" + Math.round(getValue()) + "|" + Math.round(getPaidValue())
                + "|" + getPaymentDate() + "|" + getRecipe();
    } 
}