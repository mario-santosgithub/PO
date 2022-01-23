package ggc;

import java.io.Serializable;
import java.util.Comparator;

public class Batch implements Serializable{

    private static final long serialVersionUID = 202110271516L;

    /*Batch's Product*/
    private Product _product;

    /*Batch's Partner*/
    private Partner _partner;

    /*Batch's stock*/
    private int _units;

    /*Batch's price*/
    private double _price;

    /*Comparator used to sort batches*/
    public final static Comparator<Batch> BATCH_COMPARATOR = new BatchComparator();

    public final static Comparator<Batch> BATCH_COMPARATOR_PRICE = new BatchComparatorByPrice();

        private static class BatchComparator implements Comparator<Batch> {
        @Override
        public int compare(Batch b1, Batch b2) {
            if (b1.getPartner().getID().equals(b2.getPartner().getID())) {
                if (b1.getPrice()==b2.getPrice()){
                    return (int)(b1.getUnits() - b2.getUnits());
                }
                return (int)Math.signum((b1.getPrice() - b2.getPrice()));
            }
            return b1.getPartner().getID().compareTo(b2.getPartner().getID());
        }
        }

        private static class BatchComparatorByPrice implements Comparator<Batch> {
        @Override
        public int compare(Batch b1, Batch b2) {
            return (int)Math.signum((b1.getPrice() - b2.getPrice()));
        }
        }

    public Batch(Product product, Partner partner, int units, double price){
        _product=product;
        _partner=partner;
        _units=units;
        _price=price;
    }

    /*getters and setters*/
    public Product getProduct() {return _product;}
    public void setProduct(Product product) {_product=product;}

    public Partner getPartner() {return _partner;}
    public void setPartner(Partner partner) {_partner=partner;}

    public int getUnits() {return _units;}
    public void setUnits(int units) {_units=units;} 
    public void updateUnits(int dif) {_units+=dif;}

    public double getPrice() {return _price;}
    public void setPrice(double price) {_price=price;} 

    @Override
    public boolean equals(Object o) {
        if (o instanceof Batch) {
            Batch b = (Batch) o;
            return _product.equals(b.getProduct()) && _partner.equals(b.getPartner()) &&
            _units==b.getUnits() && _price==b.getPrice();
        }
        return false;
    }

    @Override
    public String toString() {
        return _product.getId() + "|" + _partner.getID() + "|" + Math.round(_price) + "|"
                + _units ; 
    }

}