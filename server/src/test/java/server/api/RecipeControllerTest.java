package server.api;

import commons.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import static org.mockito.Mockito.mock;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class RecipeControllerTest{
    // For testing with TestRecipeRepository
    private TestRecipeRepository repo;
    private RecipeController sut;

    private Recipe recipe1;
    private Recipe recipe2;
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    public void setUp() {
        // for testing with TestIngredientRepository
        repo = new TestRecipeRepository();
        messagingTemplate = mock(SimpMessagingTemplate.class);
        sut = new RecipeController(repo, messagingTemplate);

        recipe1 = new Recipe("cucumber salad",
                new ArrayList<>(),
                new ArrayList<>(List.of("add cucumbers")),
                2);
        recipe2 = new Recipe("cucumber with salt",
                new ArrayList<>(),
                new ArrayList<>(List.of("add cucumbers")),
                3);
    }

    @Test
    public void getAllRecipesTest () {
        sut.add(recipe1);
        assertEquals(sut.getAllRecipes(), List.of(recipe1));
    }

    @Test
    public void getAllRecipesDatabaseTest () {
        sut.add(recipe1);
        sut.getAllRecipes();
        assertTrue(repo.calledMethods.contains("findAll"));
    }

    @Test
    public void getRecipeByIdTest () {
        sut.add(recipe1);
        assertEquals(sut.getById((long) 0), ResponseEntity.ok(recipe1));
    }

    @Test
    public void nullAddRecipeTestUsedDatabase () {
        var r = new Recipe("name", null, null, 1);
        assertEquals(ResponseEntity.badRequest().build(), sut.add(r));
    }

    @Test
    public void addRecipeTestOkResponse () {
        assertEquals(sut.add(recipe1), ResponseEntity.ok(recipe1));
    }

    @Test
    public void addRecipeDatabaseUsedTest () {
        sut.add(recipe1);
        assertTrue(repo.calledMethods.contains("save"));
    }

    @Test
    public void deleteTest(){
        sut.add(recipe1);
        sut.add(recipe2);
        sut.delete(recipe1.id);
        assertFalse(repo.findAll().contains(recipe1));
    }

    @Test
    public void deleteWithWrongIdTest0 () {
        sut.add(recipe1);
        sut.add(recipe2);
        var result = sut.delete(3);
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void deleteAlreadyDeletedTest () {
        sut.add(recipe1);
        sut.add(recipe2);
        var result = sut.delete(recipe1.id);
        var result2 = sut.delete(recipe1.id);
        assertEquals(OK, result.getStatusCode());
        assertEquals(BAD_REQUEST, result2.getStatusCode());
    }

    @Test
    public void updateTest () {
        sut.add(recipe1);
        var result = sut.update(recipe1.id, recipe2);
        assertEquals(OK, result.getStatusCode());

        assertIterableEquals(repo.findById(recipe1.id).get().steps, recipe2.steps);
        assertEquals(repo.findById(recipe1.id).get().name, recipe2.name);
        assertIterableEquals(repo.findById(recipe1.id).get().ingredients, recipe2.ingredients);
    }

    @Test
    public void updateWrongIdTest () {
        sut.add(recipe1);
        var result = sut.update(2, recipe2);
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void updateWrongIngredientTest () {
        sut.add(recipe1);
        Recipe recipe = new Recipe("cucumber", null, null, 1);
        var result = sut.update(recipe1.id, recipe);
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }
}
