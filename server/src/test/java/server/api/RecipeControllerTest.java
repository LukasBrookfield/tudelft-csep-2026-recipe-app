package server.api;

import commons.Ingredient;
import commons.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static commons.Unit.G;
import static org.junit.jupiter.api.Assertions.*;


public class RecipeControllerTest {

    private TestRecipeRepository repo;
    private RecipeController sut;
    private Recipe recipe1;

    @BeforeEach
    public void setUp()
    {
        repo = new TestRecipeRepository();
        sut = new RecipeController(repo);
        Ingredient ingredient1 = new Ingredient("cucumber", 100.0, G);
        recipe1 = new Recipe("cucumber salad", List.of(ingredient1), List.of("add cucumbers"));
    }
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

}
