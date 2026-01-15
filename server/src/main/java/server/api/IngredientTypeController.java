package server.api;

import java.util.List;

import commons.IngredientType;
import commons.Recipe;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.IngredientTypeRepository;
import server.services.IngredientTypeService;

@RestController
@RequestMapping("api/ingredientTypes")
public class IngredientTypeController {
    private final IngredientTypeRepository repo;
    private final IngredientTypeService ingredientTypeService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * IngredientTypeController constructor
     * @param repo The spring IngredientType repository (connects to sql database)
     */
    public IngredientTypeController(IngredientTypeRepository repo,
                                    IngredientTypeService ingredientTypeService, SimpMessagingTemplate messagingTemplate) {
        this.repo = repo;
        this.ingredientTypeService = ingredientTypeService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Returns all the ingredients in the database
     * @return List of ingredientTypes
     */
    @GetMapping(path = { "", "/" })
    public List<IngredientType> getAllIngredients() {
        return repo.findAll();
    }

    /**
     * Inputs a numeric id and returns the ingredientType in the database that has that id
     * @param id The id of the ingredientType to get
     * @return The ingredientType with the corresponding id
     */
    @GetMapping("/{id}")
    public ResponseEntity<IngredientType> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Receives an ingredientType from the client via HTTP and saves it to the database.
     * If any validation fails, a 400 Bad Request response is returned.
     * Otherwise, the ingredientType is saved to the repository and returned with a
     * 200 OK with the saved ingredient.
     * @param ingredientType the Ingredient object provided in the request body
     * @return a ResponseEntity containing the saved ingredient or an
     *         error response if validation fails
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<IngredientType> add(@RequestBody IngredientType ingredientType) {
        if (!ingredientTypeService.validateIngredientType(ingredientType)) {
            return ResponseEntity.badRequest().build();
        }
        IngredientType saved = repo.save(ingredientType);

        //Broadcast the entire list of ingredientType elements
        broadcastList();
        //Broadcast the ingredientType element by id
        broadcastSingle(saved);

        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes an ingredientType in database by its ID.
     * This method validates the incoming ingredientType id to ensure that it exists.
     * If the ID is valid, retrieves the ingredientType from the repository.
     * Then deletes the ingredientType from the repository.
     * Returns the deleted ingredient wrapped in a 200 OK response.
     * @param id the ID of the ingredientType to delete
     * @return ResponseEntity containing the deleted ingredient if successful,
     *         or a 400 Bad Request response if the ID is invalid or does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<IngredientType> delete(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        } else {
            IngredientType deleted = repo.findById(id).get();
            repo.deleteById(id);

            //Broadcast the entire list of ingredientType elements
            broadcastList();

            return ResponseEntity.ok(deleted);
        }
    }

    /**
     * Updates an existing ingredientType with the given ID.
     * This method validates the incoming ingredientType id and new ingredientType.
     * If validation fails, or the ID does not exist, returns a 400 Bad Request response.
     * If the ID is valid, retrieves the existing ingredientType from the repository,
     * updates its fields, saves it, and returns the updated ingredient with 200 OK.
     * @param id the ID of the ingredientType to update
     * @param updatedIngredientType the ingredientType object containing the updated values
     * @return ResponseEntity containing the updated ingredient if successful,
     *         or a 400 Bad Request if validation fails or the ID is invalid/non-existent.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IngredientType> update(@PathVariable("id") long id,
                                                 @RequestBody IngredientType
                                                         updatedIngredientType) {
        if (!ingredientTypeService.validateUpdatedIngredientType(id,
                updatedIngredientType)) {
            return ResponseEntity.badRequest().build();
        }

        IngredientType ingredientTypeToUpdate = repo.findById(id).get();
        ingredientTypeService.transferFields(updatedIngredientType,
                ingredientTypeToUpdate);

        IngredientType updated = repo.save(ingredientTypeToUpdate);

        //Broadcast the entire list of ingredientType elements
        broadcastList();
        //Broadcast the ingredientType element by id
        broadcastSingle(updated);

        return ResponseEntity.ok(updated);
    }

    private void broadcastList() {
        List<IngredientType> all = repo.findAll();
        // Every client that is subscribed to "/topic/ingredientType/list" gets the new full list
        messagingTemplate.convertAndSend("/topic/ingredientType/list", all);
    }


    private void broadcastSingle(IngredientType ingredientType) {
        if (ingredientType != null && ingredientType.id > 0) {
            // Every client that is subscribed to "/topic/ingredientType/{id}" gets this updated ingredientType
            messagingTemplate.convertAndSend("/topic/ingredientType/" + ingredientType.id, ingredientType);
        }
    }
}
