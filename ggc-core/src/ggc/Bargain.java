package ggc;

public class Bargain extends Notification {

    public Bargain(String productId, double price) {
        super(productId, price);
    }


    @Override
    public String toString() {
        return "BARGAIN" + super.toString();
    }
}