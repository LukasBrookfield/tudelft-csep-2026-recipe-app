package server.api;

import commons.Ingredient;
import commons.Recipe;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.IngredientRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static commons.Unit.G;

public class TestIngredientRepository implements IngredientRepository {
    public final List<Ingredient> ingredients = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Ingredient> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Ingredient> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Ingredient> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Ingredient getOne(Long aLong) {
        return null;
    }

    @Override
    public Ingredient getById(Long aLong) {
        return null;
    }

    @Override
    public Ingredient getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Ingredient> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Ingredient> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Ingredient> List<S> findAll(Example<S> example, Sort sort) {

        return List.of();
    }

    @Override
    public <S extends Ingredient> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Ingredient> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Ingredient> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Ingredient, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Ingredient> S save(S entity) {
        call("save");
        entity.id = (long) ingredients.size() + 1;
        ingredients.add(entity);
        return entity;
    }

    @Override
    public <S extends Ingredient> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Ingredient> findById(Long aLong) {
        calledMethods.add("findById");
        for(Ingredient ingredient : ingredients) {
            if (ingredient.id == aLong) {
                return Optional.of(ingredient);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.id == aLong) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Ingredient> findAll() {
        calledMethods.add("findAll");
        return ingredients;
    }

    @Override
    public List<Ingredient> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return ingredients.size();
    }

    @Override
    public void deleteById(Long aLong) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.id == aLong) {
                ingredients.remove(ingredient);
                return;
            }
        }
    }

    @Override
    public void delete(Ingredient entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Ingredient> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Ingredient> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Ingredient> findAll(Pageable pageable) {
        return null;
    }
}
