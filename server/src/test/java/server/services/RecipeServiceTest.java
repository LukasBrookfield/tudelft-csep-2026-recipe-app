package server.services;

import commons.Ingredient;
import commons.IngredientType;
import commons.Nutrition;
import commons.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.TestIngredientRepository;
import server.api.TestIngredientTypeRepository;
import server.api.TestRecipeRepository;

import java.util.ArrayList;
import java.util.List;

import static commons.Unit.G;
import static commons.Unit.TO_TASTE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class RecipeServiceTest {
    private TestRecipeRepository recipeRepo;
    private TestIngredientRepository ingredientRepo;
    private TestIngredientTypeRepository ingredientTypeRepo;

    private RecipeService recipeService;
    private IngredientService ingredientService;
    private IngredientTypeService ingredientTypeService;

    Recipe salad;
    Ingredient tomato;
    Ingredient lettuce;

    @BeforeEach
    public void setUp() {
        recipeRepo = new TestRecipeRepository();
        ingredientRepo = new TestIngredientRepository();
        ingredientTypeRepo = new TestIngredientTypeRepository();

        ingredientTypeService = new IngredientTypeService(ingredientTypeRepo);
        ingredientService = new IngredientService(ingredientRepo, ingredientTypeService);
        recipeService = new RecipeService(recipeRepo, ingredientService);

        tomato = new Ingredient(new IngredientType("Tomato",
                new Nutrition(1.0, 5.0, null), null, 1.0),
                50.0, G, null);
        lettuce = new Ingredient(new IngredientType("Lettuce",
                new Nutrition(5.1, 1.2, 4.5), null,
                5.1), null, TO_TASTE, null);

        salad = new Recipe("Salad",
                new ArrayList<>(List.of(tomato, lettuce)),
                new ArrayList<>(List.of("Do this", "And this")),
                1, "en");
    }

    @Test
    public void invalidNameTest() {
        assertTrue(recipeService.validateRecipe(salad));
        salad.name = "   ";
        assertFalse(recipeService.validateRecipe(salad));
    }

    @Test
    public void nullIngredientsTest() {
        salad.ingredients = null;
        assertFalse(recipeService.validateRecipe(salad));
    }

    @Test
    public void invalidIngredientTest() {
        tomato.unit = null;
        assertFalse(recipeService.validateRecipe(salad));
    }

    @Test
    public void updateRecipeTest() {
        recipeRepo.save(salad);

        long recipeID = salad.id;
        long nonExistingID = 5;
        assertTrue(recipeService.validateUpdatedRecipe(recipeID, salad));
        assertFalse(recipeService.validateUpdatedRecipe(nonExistingID, salad));

        salad.steps = null;
        assertFalse(recipeService.validateUpdatedRecipe(recipeID, salad));
    }

    @Test
    public void transferFieldsTest() {
        Recipe pizza = new Recipe("Pizza");
        recipeService.transferFields(salad, pizza);
        assertEquals(salad, pizza);
    }
}
