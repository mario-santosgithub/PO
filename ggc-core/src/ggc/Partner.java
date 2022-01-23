package ggc;

import java.io.Serializable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;




public class Partner implements Serializable, Observer {

    private static final long serialVersionUID = 202110271514L;
    
    /* Partner's identifactor */
    private String _idPartner;

    /* Partner's name */
    private String _namePartner;

    /* Partner's adress */
    private String _address;

    /* Partner's status */
    private Status _status;

    /* Partner's Sell Value*/
    private double _saleValue;

    /* Partner's Aquisition Value */
    private double _aquisitionValue;

    /* Partner's Paid Value */
    private double _paidValue=0;
    

    /* Partner's notifications */
    private ArrayList<Notification> _notifications = new ArrayList<Notification>();

    /* Partner's notifications delivery method */
    private DeliveryMethod _deliveryMethod = new AppRegister();

    /* Partner's Acquisitions */
    private Map<Integer, Acquisition> _acquisitions = new TreeMap<>();

    /* Partner's Sales */
    private Map<Integer, Sale> _sales = new TreeMap<>();

    /* Partner's Breakdowns */
    private Map<Integer, Breakdown> _breakdowns = new TreeMap<>();


    /* Constructor */
    public Partner(String id, String name, String address) {
        _idPartner = id;
        _namePartner = name;
        _address = address;
        _status = new Normal(this);
    }



    /* Geters and Seters for id */
    public String getID() { return _idPartner; }
    public void setID(String id) { _idPartner = id; }

    /* Geters and Seter for name */
    public String getName() { return _namePartner; }
    public void setName(String name) { _namePartner = name; }

    /* Geters and Seters for address */
    public String getAdress() { return _address; }
    public void setAdress(String address) { _address = address; }

    /* Geters and Seters for points */
    public int getPoints() { return _status.getPoints(); }
    public void setPoints(int points) { _status.setPoints(points); }

    /* Geters and Seters for status */
    public Status getStatus() { return _status; }
    public void setStatus(Status status) { _status = status; }

    public void updateAcquisitionValue(double dif) {_aquisitionValue+=dif;} 
    public void updateSalesValue(double dif) {_saleValue+=dif;}
    public void updatePaidValue(double dif) {_paidValue+=dif;}

    /* Geters and Seters for Aquisitions */
    public Collection<Acquisition> getAquisitions() {
        return Collections.unmodifiableCollection(_acquisitions.values());
    }

    /* Geters and Seters for Sales */
    public Collection<Sale> getSales() {
        return Collections.unmodifiableCollection(_sales.values());
    }

    /* Geters and Seters for Breakdowns */
    public Collection<Breakdown> getBreakdowns() {
        return Collections.unmodifiableCollection(_breakdowns.values());
    }

    public void addAcquisition (Acquisition acquisition) 
    {
        _acquisitions.put(acquisition.getId(),acquisition);
    }

    public Acquisition getAcquisition (int Id) 
    {
        return _acquisitions.get(Id);
    }

    public void addSale (Sale sale) 
    {
        _sales.put(sale.getId(),sale);
    }

    public Sale getSale(int Id) 
    { 
        return _sales.get(Id);
    }

    public void addBreakdown (Breakdown breakdown) 
    {
        _breakdowns.put(breakdown.getId(),breakdown);
    }

    public Breakdown getBreakdown (int Id) 
    {
        return _breakdowns.get(Id);
    }


    /* Methods for class Partner */

    public void addPoints(double value) {
        _status.addPoints(value);
    }

    public void removePoints(int days) {
        _status.removePoints(days);
    }

    public void update(Notification notification) {
        _deliveryMethod.send(this, notification);
    }


    public void addNotification(Notification notification) {
        _notifications.add(notification);
    }

    /* Geters and Seters for notifications */
    public Collection<Notification> getNotifications() { 
        return Collections.unmodifiableCollection(_notifications);
    } 

    public void clearNotifications() {
        _notifications.clear();
    }

    public void updatePoints(int diff) {
        _status.updatePoints(diff);
    }


    public double calculatePriceP1(int days, double baseValue) {
        return _status.priceP1(days, baseValue);
    }
    public double calculatePriceP2(int days, double baseValue) {
        return _status.priceP2(days, baseValue);
    }
    public double calculatePriceP3(int days, double baseValue) {
        return _status.priceP3(days, baseValue);
    }
    public double calculatePriceP4(int days, double baseValue) {
        return _status.priceP4(days, baseValue);
    }



    /* equals method */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Partner) {
            Partner p = (Partner) o;
            return _idPartner == p.getID() && _address == p.getAdress()
             && getPoints() == p.getPoints(); 
        }
        return false;
    }

    @Override
    public String toString() {

        return _idPartner + "|" + _namePartner + "|" + _address + "|" +
          _status + "|" + getPoints()+ "|" + (int) Math.round(_aquisitionValue) + 
          "|" + (int)Math.round(_saleValue) + "|" +
            (int)Math.round(_paidValue);
    }
}   