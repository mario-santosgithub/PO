package ggc;


import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Derivative extends Product {
    
    /*Product's recipe*/
    private Recipe _recipe = new Recipe();

    /*Product's escation*/
    private double _escalation;

    public Derivative(String id, int units, double escalation){
            super(id,units);
            _escalation=escalation;
            setN(3);
    }

    public Recipe getRecipe() {return _recipe;}

    public double getEscalation() {return _escalation;}

    /*add a new product to recipe*/
    public void addProductRecipe(Product product,int units) {
        _recipe.addProduct(product,units);}

    /*remove a product of recipe*/
    public void deleteProductRecipe(Product product) {
        _recipe.deleteProduct(product);}

    /*update the amount of a product in the recipe*/
    public void updateUnitsProductRecipe(Product product, int units){
        _recipe.updateUnits(product,units);}

    public int getUnitsRecipe(Product product) {
        return _recipe.getUnits(product);}

    public List<String> getAllIngredients() {
        return _recipe.getAllIngredients();
    }

    @Override
    public String toString(){
        return super.toString() + "|" + _escalation + "|" + _recipe.toString();
    } 
    }

