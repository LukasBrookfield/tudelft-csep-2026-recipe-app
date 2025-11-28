package server.api;

import commons.Ingredient;
import commons.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import server.database.IngredientRepository;
import server.database.RecipeRepository;

import java.util.ArrayList;
import java.util.List;
import static commons.Unit.G;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest
@Transactional
public class RecipeControllerTest {

    //For testing with TestRecipeRepository
    private TestRecipeRepository repo;
    private RecipeController sut;
    private Recipe recipe1;
    private Recipe recipe2;

    //for testing with SpringBoot
    @Autowired
    @Qualifier("recipeRepository")
    private RecipeRepository repo2;

    private RecipeController sut2;

    @Autowired
    private IngredientRepository ingredientRepository; // must clear dependent table

    @BeforeEach
    public void setUp()
    {
        //For testing with SpringBoot
        repo2.deleteAll();
        ingredientRepository.deleteAll();
        sut2 = new RecipeController(repo2);

        //For testing with TestRecipeRepository
        repo = new TestRecipeRepository();
        sut = new RecipeController(repo);
        Ingredient ingredient1 = new Ingredient("cucumber", 100.0, G);
        Ingredient ingredient2 = new Ingredient("cucumber", 100.0, G);
        recipe1 = new Recipe("cucumber salad",
                new ArrayList<>(List.of(ingredient1)),
                new ArrayList<>(List.of("add cucumbers")));
        recipe2 = new Recipe("cucumber with salt",
                new ArrayList<>(List.of(ingredient2)),
                new ArrayList<>(List.of("add cucumbers")));

    @Test
    public void getAllRecipesTest(){
        sut.add(recipe1);
        assertEquals(sut.getAllRecipes(), List.of(recipe1));
    }

    @Test
    public void getAllRecipesDatabaseTest(){
        sut.add(recipe1);
        sut.getAllRecipes();
        assertTrue(repo.calledMethods.contains("findAll"));
    }

    @Test
    public void getRecipeByIdTest(){
        sut.add(recipe1);
        assertEquals(sut.getById((long)0),  ResponseEntity.ok(recipe1));
    }

    @Test
    public void nullAddRecipeTestUsedDatabase(){
        var r = new Recipe("name", null, null);
        assertEquals(ResponseEntity.badRequest().build(), sut.add(r));
    }

    @Test
    public void addRecipeTestOkResponse(){
        assertEquals(sut.add(recipe1), ResponseEntity.ok(recipe1));
    }

    @Test
    public void addRecipeDatabaseUsedTest(){
        sut.add(recipe1);
        assertTrue(repo.calledMethods.contains("save"));
    }

    @Test
    public void deleteTest() {
        sut2.add(recipe1);
        sut2.add(recipe2);
        sut2.delete(recipe1.id);
        assertFalse(repo2.findAll().contains(recipe1));
    }

    @Test
    public void deleteWithWrongIdTest0() {
        sut2.add(recipe1);
        sut2.add(recipe2);
        var result = sut2.delete(3);
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void deleteAlreadyDeletedTest0() {
        sut2.add(recipe1);
        sut2.add(recipe2);
        var result = sut2.delete(recipe1.id);
        var result2 = sut2.delete(recipe1.id);
        assertEquals(OK, result.getStatusCode());
        assertEquals(BAD_REQUEST, result2.getStatusCode());
    }

    @Test
    public void updateTest() {
        sut2.add(recipe1);
        var result = sut2.update(recipe1.id, recipe2);
        assertEquals(OK, result.getStatusCode());

        //so that recipes have the sam id (for equals)
        recipe2.id = recipe1.id;
        assertEquals(repo2.findById(recipe1.id).get().id, recipe2.id);
        assertIterableEquals(repo2.findById(recipe1.id).get().steps, recipe2.steps);
        assertEquals(repo2.findById(recipe1.id).get().name, recipe2.name);
        assertIterableEquals(repo2.findById(recipe1.id).get().ingredients, recipe2.ingredients);
    }

    @Test
    public void updateWrongIdTest() {
        sut2.add(recipe1);
        var result = sut2.update(2, recipe2);
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void updateWrongIngredientTest() {
        sut2.add(recipe1);
        Recipe recipe = new Recipe("cucumber", null, null);
        var result = sut2.update(recipe1.id, recipe);
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

}
