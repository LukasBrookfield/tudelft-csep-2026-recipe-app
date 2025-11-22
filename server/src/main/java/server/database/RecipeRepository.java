package server.database;

import org.springframework.data.jpa.repository.JpaRepository;

import commons.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {}
