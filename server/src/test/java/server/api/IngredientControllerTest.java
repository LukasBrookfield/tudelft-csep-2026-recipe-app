package server.api;

import commons.Ingredient;
import commons.IngredientType;
import commons.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.services.IngredientService;

import java.util.ArrayList;
import java.util.List;
import static commons.Unit.G;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class IngredientControllerTest {
    private Ingredient ingredient1;
    private Ingredient ingredient2;
    private Recipe recipe1;
    private Recipe recipe2;

    private TestIngredientRepository repo;
    private IngredientController sut;
    private IngredientService ingredientService;

    @BeforeEach
    public void setup() {
        // for testing with TestIngredientRepository
        repo = new TestIngredientRepository();
        ingredientService = new IngredientService(repo);
        sut = new IngredientController(repo, ingredientService);

        recipe1 = new Recipe("cucumber salad",
                new ArrayList<>(),
                new ArrayList<>(List.of("add cucumbers")),
                2);
        recipe2 = new Recipe("cucumber with salt",
                new ArrayList<>(),
                new ArrayList<>(List.of("add cucumbers")),
                3);
        ingredient1 = new Ingredient(
                new IngredientType("cucumber", null, new ArrayList<>(), null), 100.0, G, recipe1);
        ingredient2 = new Ingredient(
                new IngredientType("cucumber", null, new ArrayList<>(), null), 99.0, G, recipe2);
    }

    @Test
    public void getAllIngredientsTest() {
        var ingredients = sut.getAllIngredients();
        assertTrue(repo.calledMethods.contains("findAll"));
    }

    @Test
    public void getIngredientByIdTest() {
        sut.add(ingredient1);
        var ingredient = sut.getById(1).getBody();
        assertEquals(ingredient1, ingredient);
    }

    @Test
    public void getIngredientByIdContainsTest() {
        sut.add(ingredient1);
        assertEquals(sut.getById(1), ResponseEntity.ok(ingredient1));
    }

    @Test
    public void deleteTest() {
        sut.add(ingredient1);
        sut.add(ingredient2);
        sut.delete(ingredient1.id);
        assertFalse(repo.findAll().contains(ingredient1));
    }

    @Test
    public void deleteWithWrongIdTest() {
        sut.add(ingredient1);
        sut.add(new Ingredient(new IngredientType("cucumber", null, null, null), 99.0, G, null));
        var result = sut.delete(3);
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void deleteAlreadyDeletedTest() {
        sut.add(ingredient1);
        sut.add(new Ingredient(new IngredientType("cucumber", null, null, null), 99.0, G, null));
        var result = sut.delete(ingredient1.id);
        var result2 = sut.delete(ingredient1.id);
        assertEquals(OK, result.getStatusCode());
        assertEquals(BAD_REQUEST, result2.getStatusCode());
    }

    @Test
    public void updateTest() {
        sut.add(ingredient1);
        var result = sut.update(ingredient1.id, ingredient2);
        assertEquals(OK, result.getStatusCode());
        assertEquals(repo.findById(ingredient1.id).get(), ingredient2);
    }

    @Test
    public void updateWrongIdTest() {
        sut.add(ingredient1);
        var result = sut.update(2, ingredient2);
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void updateWrongIngredientTest() {
        sut.add(ingredient1);
        Ingredient ingredient = new Ingredient(
                null,
                99.0,
                null,
                null);
        var result = sut.update(ingredient1.id, ingredient);
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void addIngredientTest() {
        assertEquals(ResponseEntity.ok(ingredient1), sut.add(ingredient1));
    }
}
