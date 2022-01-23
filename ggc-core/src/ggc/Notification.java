package ggc;

import java.io.Serializable;

public abstract class Notification implements Serializable{


    /* Prouct's id */
    private String _productId;
    private double _price;


    /* Constructor */
    public Notification(String productId, double price) {
        _productId = productId;
        _price = price;
    }


    @Override
    public String toString() {
        return '|' + _productId + '|' + Math.round(_price);
    } 
}