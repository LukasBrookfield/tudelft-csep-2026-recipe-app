package server.services;

import commons.Ingredient;
import commons.IngredientType;
import commons.Nutrition;
import commons.Unit;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.TestIngredientRepository;
import server.api.TestIngredientTypeRepository;
import server.database.IngredientRepository;

import static commons.Unit.G;
import static commons.Unit.TO_TASTE;
import static org.junit.jupiter.api.Assertions.*;

public class IngredientServiceTest {
    private TestIngredientRepository ingredientRepo;
    private TestIngredientTypeRepository ingredientTypeRepo;

    private IngredientService ingredientService;
    private IngredientTypeService ingredientTypeService;

    Ingredient tomato;

    @BeforeEach
    public void setUp() {
        ingredientRepo = new TestIngredientRepository();
        ingredientTypeRepo = new TestIngredientTypeRepository();

        ingredientTypeService = new IngredientTypeService(ingredientTypeRepo);
        ingredientService = new IngredientService(ingredientRepo, ingredientTypeService);

        tomato = new Ingredient(new IngredientType("Tomato",
                new Nutrition(1.0, 5.0, null), null, 1.0),
                50.0, G, null);
    }

    @Test
    public void validIngredientTest() {
        assertTrue(ingredientService.validateIngredient(tomato));
    }

    @Test
    public void invalidIngredientTypeTest() {
        tomato.ingredientType.nutrition.protein = -0.5;
        assertFalse(ingredientService.validateIngredient(tomato));
    }

    @Test
    public void nullAmountTest() {
        tomato.amount = null;
        assertFalse(ingredientService.validateIngredient(tomato));

        tomato.unit = TO_TASTE;
        assertTrue(ingredientService.validateIngredient(tomato));
        tomato.amount = 25.0;
        assertFalse(ingredientService.validateIngredient(tomato));
    }

    @Test
    public void nullUnitTest() {
        tomato.unit = null;
        assertFalse(ingredientService.validateIngredient(tomato));
    }

    @Test
    public void updateIngredientTest() {
        ingredientRepo.save(tomato);

        assertTrue(ingredientService.validateUpdatedIngredient(1, tomato));
        assertFalse(ingredientService.validateUpdatedIngredient(-1, tomato));

        tomato.ingredientType.nutrition.protein = -1.6;
        assertFalse(ingredientService.validateUpdatedIngredient(1, tomato));
    }

    @Test
    public void transferFieldsTest() {
        Ingredient lettuce = new Ingredient(new IngredientType("Lettuce",
                new Nutrition(5.1, 1.2, 4.5), null,
                5.1), null, TO_TASTE, null);

        ingredientService.transferFields(tomato, lettuce);
        assertEquals(tomato, lettuce);
    }
}
