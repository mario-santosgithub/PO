package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.UnknownProductException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.UnknownKeyException;

/**
 * Register order.
 */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);
    addStringField("idPartner", Prompt.partnerKey());
    addStringField("idProduct", Prompt.productKey());
    addRealField("price", Prompt.price());
    addIntegerField("amount", Prompt.amount());
    
  }

  @Override
  public final void execute() throws CommandException {
    String partnerId = stringField("idPartner");
    String productId = stringField("idProduct");
    double price = realField("price");
    int amount = integerField("amount");
    try{
     try{
        _receiver.registerAcquisition(partnerId,productId,price,amount);
      } catch (UnknownProductException e) {
        boolean hasRecipe = Form.confirm(Prompt.addRecipe());
        if (hasRecipe) {
          int n_components = Form.requestInteger(Prompt.numberOfComponents());
          double alpha = Form.requestReal(Prompt.alpha());
          String recipe="";
          for(int i=0; i<n_components; i++)
          {
              String componentId = Form.requestString(Prompt.productKey());
              int componentAmount = Form.requestInteger(Prompt.amount());
              recipe+=componentId+":"+componentAmount;
              if (i!=n_components)
                recipe+="#";
          }
          _receiver.registerAcquisitionNewProduct(partnerId,productId,price,amount,alpha,recipe);
        }
        _receiver.registerAcquisitionNewProduct(partnerId,productId,price,amount);
    }
  }
  catch (UnknownKeyException e) {throw new UnknownPartnerKeyException (e.getKey());}
  catch(UnknownProductException e) {throw new UnknownProductKeyException(e.getId());}
}
}
