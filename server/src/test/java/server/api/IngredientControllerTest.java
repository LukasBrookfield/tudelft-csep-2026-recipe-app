package server.api;

import commons.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import server.database.IngredientRepository;
import server.database.RecipeRepository;

import static commons.Unit.G;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest
@Transactional
public class IngredientControllerTest {
    private TestIngredientRepository repo;
    private Ingredient ingredient1;
    private Ingredient ingredient2;
    private IngredientController sut;

    //for testing with SpringBoot
    @Autowired
    @Qualifier("ingredientRepository")
    private IngredientRepository repo2;

    @Autowired
    private RecipeRepository recipeRepository; // must clear dependent table

    private IngredientController sut2;

    @BeforeEach
    public void setup() {
        //for testing with SpringBoot
        repo2.deleteAll();
        recipeRepository.deleteAll();
        sut2 = new IngredientController(repo2);

        //for testing with TestIngredientRepository
        repo = new TestIngredientRepository();
        sut = new IngredientController(repo);

        ingredient1 = new Ingredient("cucumber", 100.0, G);
        ingredient2 = new Ingredient("cucumber", 99.0, G);
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
    public void deleteTest() {
        sut2.add(ingredient1);
        sut2.add(ingredient2);
        sut2.delete(ingredient1.id);
        assertFalse(repo2.findAll().contains(ingredient1));
    }

    @Test
    public void deleteWithWrongIdTest0() {
        sut2.add(ingredient1);
        sut2.add(new Ingredient("cucumber", 99.0, G));
        var result = sut2.delete(3);
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void deleteAlreadyDeletedTest0() {
        sut2.add(ingredient1);
        sut2.add(new Ingredient("cucumber", 99.0, G));
        var result = sut2.delete(ingredient1.id);
        var result2 = sut2.delete(ingredient1.id);
        assertEquals(OK, result.getStatusCode());
        assertEquals(BAD_REQUEST, result2.getStatusCode());
    }

    @Test
    public void updateTest() {
        sut2.add(ingredient1);
        Ingredient ingredient = new Ingredient("cucumber", 100.0, G);
        var result = sut2.update(ingredient1.id, ingredient);
        assertEquals(OK, result.getStatusCode());
        assertEquals(repo2.findById(ingredient1.id).get(), ingredient);
    }

    @Test
    public void updateWrongIdTest() {
        sut2.add(ingredient1);
        var result = sut2.update(2, ingredient2);
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void updateWrongIngredientTest() {
        sut2.add(ingredient1);
        Ingredient ingredient = new Ingredient("cucumber", 99.0, null);
        var result = sut2.update(ingredient1.id, ingredient);
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void addIngredientTest() {
        assertEquals(ResponseEntity.ok(ingredient1), sut.add(ingredient1));
    }

}
