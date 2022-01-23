package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.exceptions.UnknownTransactionException;
import ggc.app.exceptions.UnknownTransactionKeyException;

/**
 * Show specific transaction.
 */
public class DoShowTransaction extends Command<WarehouseManager> {

  public DoShowTransaction(WarehouseManager receiver) {
    super(Label.SHOW_TRANSACTION, receiver);

    addIntegerField("idTransaction", Prompt.transactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    try{
    int id=integerField("idTransaction");
    _display.popup(_receiver.getTransaction(id)); } 
    catch (UnknownTransactionException e) {
        throw new UnknownTransactionKeyException(e.getKey());
      }
  }

}
