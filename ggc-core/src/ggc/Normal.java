package ggc;

public class Normal extends Status {

    public Normal(Partner partner) {
        super(partner);
    }

    public Normal(Partner partner, int points) {
        super(partner,points);
    }


    @Override
    public void upgrade1Status() {
        getPartner().setStatus(new Selection(getPartner(),getPoints()));
    }

    @Override
    public void upgrade2Status() {
        getPartner().setStatus(new Elite(getPartner(),getPoints()));
    }

    @Override
    public void downgrade() {}


    @Override
    public void checkStatus(int points) {
        if (points > 2000) { upgrade1Status();}
        if (points >= 25000) {upgrade2Status();}
    }


    @Override
    public double priceP1(int days, double baseValue) {
        baseValue *= 0.9;
        return baseValue;
    }


    @Override
    public double priceP2(int days, double baseValue) {
        return baseValue;
    }

    @Override
    public double priceP3(int days, double baseValue) {
        baseValue *= 1 + (0.05*days);
        return baseValue;
    }

    
    @Override
    public double priceP4(int days, double baseValue) {
        baseValue *= 1 + (0.1*days);
        return baseValue;
    }

    @Override
    public void removePoints(int delayDays) {
        updatePoints(-getPoints());
    }

    @Override 
    public String toString() {
        return "NORMAL";
    }

}