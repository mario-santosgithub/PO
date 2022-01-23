package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownKeyException;


/**
 * Show partner.
 */
class DoShowPartner extends Command<WarehouseManager> {

  DoShowPartner(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER, receiver);
    addStringField("key", Prompt.partnerKey());
  }

  @Override
  public void execute() throws CommandException {
    try{
      String id = stringField("key");
      _display.popup(_receiver.getPartner(id)); 
      _display.popup(_receiver.getPartnerNotifications(id));
      } catch (UnknownKeyException e) {
        throw new UnknownPartnerKeyException (e.getKey());
      }
  }

}
