package ggc;

public class Elite extends Status {

    public Elite(Partner partner) {
        super(partner);
    }

    public Elite(Partner partner, int points) {
        super(partner,points);
    }

    @Override
    public void downgrade() {
        getPartner().setStatus(new Selection(getPartner(),getPoints()));
    }
    
    @Override
    public void upgrade1Status() {}

    @Override
    public void upgrade2Status() {}

    @Override
    public void checkStatus(int points) {
        if (points <= 25000) { downgrade(); }
    }

    @Override
    public double priceP1(int days, double baseValue) {
        baseValue *= 0.9;
        return baseValue;
    }



    @Override
    public double priceP2(int days, double baseValue) {
        baseValue *= 0.9;
        return baseValue;
    }

    @Override
    public double priceP3(int days, double baseValue) {
        baseValue *= 0.95;
        return baseValue;
    }
    
    @Override
    public double priceP4(int days, double baseValue) {
        return baseValue;   
    }

    @Override
    public void removePoints(int delayDays) {
        if (delayDays>15) {
        updatePoints((int)Math.round(-getPoints()*0.75));
        downgrade();
        }
    }

    @Override
    public String toString() {
        return "ELITE";
    }
}