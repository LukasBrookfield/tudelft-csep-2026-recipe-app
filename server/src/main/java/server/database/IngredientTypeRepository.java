package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import commons.IngredientType;

public interface IngredientTypeRepository extends JpaRepository<IngredientType, Long> {}
