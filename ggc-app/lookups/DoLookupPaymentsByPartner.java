package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.UnknownKeyException;
import ggc.app.exceptions.UnknownPartnerKeyException;

/**
 * Lookup payments by given partner.
 */
public class DoLookupPaymentsByPartner extends Command<WarehouseManager> {

  public DoLookupPaymentsByPartner(WarehouseManager receiver) {
    super(Label.PAID_BY_PARTNER, receiver);
    addStringField("key", Prompt.partnerKey());
  }

  @Override
  public void execute() throws CommandException {
    try{
      String id = stringField("key");
      _display.popup(_receiver.getPaidTransactionsByPartner(id)); 
      }
      catch (UnknownKeyException e) {
        throw new UnknownPartnerKeyException (e.getKey());
      }
  }

}
