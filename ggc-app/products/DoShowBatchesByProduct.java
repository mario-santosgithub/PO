package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.UnknownProductException;

/**
 * Show all products.
 */
class DoShowBatchesByProduct extends Command<WarehouseManager> {

  DoShowBatchesByProduct(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_BY_PRODUCT, receiver);
    addStringField("idProduct", Prompt.productKey());
  }

  @Override
  public final void execute() throws CommandException {
    try{
      String id = stringField("idProduct");
      _display.popup(_receiver.batchesByProduct(id)); 
      } catch (UnknownProductException e) {
        throw new UnknownProductKeyException (e.getId());
    }
  }

}
