package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownKeyException;

/**
 * Show all transactions for a specific partner.
 */
class DoShowPartnerSales extends Command<WarehouseManager> {

  DoShowPartnerSales(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER_SALES, receiver);
    addStringField("key", Prompt.partnerKey());
  }

  @Override
  public void execute() throws CommandException {
    String partnerId= stringField("key");
    try {
    _display.popup(_receiver.getPartnerSales(partnerId));}
    catch(UnknownKeyException e) {throw new UnknownPartnerKeyException(e.getKey());}

  }

}
