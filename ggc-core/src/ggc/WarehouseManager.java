package ggc;


import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.Collection;
import java.util.Collections;

import ggc.exceptions.MissingFileAssociationException;
import ggc.exceptions.UnavailableFileException;
import ggc.exceptions.ImportFileException;
import ggc.exceptions.BadEntryException;
import ggc.exceptions.InvalidDaysDateException;
import ggc.exceptions.DuplicateKeyException;
import ggc.exceptions.UnknownKeyException;
import ggc.exceptions.UnknownProductException;
import ggc.exceptions.UnknownTransactionException;
import ggc.exceptions.UnavailableProductAmountException;
                    

/** Façade for access. */
public class WarehouseManager {

  /** Name of file storing current store. */
  private String _filename = "";

  /**var that indicates if file is saved*/
  private boolean _saved = false;

  /** The warehouse itself. */
  private Warehouse _warehouse = new Warehouse();

  /*getters and setters*/
  public String getFilename() {return _filename;} 
  public void setFilename(String filename) {_filename=filename;}

  public int getDate() { return _warehouse.getDate(); }

  public void advanceDate(int days) throws InvalidDaysDateException{
    _warehouse.advanceDate(days);
    notSaved();
  }

  public double availableBalance() {return _warehouse.getAvailableBalance();}
  public double accountingBalance() {return _warehouse.getAccountingBalance();}

  public void notSaved() {_saved=false;}
  public void isSaved() {_saved=true;}

  public void registerPartner(String id, String name, String address) throws DuplicateKeyException {
    _warehouse.registerPartner(id,name,address);
    notSaved();
  }

  public Collection<Partner> partners() {
    return _warehouse.partners();
  }

  public Partner getPartner(String id) throws UnknownKeyException{
    return _warehouse.getPartner(id);
  }

  public Transaction getTransaction(int id) throws UnknownTransactionException {
    return _warehouse.getTransaction(id);
  }

  public Collection getPartnerAcquisitions(String id) throws UnknownKeyException {
    return _warehouse.getPartnerAcquisitions(id);
  }

  public void toggleProductNotifications(String partnerId, String productId) throws UnknownProductException, UnknownKeyException {
    _warehouse.toggleProductNotifications(partnerId, productId);
    notSaved();
  }

  public void receivePayment(int transactionId) throws UnknownTransactionException{
    _warehouse.receivePayment(transactionId);
    notSaved();
  }

  public void registerBreakdown (String partnerId, String productId, int amount) throws UnknownProductException,UnknownKeyException,UnavailableProductAmountException{
    _warehouse.registerBreakdown(partnerId,productId,amount);
    notSaved();
  }

  public Collection<Notification> getPartnerNotifications(String partnerId) throws UnknownKeyException {
    return _warehouse.getPartnerNotifications(partnerId);
  }

  public Collection<Product> products() {
    return _warehouse.products();
  }

  public Collection<Batch> batches() {
    return _warehouse.allBatches();
  }

  public Collection<Batch> batchesByPartner(String partnerId) throws UnknownKeyException{
    return _warehouse.batches_byPartner(partnerId);
  }

  public Collection<Batch> batchesByProduct(String productId) throws UnknownProductException {
    return _warehouse.batches_byProduct(productId);
  }

  public Collection<Batch> batchesUnderLimit(double limit){
    return _warehouse.batchesUnderLimit(limit);
  }

  public void registerAcquisition(String partnerId, String productId,double price,int amount) throws UnknownProductException,UnknownKeyException {
     _warehouse.registerAcquisition(partnerId,productId,price,amount);
     notSaved();
  }

  public void registerAcquisitionNewProduct(String partnerId, String productId,double price,int amount) throws UnknownKeyException {
    _warehouse.registerAcquisitionNewProduct(partnerId,productId,price,amount);
    notSaved();
  }

  public void registerAcquisitionNewProduct(String partnerId, String productId,double price,int amount, double alpha, String recipe) throws UnknownKeyException,UnknownProductException{
    _warehouse.registerAcquisitionNewProduct(partnerId,productId,price,amount,alpha,recipe);
    notSaved();
  }
  
  public void registerSale(String partnerId, int paymentDeadline, String productId, int amount) throws UnknownProductException,UnknownKeyException,UnavailableProductAmountException {
     _warehouse.registerSale(partnerId,paymentDeadline,productId,amount);
     notSaved();
  }

  public Collection<Transaction> getPaidTransactionsByPartner(String partnerId) throws UnknownKeyException{
    return _warehouse.getPaidTransactionsByPartner(partnerId);
  }

   public Collection getPartnerSales(String id) throws UnknownKeyException {
    return _warehouse.getPartnerSales(id); }

  /**
   * @@throws IOException
   * @@throws FileNotFoundException
   * @@throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
    if (_saved==false){
      if (_filename.equals(""))
        throw new MissingFileAssociationException();
      try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)))){
        oos.writeObject(_warehouse);
        oos.close();
      isSaved();
    } }
  }

  /**
   * @@param filename
   * @@throws MissingFileAssociationException
   * @@throws IOException
   * @@throws FileNotFoundException
   */
  public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
    _filename = filename;
    save();
  }

  /**
   * @@param filename
   * @@throws UnavailableFileException
   */
  public void load(String filename) throws UnavailableFileException {
     try {
      _filename=filename;
      ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
      _warehouse = (Warehouse)ois.readObject();
      ois.close();
      notSaved();
    }
    catch (IOException | ClassNotFoundException e) 
      { throw new UnavailableFileException(filename); }
  }
  

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException {
    try {
	    _warehouse.importFile(textfile);
      notSaved();
    } catch (IOException | BadEntryException | DuplicateKeyException e) {
	    throw new ImportFileException(textfile);
    }
  }

}
