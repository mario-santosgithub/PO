package ggc;

import java.io.Serializable;

public abstract class Status implements Serializable{

    private Partner _partner;

    private int _points=0;


    public Status(Partner partner) {
        _partner = partner;
    }
    

    public Status(Partner partner, int points) {
        _partner = partner;
        _points = points;
    }
    
    public int getPoints() {return _points;}
    public void setPoints(int points) {_points=points;}
    public void updatePoints(int dif) {_points+=dif;}


    public Partner getPartner() { return _partner; }


    public abstract void checkStatus(int points);

    public abstract void upgrade1Status();
    public abstract void upgrade2Status();
    public abstract void downgrade();

    public abstract double priceP1(int days, double baseValue);
    public abstract double priceP2(int days, double baseValue);
    public abstract double priceP3(int days, double baseValue);
    public abstract double priceP4(int days, double baseValue);

    public void addPoints (double baseValue) {
        int pointsToAdd = (int)Math.round(baseValue * 10);
        updatePoints(pointsToAdd);
        checkStatus(getPoints());
    }

    public abstract void removePoints(int delayDays);
}