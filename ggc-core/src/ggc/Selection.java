package ggc;

public class Selection extends Status { 


   public Selection(Partner partner) {
      super(partner);
   }

   public Selection(Partner partner, int points) {
      super(partner,points);
   }

   @Override
   public void upgrade1Status() {
      getPartner().setStatus(new Elite(getPartner(),getPoints()));
   }

   @Override
   public void upgrade2Status() {}

   @Override
   public void downgrade() {
      getPartner().setStatus(new Normal(getPartner(),getPoints()));
   }

   @Override
   public void checkStatus(int points) {
      if (points <= 2000) { downgrade(); }
      if (points >= 25000) { upgrade1Status(); }
   }


   @Override
   public double priceP1(int days, double baseValue) {
      baseValue *= 0.9;
      return baseValue;
   }

   @Override
   public double priceP2(int days, double baseValue) {
      if (days >= 2) {
         baseValue *= 0.95;
      }
      return baseValue;
   }

   @Override
   public double priceP3(int days, double baseValue) {
      if (days > 1) {
         baseValue *= 1 + (0.02*days);
      }
      return baseValue;
   }
    
   @Override
   public double priceP4(int days, double baseValue) {
      baseValue *= 1 + (0.05*days);
      return baseValue;
   }

   @Override
    public void removePoints(int delayDays) {
        if (delayDays>2) {
        updatePoints((int)Math.round(-getPoints()*0.90));
        downgrade();
        }
    }


   @Override
   public String toString() {
      return "SELECTION";
   }
}