package server.api;

import commons.IngredientType;
import commons.Nutrition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.services.IngredientTypeService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class IngredientTypeControllerTest {
    private IngredientType ingredientType1;
    private IngredientType ingredientType2;

    private TestIngredientTypeRepository repo;
    private IngredientTypeController sut;
    private IngredientTypeService ingredientTypeService;
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    public void setup() {
        repo = new TestIngredientTypeRepository();
        ingredientTypeService = new IngredientTypeService(repo);
        messagingTemplate = mock(SimpMessagingTemplate.class);
        sut = new IngredientTypeController(repo, ingredientTypeService, messagingTemplate);

        ingredientType1 = new IngredientType(
                "Onion", new Nutrition(60.0, 0.0, 0.0), new ArrayList<>(), null);
        ingredientType2 = new IngredientType(
                "Chicken Breast", new Nutrition(0.0, 20.0, 5.0), new ArrayList<>(), null);
    }

    @Test
    public void getAllIngredientsTest() {
        sut.add(ingredientType1);
        sut.add(ingredientType2);
        var ingredientTypes = sut.getAllIngredients();
        assertTrue(repo.calledMethods.contains("findAll"));
        assertEquals(List.of(ingredientType1, ingredientType2), ingredientTypes);
    }

    @Test
    public void getByIdTest() {
        sut.add(ingredientType1);
        var ingredientType = sut.getById(ingredientType1.id).getBody();
        assertTrue(repo.calledMethods.contains("existsById"));
        assertEquals(ingredientType1, ingredientType);
    }

    @Test
    public void getByIdNotFoundTest() {
        var result = sut.getById(999);
        assertFalse(repo.calledMethods.contains("findById"));
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void addTest() {
        var newIngredientType = new IngredientType("Steak", null, new ArrayList<>(), null);
        sut.add(newIngredientType);
        assertTrue(repo.calledMethods.contains("save"));
        assertTrue(repo.ingredientTypes.contains(newIngredientType));
    }

    @Test
    public void addInvalidTest() {
        var newIngredientType = new IngredientType(null, null, null, null);
        newIngredientType.id = -10;
        var result = sut.add(newIngredientType);
        assertFalse(repo.calledMethods.contains("save"));
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void deleteTest() {
        sut.add(ingredientType1);
        sut.add(ingredientType2);
        sut.delete(ingredientType1.id);
        assertTrue(repo.calledMethods.contains("existsById"));
        assertTrue(repo.calledMethods.contains("deleteById"));
        assertFalse(repo.ingredientTypes.contains(ingredientType1));
    }

    @Test
    public void deleteNotFoundTest() {
        var result = sut.delete(999);
        assertFalse(repo.calledMethods.contains("deleteById"));
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void updateTest() {
        sut.add(ingredientType1);
        sut.update(ingredientType1.id, ingredientType2);
        assertTrue(repo.calledMethods.contains("existsById"));
        assertTrue(repo.calledMethods.contains("save"));
        assertEquals(ingredientType1, ingredientType2);
    }

    @Test
    public void updateNotFoundTest() {
        var result = sut.update(999, ingredientType1);
        assertTrue(repo.calledMethods.contains("existsById"));
        assertFalse(repo.calledMethods.contains("save"));
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void updateInvalidTest() {
        var newIngredientType = new IngredientType(null, null, null, null);
        var result = sut.update(ingredientType1.id, newIngredientType);
        assertFalse(repo.calledMethods.contains("save"));
        assertEquals(BAD_REQUEST, result.getStatusCode());
    }
}
