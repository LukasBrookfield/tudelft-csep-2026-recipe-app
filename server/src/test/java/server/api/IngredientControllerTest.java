package server.api;

import commons.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static commons.Unit.G;
import static org.junit.jupiter.api.Assertions.*;

public class IngredientControllerTest {
    private TestIngredientRepository repo;
    private Ingredient ingredient1;
    private IngredientController sut;

    @BeforeEach
    public void setup() {
        repo = new TestIngredientRepository();
        sut = new IngredientController(repo);
        ingredient1 = new Ingredient("cucumber", 100.0, G);
    }

    @Test
    public void getAllIngredientsTes() {
        sut.getAllIngredients();
        assertTrue(repo.calledMethods.contains("findAll"));
    }

    @Test
    public void getIngredientByIdTest() {
        sut.add(ingredient1);
        sut.getById((long)0);
        assertTrue(repo.calledMethods.contains("findById"));
    }
    @Test
    public void getIngredientByIdContainsTest() {
        sut.add(ingredient1);
        assertEquals(sut.getById((long)0), ResponseEntity.ok(ingredient1));
    }

    @Test
    public void addIngredientTest() {
        assertEquals(ResponseEntity.ok(ingredient1), sut.add(ingredient1));
    }

}
