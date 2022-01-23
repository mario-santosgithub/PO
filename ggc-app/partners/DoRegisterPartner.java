package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.WarehouseManager;
import ggc.app.exceptions.DuplicatePartnerKeyException;
import ggc.exceptions.DuplicateKeyException;



/**
 * Register new partner.
 */
class DoRegisterPartner extends Command<WarehouseManager> {

  DoRegisterPartner(WarehouseManager receiver) {
    super(Label.REGISTER_PARTNER, receiver);
    addStringField("id", Prompt.partnerKey());
    addStringField("name", Prompt.partnerName());
    addStringField("address", Prompt.partnerAddress());
  }

  @Override
  public void execute() throws CommandException {
    try{
      String id = stringField("id");
      String name = stringField("name");
      String address= stringField("address");
      _receiver.registerPartner(id,name,address); 
      } catch (DuplicateKeyException e) {
        throw new DuplicatePartnerKeyException (e.getKey());
    }
  }
}