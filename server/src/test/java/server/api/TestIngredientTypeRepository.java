package server.api;

import commons.IngredientType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.IngredientTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestIngredientTypeRepository implements IngredientTypeRepository {
    public final List<IngredientType> ingredientTypes = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends IngredientType> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends IngredientType> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<IngredientType> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public IngredientType getOne(Long aLong) {
        return null;
    }

    @Override
    public IngredientType getById(Long aLong) {
        return null;
    }

    @Override
    public IngredientType getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends IngredientType> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends IngredientType> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends IngredientType> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends IngredientType> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends IngredientType> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends IngredientType> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends IngredientType, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends IngredientType> S save(S entity) {
        call("save");
        entity.id = (long) ingredientTypes.size() + 1;
        ingredientTypes.add(entity);
        return entity;
    }

    @Override
    public <S extends IngredientType> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<IngredientType> findById(Long aLong) {
        call("findById");
        for (IngredientType ingredientType : ingredientTypes) {
            if (ingredientType.id == aLong)
                return Optional.of(ingredientType);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        call("existsById");
        for (IngredientType ingredientType : ingredientTypes) {
            if (ingredientType.id == aLong) return true;
        }
        return false;
    }

    @Override
    public List<IngredientType> findAll() {
        call("findAll");
        return ingredientTypes;
    }

    @Override
    public List<IngredientType> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {
        call("deleteById");
        for (IngredientType ingredientType : ingredientTypes) {
            if (ingredientType.id == aLong) ingredientTypes.remove(ingredientType);
        }
    }

    @Override
    public void delete(IngredientType entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends IngredientType> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<IngredientType> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<IngredientType> findAll(Pageable pageable) {
        return null;
    }
}
