package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownKeyException;

/**
 * Show batches supplied by partner.
 */
class DoShowBatchesByPartner extends Command<WarehouseManager> {

  DoShowBatchesByPartner(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_SUPPLIED_BY_PARTNER, receiver);
    addStringField("idPartner", Prompt.partnerKey());
  }

  @Override
  public final void execute() throws CommandException {
    try{
      String id = stringField("idPartner");
      _display.popup(_receiver.batchesByPartner(id)); 
      } catch (UnknownKeyException e) {
        throw new UnknownPartnerKeyException (e.getKey());
    }
  }

}
