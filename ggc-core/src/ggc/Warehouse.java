package ggc;

import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.List;

import ggc.exceptions.BadEntryException;
import ggc.exceptions.InvalidDaysDateException;
import ggc.exceptions.DuplicateKeyException;
import ggc.exceptions.UnknownKeyException;
import ggc.exceptions.UnknownProductException;
import ggc.exceptions.UnknownTransactionException;
import ggc.exceptions.ProductWithoutBatchesException;
import ggc.exceptions.UnavailableProductAmountException;
import ggc.exceptions.NotPossibleFabricateException;


/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  /*Partners Map (Keys are Upper Case)*/
  private Map<String, Partner> _partners = new TreeMap<>();

  /*Products Map*/
  private Map<String,Product> _products= new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  private Map<String,Derivative> _derivatives = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  /*Warehouse date*/
  private int _date = 0;

  private double _availableBalance=0; /*real*/

  private double _accountingBalance=0;

  private int _nextTransactionId=0;

  private Map<Integer,Sale> _sales= new TreeMap<>();
  private Map<Integer,Acquisition> _acquisitions= new TreeMap<>();
  private Map<Integer,Breakdown> _breakdowns= new TreeMap<>();

  /**
  *@return the actual date
  */
  public int getDate() { return _date; }

  /**
  *@param date date to set
  */
  public void setDate(int date) {_date=date;}

  /**
   * 
   * @return the next Transaction's Id
   */
  public int getNextTransactionId() {return _nextTransactionId;}

  /**
   * 
   * @param id id to set
   */
  public void setNextTransactionId(int id) {_nextTransactionId=id;} 

  /**
   * 
   * @return the available Balance
   */
  public double getAvailableBalance() {return _availableBalance;}

  /**
   * 
   * @param value available Balance to set
   */
  public void setAvailableBalance (double value) {_availableBalance=value;}

  /**
   * calculates the accounting value 
   * @return the accounting Balance
   */
  public double getAccountingBalance() { 
    _accountingBalance=_availableBalance;
      for (Sale sale: sales()) {
      if (!sale.paid()){
        sale.setPaidValue(calculateValueToPay(sale)); 
        _accountingBalance+=sale.getPaidValue();} }
      return _accountingBalance;
    }

  /**
   * 
   * @param value accounting balance to set
   */
  public void setAccountingBalance(double value) {_accountingBalance=value;}


  /**
  *
  *@param days days to advance the date
  *@throws InvalidDaysDateException
  */
  public void advanceDate(int days) throws InvalidDaysDateException{
    if (days<=0)
      throw new InvalidDaysDateException(days);
      _date+=days;
  }

  /**
  *@param id id Partner
  *@param name name Partner
  *@param address address Partner
  *@throws DuplicateKeyException
  */
  public void registerPartner(String id, String name, String address) throws DuplicateKeyException {
    String idProcessed = id.toUpperCase();
    if (_partners.containsKey(idProcessed)) {
      throw new DuplicateKeyException(id);
    }
    Partner partner = new Partner(id, name, address);
    _partners.put(idProcessed, partner);
    for(Product  p: products()) {
      p.registerObserver(partner);}
  }



  /**
  *register a simple product
  *
  *@param id id Product
  *@param units units in stock 
  */
  public void registerProduct(String id, int units) {
    Product product = new Product(id, units);
    _products.put(product.getId(), product);
    for(Partner  p: partners()) {
      product.registerObserver(p);}
  }

  /**
  *register a derivative product
  *
  *@param id id Product
  *@param units units in stock
  *@param escalation Derivative escalation 
  */
  public void registerProduct(String id, int units, double escalation, String recipe) {
    Derivative product = new Derivative(id, units,escalation);
    for (Partner partner: partners())
      product.registerObserver(partner);
    try{
    String[] ingredients = recipe.split("#");
      for (String ingredient: ingredients)
      {
        String[] elements= ingredient.split(":");
        Product product_recipe=getProduct(elements[0]);
        product.addProductRecipe(product_recipe,Integer.parseInt(elements[1]));
      } }
    catch (UnknownProductException e) {e.printStackTrace();}
    _products.put(id, product);
    _derivatives.put(id,product);
  }

  /**
  *register a simple product batch
  *
  *@param idProduct Product's id 
  *@param idPartner Partner's id 
  *@param price Batch's price
  *@param stock units in stock
  */
  public void registerBatch (String idProduct, String idPartner, double price, int stock)
  {
    Product product=null;
    try{
    product=getProduct(idProduct);
    product.updateStock(stock);
    }
    catch (UnknownProductException e1)
    {
      try{
      registerProduct(idProduct,stock);
      product=getProduct(idProduct);}
      catch (UnknownProductException e2) {e2.printStackTrace();}
    }
    try{
    product.addBatch(getPartner(idPartner),stock,price);}
    catch (UnknownKeyException e) {e.printStackTrace();}
  }

  /**
  *register a derivative product batch
  *
  *@param idProduct Product's id 
  *@param idPartner Partner's id 
  *@param price Batch's price
  *@param stock units in stock
  *@param escalation Derivative's escalation
  *@param recipe Derivative's recipe
  */
  public void registerBatch (String idProduct, String idPartner, double price, int stock, double escalation, String recipe)
  {
    Derivative product=null;
    try{
    product = getDerivative(idProduct);
    product.updateStock(stock); }
    catch (UnknownProductException e1)/*product doesn't exists yet*/
    {
      try{
      registerProduct(idProduct,stock,escalation,recipe);
      product = getDerivative(idProduct); }
      catch (UnknownProductException e2) {e2.printStackTrace();}
    }
    try{
    product.addBatch(getPartner(idPartner),stock,price);}
    catch (UnknownKeyException e) {e.printStackTrace();}
    
  }

  /**
   * 
   * @param acquisition acquisition to add
   */
  public void addAcquisition (Acquisition acquisition) 
  {
    _acquisitions.put(acquisition.getId(),acquisition);
  }

  /**
   * 
   * @param Id Acquisition's Id to get
   * @return the Acquisition with the requested Id
   */
  public Acquisition getAcquisition (int Id) 
  {
    return _acquisitions.get(Id);
  }

  /**
   * 
   * @param sale Sale to add
   */
  public void addSale (Sale sale) 
  {
    _sales.put(sale.getId(),sale);
  }

  /**
   * 
   * @param Id Sale's Id to get
   * @return the Sale with the requested Id
   */
  public Sale getSale(int Id) 
  {
    return _sales.get(Id);
  }

  /**
   * 
   * @param breakdown Breakdown to add
   */
  public void addBreakdown (Breakdown breakdown) 
  {
    _breakdowns.put(breakdown.getId(),breakdown);
  }

  /**
   * 
   * @param Id Breakdown's Id to get
   * @return the Breakdown with the requested Id
   */
  public Breakdown getBreakdown (int Id) 
  {
    return _breakdowns.get(Id);
  }

  /**
   * 
   * @param id the Transaction's Id 
   * @return the Transaction with the requested Id
   * @throws UnknownTransactionException
   */
  public Transaction getTransaction(int id) throws UnknownTransactionException{
      Transaction transaction=null;
      if (_acquisitions.containsKey(id))
        return getAcquisition(id);
      else if (_sales.containsKey(id)) {
        Sale sale = getSale(id);
        if (!sale.paid())
          sale.setPaidValue(calculateValueToPay(sale)); 
        return sale;
        }
      else if (_breakdowns.containsKey(id))
        return getBreakdown(id);
      else
        throw new UnknownTransactionException(id);
  }

  /**
   * 
   * @param id Partner's Id
   * @return Partner's acquisitions
   * @throws UnknownKeyException
   */
  public Collection getPartnerAcquisitions(String id) throws UnknownKeyException {
    Partner partner = getPartner(id);
    return partner.getAquisitions();
  }

  /**
   * 
   * @param id Partner's Id
   * @return Partner's Sales
   * @throws UnknownKeyException
   */
  public Collection getPartnerSales(String id) throws UnknownKeyException {
    Partner partner = getPartner(id);
    List<Transaction> transactions = new ArrayList<>();
    for (Sale sale: partner.getSales()) {
      if (!sale.paid())
          sale.setPaidValue(calculateValueToPay(sale)); 
      transactions.add(sale);
    }
    transactions.addAll(partner.getBreakdowns());
    transactions.sort(Transaction.TRANSACTION_COMPARATOR);

    return transactions;
  }

  /**
   * function that calculates and receives a Sale Payment
   * @param transactionId Transaction's Id to pay
   * @throws UnknownTransactionException
   */
  public void receivePayment(int transactionId) throws UnknownTransactionException{
    Transaction transaction = getTransaction(transactionId);
    if (_sales.containsKey(transactionId)) {
      Sale sale=getSale(transactionId);
      int date=_date;
      int deadline= sale.getPaymentDeadline();
      if (!sale.paid()) {
        try{
        Partner partner = getPartner(sale.getPartnerId());
        Product product = getProduct(sale.getProductId());
        sale.setPaidValue(calculateValueToPay(sale)); 
        _availableBalance+=sale.getPaidValue();
        partner.updatePaidValue(sale.getPaidValue());
        sale.setPaid();
        sale.setPaymentDate(date);
      if ((deadline - date)>=product.getN())
        partner.addPoints(sale.getPaidValue());
      if (0<=(deadline-date) && (deadline-date)<product.getN())
        partner.addPoints(sale.getPaidValue());
      if (0<(date-deadline) && (date-deadline)<product.getN())
        partner.removePoints(date-deadline);
      if (date - deadline>product.getN())
        partner.removePoints(date-deadline);
      }
      catch (UnknownKeyException e) {e.printStackTrace();}
      catch (UnknownProductException e) {e.printStackTrace();}
          
    }
  }
  }

  /**
   * 
   * @param partnerId Acquisition's partnerId  
   * @param productId Acquisition's productId  
   * @param price Acquisition's price  
   * @param amount  Acquisition's amount
   * @throws UnknownProductException
   * @throws UnknownKeyException
   */
  public void registerAcquisition(String partnerId, String productId,double price,int amount) throws UnknownProductException,UnknownKeyException{
      Product product = getProduct(productId);
      Partner partner = getPartner(partnerId);
      double productMinPrice = 0;
      try {
      productMinPrice = product.getMinPrice(); }
      catch (ProductWithoutBatchesException e) {productMinPrice = product.getMaxPrice(); }
      Acquisition a= new Acquisition(_nextTransactionId++,partnerId,productId, amount, getDate(), price*amount);
      partner.updateAcquisitionValue(price*amount);
      product.addBatch(partner,amount, price);
      addAcquisition(a);
      _availableBalance-=(price*amount);
      product.updateStock(amount,price);
      partner.addAcquisition(a);
      if (price<productMinPrice) { 
          product.notifyObservers(new Bargain(product.getId(),price));} 
  }

  /**
   * 
   * @param partnerId Sale's partnerId
   * @param paymentDeadline Sale's payment deadline
   * @param productId Sale's productId
   * @param amount Sale's amount
   * @throws UnknownProductException
   * @throws UnknownKeyException
   * @throws UnavailableProductAmountException
   */
  public void registerSale(String partnerId, int paymentDeadline, String productId, int amount) throws UnknownProductException,UnknownKeyException,UnavailableProductAmountException {
      double price=0;
      Partner partner = getPartner(partnerId);
      Product product = getProduct(productId);
      int lacking=amount;
      if (lacking>product.getStockTotal()){
        try {price=fabricate(amount-product.getStockTotal(),product);
            lacking-=(amount-product.getStockTotal());}
        catch (UnknownProductException e) {e.printStackTrace();}
        catch(NotPossibleFabricateException e) {throw new UnavailableProductAmountException(productId,amount,product.getStockTotal());}
      }
      price+=selectBatches(product,lacking);
      Sale sale= new Sale(_nextTransactionId++,partnerId,productId,amount,price,paymentDeadline,_date);
      addSale(sale);
      partner.addSale(sale);
      partner.updateSalesValue(price);
      }


  /**
   * 
   * @param amount amount to fabricate
   * @param product product to fabricate
   * @return the fabricate's price
   * @throws NotPossibleFabricateException
   * @throws UnavailableProductAmountException
   * @throws UnknownProductException
   */
  public double fabricate(int amount,Product product) throws NotPossibleFabricateException,UnavailableProductAmountException,UnknownProductException{
    if (!_derivatives.containsKey(product.getId()))
      throw new NotPossibleFabricateException();
    Derivative derivative=getDerivative(product.getId());
    double price=0;
    double alpha=derivative.getEscalation();
    Recipe recipe=derivative.getRecipe();
    for (String i: recipe.getAllIngredients()) {
      Product ingredient = getProduct(i);
      int lacking=(amount*recipe.getUnits(ingredient));
      if (lacking>ingredient.getStockTotal())
        throw new UnavailableProductAmountException(ingredient.getId(),lacking,ingredient.getStockTotal());
    }
    for (String ing: recipe.getAllIngredients()) {
      Product ingredient = getProduct(ing);
      int lacking=(amount*recipe.getUnits(ingredient));
      price+=selectBatches(ingredient,lacking);}
    price=(1+alpha)*price;
    if ((price/amount)>product.getMaxPrice()) {
        product.setMaxPrice(price/amount);}
    return price;
  }

  /**
   * 
   * @param partnerId Breakdown's partnerId
   * @param productId Breakdown's productId
   * @param amount Breakdown's amount
   * @throws UnknownProductException
   * @throws UnknownKeyException
   * @throws UnavailableProductAmountException
   */
  public void registerBreakdown (String partnerId, String productId, int amount) throws UnknownProductException,UnknownKeyException,UnavailableProductAmountException{
    Partner partner = getPartner(partnerId);
    Product product = getProduct(productId);
    String recipe = "";
    double price = 0;
    int lacking=amount;
    if (_derivatives.containsKey(productId)) {
      Derivative derivative=null;
      derivative = getDerivative(productId);
      if (lacking>derivative.getStockTotal())
          throw new UnavailableProductAmountException(productId,lacking,derivative.getStockTotal());
      price+=selectBatches(derivative,lacking);
      double batchPrice=0;
      int units=0;
      int counter=0;
      for (String i: derivative.getAllIngredients()) {
        try{
          Product ingredient = getProduct(i);
          try{
            batchPrice=ingredient.getMinPrice();
          } catch (ProductWithoutBatchesException e) {batchPrice=ingredient.getMaxPrice();}
          ingredient.updateStock(amount*derivative.getUnitsRecipe(ingredient));
          ingredient.addBatch(partner,amount*derivative.getUnitsRecipe(ingredient),batchPrice);
          price-=(amount*derivative.getUnitsRecipe(ingredient)*batchPrice);
          recipe+=ingredient.getId()+":"+(derivative.getUnitsRecipe(ingredient)*amount) + ":" + 
                  (int)Math.round((derivative.getUnitsRecipe(ingredient)*batchPrice*amount));
          if (counter++<derivative.getAllIngredients().size()-1)
            recipe+="#";
          
        } catch (UnknownProductException e) {e.printStackTrace();}
      }
      Breakdown b =new Breakdown(_nextTransactionId++,partnerId,productId,amount,_date,price,recipe);
      addBreakdown(b);
      partner.addBreakdown(b);
      if (price>0)
        partner.addPoints(price);
      _availableBalance+=b.getPaidValue();
    }
  }

  /**
   * 
   * @param product to select batches
   * @param lacking units to select
   * @return
   */
  public double selectBatches(Product product, int lacking) {
    double price = 0;
     List<Batch> _batches = product.getBatchesSortedByPrice();
          for (int i=0; lacking>0; i++) {
              Batch b=_batches.get(i);
              if (b.getUnits()>lacking) {
                  b.updateUnits(-lacking);
                  product.updateStock(-lacking);
                  price+=(lacking*b.getPrice());
                  lacking=0; }
              else {
                  lacking-=b.getUnits();
                  price+=(b.getUnits()*b.getPrice());
                  product.updateStock(-b.getUnits());
                  product.removeBatch(b);}
              }
    return price;
  }

  /**
   * to registe a acquisition with a new product
   * @param partnerId Acquisition's partnerId
   * @param productId Acquisition's productId
   * @param price Acquisiton's price
   * @param amount Acquisition's amount
   * @throws UnknownKeyException
   */
  public void registerAcquisitionNewProduct(String partnerId, String productId,double price,int amount) throws UnknownKeyException {
      try{
      registerProduct(productId,0);
      registerAcquisition(partnerId,productId,price,amount); }
      catch (UnknownProductException e) {e.printStackTrace();}
  }

  /**
   * to registe a acquisition with a new derivative
   * @param partnerId Acquisition's partnerId
   * @param productId Acquisition's productId
   * @param price Acquisiton's price
   * @param amount Acquisition's amount
   * @param escalation Derivative's escalation
   * @param recipe Derivative's recipe
   * @throws UnknownKeyException
   * @throws UnknownProductException
   */
  public void registerAcquisitionNewProduct(String partnerId, String productId,double price,int amount, 
                                            double escalation, String recipe) throws UnknownKeyException, UnknownProductException{
      registerProduct(productId,0,escalation,recipe);
      registerAcquisition(partnerId,productId,price,amount); 
  }


  /**
   * 
   * @param sale sale to calculate Price
   * @return the actual value to pay
   */
  public double calculateValueToPay (Sale sale) {
    double valueToPay=0;
    try {
    Partner partner = getPartner(sale.getPartnerId());
    Product product = getProduct(sale.getProductId());
    sale.calculateValueToPay(_date,partner,product);
    valueToPay=sale.getPaidValue();
    }
    catch (UnknownKeyException e) {e.printStackTrace();}
    catch (UnknownProductException e) {e.printStackTrace();}
    return valueToPay;
  }






  /**
  *@return a collection with all partners
  */
  public Collection<Partner> partners() {
    return Collections.unmodifiableCollection(_partners.values());
  }

  /**
   * 
   * @return a collection with all acquisitions
   */
  public Collection<Acquisition> acquisitions() {
    return Collections.unmodifiableCollection(_acquisitions.values());
  }

  /**
   * 
   * @return a collection with all sales
   */
  public Collection<Sale> sales() {
    return Collections.unmodifiableCollection(_sales.values());
  }
  
  /**
   * 
   * @return a collection with all breakdowns
   */
  public Collection<Breakdown> breakdowns() {
    return Collections.unmodifiableCollection(_breakdowns.values());
  }
  

  /**
  *@param id Partner's id to search 
  *@return Partner with the requested id 
  *@throws UnknownKeyException
  */
  public Partner getPartner(String id) throws UnknownKeyException {
    String idProcessed = id.toUpperCase();
    if (!_partners.containsKey(idProcessed)) {
      throw new UnknownKeyException(id);
    }
    return _partners.get(idProcessed);
  }

  /**
  * @param id Product's id to search 
  * @return Product with the requested id 
  * @throws UnknownProductException
  */
  public Product getProduct(String id) throws UnknownProductException{
    if (!_products.containsKey(id)) {
      throw new UnknownProductException(id);
    }
    return _products.get(id);
  }

  /**
   * 
   * @param id Derivative's id to search
   * @return Derivative with the requested id
   * @throws UnknownProductException
   */
  public Derivative getDerivative(String id) throws UnknownProductException{
    if (!_derivatives.containsKey(id)) {
      throw new UnknownProductException(id);
    }
    return _derivatives.get(id);
  }

  

  /**
  *@return a collection with all products
  */
  public Collection<Product> products() {
    return Collections.unmodifiableCollection(_products.values());
  }

  /**
  *@return a collection with all batches
  */
  public Collection<Batch> allBatches() {
        List<Batch> result = new ArrayList<>();
        for(Map.Entry<String,Product> entry : _products.entrySet()) 
            result.addAll(entry.getValue().batches());
        return result;
        }

  /**
   * 
   * @param partnerId 
   * @return a collection with all batches by the requested partner
   * @throws UnknownKeyException
   */
  public Collection<Batch> batches_byPartner(String partnerId) throws UnknownKeyException{
      List<Batch> result = new ArrayList<>();
        for(Batch b: allBatches()){
            if (b.getPartner().equals(getPartner(partnerId)))
              result.add(b);
              }
        return result;
        }

  /**
   * 
   * @param productId
   * @return a collection with all batches by the requested product
   * @throws UnknownProductException
   */
  public Collection<Batch> batches_byProduct(String productId) throws UnknownProductException {
      List<Batch> result = new ArrayList<>();
      Product p=getProduct(productId);
        for(Batch b: allBatches()){
            if (b.getProduct().equals(p))
              result.add(b);}
        return result;
        }

  /**
   * 
   * @param limit 
   * @return all batches whose price is under limit
   */
  public Collection<Batch> batchesUnderLimit(double limit){
      List<Batch> result = new ArrayList<>();
        for(Batch b: allBatches()){
            if (b.getPrice()<limit)
              result.add(b);
              }
        return result;
        }  

  /**
   * 
   * @param partnerId partner's id to notify/not notify
   * @param productId 
   * @throws UnknownProductException
   * @throws UnknownKeyException
   */
  public void toggleProductNotifications(String partnerId, String productId) throws UnknownProductException, UnknownKeyException {
      Product product = getProduct(productId);
      Partner partner = getPartner(partnerId);

      product.toogleNotifications(partner);
  }

  /**
   * 
   * @param partnerId 
   * @return a collection with all partner's notifications
   * @throws UnknownKeyException
   */
  public Collection<Notification> getPartnerNotifications(String partnerId) throws UnknownKeyException {
    Partner partner = getPartner(partnerId);
    List<Notification> notifications = new ArrayList<>();
    notifications.addAll(partner.getNotifications());
    partner.clearNotifications();
    return notifications;
  }

  /**
   * 
   * @param partnerId
   * @return a collection with all transactions paid by a partner
   * @throws UnknownKeyException
   */
  public Collection<Transaction> getPaidTransactionsByPartner(String partnerId) throws UnknownKeyException {
    Partner partner = getPartner(partnerId);
    List<Transaction> paidTransactions = new ArrayList<>();
    for (Sale sale: partner.getSales()) {
      if (sale.paid()) {
        paidTransactions.add(sale);
      }
    } 
    paidTransactions.addAll(partner.getBreakdowns());
    paidTransactions.sort(Transaction.TRANSACTION_COMPARATOR);
    return paidTransactions;
  }

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   * @throws DuplicateKeyException
   * @throws FileNotFoundException
   */
  void importFile(String txtfile) throws IOException, BadEntryException,DuplicateKeyException, FileNotFoundException  {
    try (BufferedReader in = new BufferedReader(new FileReader(txtfile))) {
      String s;
      while ((s = in.readLine()) != null) {
        String line = new String(s.getBytes(), "UTF-8");
        if (line.charAt(0) == '#')
          continue;

        String[] fields = line.split("\\|");
        switch (fields[0]) {
        case "PARTNER" -> registerPartner(fields[1],fields[2],fields[3]);
        case "BATCH_S" -> registerBatch(fields[1],fields[2],Integer.parseInt(fields[3]),Integer.parseInt(fields[4]));
        case "BATCH_M" -> registerBatch(fields[1],fields[2],Integer.parseInt(fields[3]),Integer.parseInt(fields[4]),Double.parseDouble(fields[5]),fields[6]);
        default -> throw new BadEntryException(fields[0]);
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (DuplicateKeyException e) {
      e.printStackTrace();
    } catch (BadEntryException e) {
      e.printStackTrace();
    }
  }

}
