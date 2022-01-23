package ggc;

public class New extends Notification {

    
    public New(String productId, double price) {
        super(productId, price);
    }


    @Override
    public String toString() {
        return "NEW" + super.toString();
    }
}    