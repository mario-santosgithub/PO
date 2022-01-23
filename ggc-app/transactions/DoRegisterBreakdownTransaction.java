package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.UnknownProductException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownKeyException;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.UnavailableProductAmountException;
import ggc.app.exceptions.UnavailableProductException;

/**
 * Register order.
 */
public class DoRegisterBreakdownTransaction extends Command<WarehouseManager> {

  public DoRegisterBreakdownTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_BREAKDOWN_TRANSACTION, receiver);
    addStringField("idPartner", Prompt.partnerKey());
    addStringField("idProduct", Prompt.productKey());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    String partnerId = stringField("idPartner");
    String productId = stringField("idProduct");
    int amount = integerField("amount");
    try {
      _receiver.registerBreakdown(partnerId,productId,amount);
    }
    catch(UnknownKeyException e) {throw new UnknownPartnerKeyException(e.getKey());}
    catch(UnknownProductException e) {throw new UnknownProductKeyException(e.getId());}
    catch(UnavailableProductAmountException e) {throw new UnavailableProductException(e.getId(),e.getRequested(),e.getAvailable());}
  }

}
