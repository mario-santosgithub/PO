package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

import ggc.WarehouseManager;
import ggc.exceptions.MissingFileAssociationException;

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Save current state to file under current name (if unnamed, query for name).
 */
class DoSaveFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoSaveFile(WarehouseManager receiver) {
    super(Label.SAVE, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    try{
    _receiver.save();}
    catch (MissingFileAssociationException e1)
    {
      String filename = Form.requestString(Prompt.newSaveAs());
      try{
      _receiver.saveAs(filename);}
      catch (IOException | MissingFileAssociationException e2) {e2.printStackTrace();}
    }
    catch (IOException e1) {e1.printStackTrace();}

  }

}
