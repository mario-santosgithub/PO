package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownKeyException;

/**
 * Show all transactions for a specific partner.
 */
class DoShowPartnerAcquisitions extends Command<WarehouseManager> {

  DoShowPartnerAcquisitions(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER_ACQUISITIONS, receiver);
    addStringField("key", Prompt.partnerKey());
  }

  @Override
  public void execute() throws CommandException {
    try{
      String id = stringField("key");
      _display.popup(_receiver.getPartnerAcquisitions(id)); 
      } catch (UnknownKeyException e) {
        throw new UnknownPartnerKeyException (e.getKey());
      }
  }

}
