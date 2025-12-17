package client.scenes;

import client.utils.ServerUtils;
import commons.IngredientType;

import java.util.ArrayList;
import java.util.List;

public class TestServerUtils extends ServerUtils{

    private final List<IngredientType> ingredientTypes;

    public TestServerUtils() {
        super(null);
        ingredientTypes = new ArrayList<>();
    }

    @Override
    public List<IngredientType> getIngredientTypes() {
        return ingredientTypes;
    }

    @Override
    public IngredientType addIngredientType(IngredientType ingredientType) {
        ingredientTypes.add(ingredientType);
        return ingredientType;
    }
}
