package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownProductException;
import ggc.exceptions.UnknownKeyException;

/**
 * Toggle product-related notifications.
 */
class DoToggleProductNotifications extends Command<WarehouseManager> {

  DoToggleProductNotifications(WarehouseManager receiver) {
    super(Label.TOGGLE_PRODUCT_NOTIFICATIONS, receiver);
    addStringField("idPartner", Prompt.partnerKey());
    addStringField("idProduct", Prompt.productKey());
  }

  @Override
  public void execute() throws CommandException {

    try {
      String idPartner = stringField("idPartner");
      String idProduct = stringField("idProduct");
      _receiver.toggleProductNotifications(idPartner, idProduct);
    }
    catch (UnknownKeyException e) {
      throw new UnknownPartnerKeyException (e.getKey());
    }
    catch (UnknownProductException e) {
      throw new UnknownProductKeyException (e.getId());
    }
  }

}
