package server.api;

import commons.Recipe;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.RecipeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestRecipeRepository implements RecipeRepository {
    public final List<Recipe> recipes = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Recipe> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Recipe> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Recipe> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Recipe getOne(Long aLong) {
        return null;
    }

    @Override
    public Recipe getById(Long aLong) {
        return null;
    }

    @Override
    public Recipe getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Recipe> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Recipe> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Recipe> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Recipe> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Recipe> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Recipe> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Recipe, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Recipe> S save(S entity) {
        call("save");
        entity.id = (long) recipes.size();
        recipes.add(entity);
        calledMethods.add("save");
        return entity;
    }

    @Override
    public <S extends Recipe> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Recipe> findById(Long aLong) {
        calledMethods.add("findById");
        return Optional.of(new Recipe(null, null, null));
    }

    @Override
    public boolean existsById(Long aLong) {
        return true;
    }

    @Override
    public List<Recipe> findAll() {
        calledMethods.add("findAll");
        return recipes;
    }

    @Override
    public List<Recipe> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Recipe entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Recipe> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Recipe> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Recipe> findAll(Pageable pageable) {
        return null;
    }
}
