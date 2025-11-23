package server.database;

import org.springframework.data.jpa.repository.JpaRepository;

import commons.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {}
