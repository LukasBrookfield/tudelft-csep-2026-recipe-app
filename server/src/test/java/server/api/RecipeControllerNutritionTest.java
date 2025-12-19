package server.api;

import commons.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.services.UnitConversionService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class RecipeControllerNutritionTest {

    private TestRecipeRepository repo;
    private RecipeController controller;

    @BeforeEach
    public void setup(){
        repo = new TestRecipeRepository();
        var messagingTemplate = mock(SimpMessagingTemplate.class);

        var unitConversion = new server.services.UnitConversionService();
        var normalizer = new server.services.QuantityNormalizationService(unitConversion);
        var nutritionService = new server.services.RecipeNutritionService(normalizer);

        controller = new RecipeController(repo, messagingTemplate, nutritionService);
    }

    @Test
    public void getNutrition_badId_returns400() {
        var resp = controller.getNutrition(-1);
        assertEquals(BAD_REQUEST, resp.getStatusCode());
    }

    @Test
    public void getNutrition_existingRecipe_returns200() {
        Nutrition n = new Nutrition(10.0, 0.0, 0.0);
        IngredientType chicken = new IngredientType("Chicken", n, new ArrayList<>(), null);

        Recipe r = new Recipe("Chicken bowl", new ArrayList<>(), new ArrayList<>(List.of("add chicken")), 1);
        r.ingredients.add(new Ingredient(chicken, 100.0, Unit.G, r));
        controller.add(r);

        var resp = controller.getNutrition(1);
        assertEquals(OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody().totalGrams() > 0);

    }
}
