package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.UnknownTransactionException;
import ggc.app.exceptions.UnknownTransactionKeyException;

/**
 * Receive payment for sale transaction.
 */
public class DoReceivePayment extends Command<WarehouseManager> {

  public DoReceivePayment(WarehouseManager receiver) {
    super(Label.RECEIVE_PAYMENT, receiver);
    addIntegerField("idTransaction", Prompt.transactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    try{
    int id=integerField("idTransaction");
    _receiver.receivePayment(id);}
    catch (UnknownTransactionException e) {
      throw new UnknownTransactionKeyException(e.getKey());}
  }

}
