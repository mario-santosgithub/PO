package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.UnknownProductException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.app.exceptions.UnavailableProductException;
import ggc.exceptions.UnknownKeyException;
import ggc.exceptions.UnavailableProductAmountException;

/**
 * 
 */
public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

  public DoRegisterSaleTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    addStringField("idPartner", Prompt.partnerKey());
    addIntegerField("paymentDeadline", Prompt.paymentDeadline());
    addStringField("idProduct", Prompt.productKey());
    addIntegerField("amount", Prompt.amount());

  }

  @Override
  public final void execute() throws CommandException {
    String partnerId = stringField("idPartner");
    String productId = stringField("idProduct");
    int paymentDeadline = integerField("paymentDeadline");
    int amount = integerField("amount");
    try {
      _receiver.registerSale(partnerId,paymentDeadline,productId,amount);
    }
    catch (UnknownKeyException e) {throw new UnknownPartnerKeyException(e.getKey());}
    catch (UnknownProductException e) {throw new UnknownProductKeyException(e.getId());}
    catch (UnavailableProductAmountException e) {throw new UnavailableProductException(e.getId(),e.getRequested(),e.getAvailable());}
  }

}
