package ggc;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable{

    private static final long serialVersionUID = 202110271514L;

    /*Map that contains the units of each ingredient needed to produce the product*/
    private Map<String,Integer> _recipe = new HashMap<String,Integer>();
    private List<String> _ingredients = new ArrayList<>();

    /*add a product to recipe*/
    public void addProduct(Product product,int units)
    {
        if (!_recipe.containsKey(product.getId())) {
            _ingredients.add(product.getId());
            _recipe.put(product.getId(),units); }
    }

    /*delete a product from recipe*/
    public void deleteProduct(Product product)
    {
        _ingredients.remove(product.getId());
        _recipe.remove(product.getId());
    }

    /*update the ingredient units needed*/
    public void updateUnits(Product product, int units)
    {
        _recipe.put(product.getId(),units);
    }

    /*return the ingredient units needed*/
    public int getUnits(Product product)
    {
        return _recipe.get(product.getId());
    }

    public List<String> getAllIngredients() {
        return _ingredients;
    }


    @Override
    public String toString(){
        int counter=0;
        String string = "";
        for (String ingredient: _ingredients) {
        string = string + ingredient + ":" + _recipe.get(ingredient);
        if (counter++<_recipe.size()-1)
            string+="#";
        }
        return string;

    }
}
