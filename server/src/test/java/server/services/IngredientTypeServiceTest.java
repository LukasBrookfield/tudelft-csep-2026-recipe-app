package server.services;

import commons.IngredientType;
import commons.Nutrition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.TestIngredientTypeRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class IngredientTypeServiceTest {
    private TestIngredientTypeRepository ingredientTypeRepo;

    private IngredientTypeService ingredientTypeService;

    IngredientType tomato;

    @BeforeEach
    public void setUp() {
        ingredientTypeRepo = new TestIngredientTypeRepository();

        ingredientTypeService = new IngredientTypeService(ingredientTypeRepo);

        tomato = new IngredientType("Tomato",
                new Nutrition(1.5, 6.2, 3.7),
                new ArrayList<>(),
                1.0);
    }

    @Test
    public void validIngredientTypeTest() {
        assertTrue(ingredientTypeService.validateIngredientType(tomato));
    }

    @Test
    public void invalidNameTest() {
        tomato.name = " ";
        assertFalse(ingredientTypeService.validateIngredientType(tomato));
    }

    @Test
    public void invalidDensityTest() {
        tomato.density = -0.4;
        assertFalse(ingredientTypeService.validateIngredientType(tomato));
    }

    @Test
    public void invalidNutritionTest() {
        tomato.nutrition.protein = -0.5;
        assertFalse(ingredientTypeService.validateIngredientType(tomato));
    }

    @Test
    public void updateIngredientTypeTest() {
        ingredientTypeRepo.save(tomato);

        long ingredientTypeID = tomato.id;
        long invalidID = -1;
        assertTrue(ingredientTypeService.validateUpdatedIngredientType(
                ingredientTypeID, tomato));
        assertFalse(ingredientTypeService.validateUpdatedIngredientType(
                invalidID, tomato));

        tomato.density = -0.5;
        assertFalse(ingredientTypeService.validateUpdatedIngredientType(
                ingredientTypeID, tomato));
    }

    @Test
    public void transferFieldsTest() {
        IngredientType lettuce = new IngredientType("Lettuce",
                new Nutrition(7.1, null, 1.0),
                new ArrayList<>(),
                2.5);
        ingredientTypeService.transferFields(tomato, lettuce);
        assertEquals(tomato, lettuce);
    }
}
