package ggc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import ggc.exceptions.ProductWithoutBatchesException;


public class Product implements Serializable,Subject{

    private static final long serialVersionUID = 202110271517L;

    /* Product's id */
    private String _idProduct;

    /* Product's maxprice */
    private double _maxPrice;

    /* Product's stock */
    private int _stockTotal;

    private boolean _available=true;

    private int _n = 5;

    /*Product's batches*/
    private List<Batch> _batches = new ArrayList<>();

    private List<Observer> _observers = new ArrayList<>();


    public Product(String id, int units) {
        _idProduct = id;
        _maxPrice = 0;
        _stockTotal = units; 
    }

    /*getters and setters*/
    public String getId() { return _idProduct; }
    public void setId(String id) { _idProduct = id; }

    public double getMaxPrice() { return _maxPrice; }
    public void setMaxPrice(double maxPrice) { _maxPrice = maxPrice; }

    public int getStockTotal() { return _stockTotal; }
    public void setStockTotal(int stockTotal) { _stockTotal = stockTotal; }

    public int getN() {return _n;}
    public void setN(int n) {_n=n;}


    /*return all Product batches*/
    public List<Batch> batches() {
        return Collections.unmodifiableList(_batches);
    }

    /* Create a new Batch of this product */
    public void addBatch(Partner partner, int units, double price) {
        Batch b = new Batch(this, partner, units,price);
        if (price>_maxPrice)
            _maxPrice=price;
        _batches.add(b);
        _batches.sort(Batch.BATCH_COMPARATOR);
    }

    /*delete a batch*/
    public void removeBatch(Batch batch)
    {
        _batches.remove(batch);
        _batches.sort(Batch.BATCH_COMPARATOR);
    }

    public void registerObserver(Observer observer){
        _observers.add(observer);
    }

    public void removeObserver(Observer observer){
        _observers.remove(observer);
    }

    public void notifyObservers(Notification notification) {
        for (Observer o: _observers) 
            o.update(notification);
    }

    public void updateStock(int dif) {
        _stockTotal+=dif;
         _batches.sort(Batch.BATCH_COMPARATOR);
    }

    /*update the batch stock*/
    public void updateStock(int dif,double price) {
        if (!_available && dif>0){
            notifyObservers(new New(getId(),price));
            _available=true;
        }
        _stockTotal+=dif;
        if (_stockTotal==0)
            _available=false;
         _batches.sort(Batch.BATCH_COMPARATOR);
    }

    public double getMinPrice() throws ProductWithoutBatchesException{
        double min_price=_maxPrice;
        if (_stockTotal!=0) {
        for (Batch b: batches()) {
            if (b.getPrice()<min_price)
                min_price=b.getPrice();
        }
        return min_price;
        }
        else {throw new ProductWithoutBatchesException();}
    }

    public List<Batch> getBatchesSortedByPrice() {
        List<Batch> _batches= new ArrayList<>();
        _batches.addAll(batches());
        _batches.sort(Batch.BATCH_COMPARATOR_PRICE);
        return _batches;
    }

    public void toogleNotifications(Partner partner) {
        if (_observers.contains(partner)) {
            removeObserver(partner);
        }
        else {
            registerObserver(partner);
        }
    }

    /* equals method */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Product) {
            Product p = (Product) o;
            return _idProduct == p.getId() && _maxPrice == p.getMaxPrice() &&
             _stockTotal == p.getStockTotal();
        }
        return false;
    }

    /* toString method */
    @Override
    public String toString() {
        return _idProduct + "|" + Math.round(_maxPrice) + "|" + _stockTotal;
    }


}
