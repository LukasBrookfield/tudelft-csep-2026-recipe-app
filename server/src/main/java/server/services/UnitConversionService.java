package server.services;

import commons.Ingredient;
import commons.Unit;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UnitConversionService {

    private static final Map<Unit, Double> mass_to_grams;
    private static final Map<Unit, Double> volume_to_ml;

    static {
        Map<Unit, Double> mass = new EnumMap<>(Unit.class);
        mass.put(Unit.KG, 1000.0);
        mass.put(Unit.G, 1.0);
        mass_to_grams = Collections.unmodifiableMap(mass);

        Map<Unit, Double> volume = new EnumMap<>(Unit.class);
        volume.put(Unit.L, 1000.0);
        volume.put(Unit.ML, 1.0);
        volume.put(Unit.TSP, 4.928);
        volume.put(Unit.TBSP, 14.787);
        volume_to_ml = Collections.unmodifiableMap(volume);
    }

    /**
     * Checks if the unit is informal (not convertible).
     * @param unit to be checked.
     * @return True if the unit is informal, meaning not convertible.
     */
    public boolean isInformal(Unit unit) {
        return unit == null || unit == Unit.PINCH || unit == Unit.HANDFUL || unit == Unit.TO_TASTE;
    }

    /**
     * Tries to convert (amount, unit) to its base unit (grams or milliliters).
     * @param unit of the ingredient, to convert from.
     * @param amount of the ingredient.
     * @return an optional containing the normalized quantity
     * (empty if the unit is null or not convertible).
     */
    public Optional<NormalizedQuantity> toBase (Unit unit, double amount){
        if (unit == null) return Optional.empty();

        Double gramsFactor = mass_to_grams.get(unit);
        if (gramsFactor != null){
            return Optional.of(new NormalizedQuantity(amount * gramsFactor, Unit.G));
        }

        Double mlFactor = volume_to_ml.get(unit);
        if (mlFactor != null){
            return Optional.of(new NormalizedQuantity(amount * mlFactor, Unit.ML));
        }

        return Optional.empty();
    }

    // just used as a return type for toBase
    public record NormalizedQuantity(double amount, Unit unit) {}

    /**
     * Converts a numeric value from a unit to grams.
     * @param unit to convert from
     * @param amount numeric value to convert
     * @return grams equivalent of the given amount
     * (empty if the unit is null or not convertible)
     */
    public Optional<Double> toGrams(Unit unit, double amount) {
        Double factor = unit == null ? null : mass_to_grams.get(unit);
        return factor == null ? Optional.empty() : Optional.of(amount * factor);
    }

    /**
     * Converts a numeric value from a unit to milliliters.
     * @param unit to convert from
     * @param amount numeric value to convert
     * @return milliliters equivalent of the given amount
     * (empty if the unit is null or not convertible)
     */
    public Optional<Double> toMl(Unit unit, double amount) {
        Double factor = unit == null ? null : volume_to_ml.get(unit);
        return factor == null ? Optional.empty() : Optional.of(amount * factor);
    }

    /**
     * Checks if the unit is a mass unit.
     * @param unit to check
     * @return true if this unit is a mass unit
     */
    public boolean isMass(Unit unit) {
        return unit != null && mass_to_grams.containsKey(unit);
    }

    /**
     * Checks if the unit is a volume unit.
     * @param unit to check
     * @return true if this unit is a volume unit
     */
    public boolean isVolume(Unit unit) {
        return unit != null && volume_to_ml.containsKey(unit);
    }
}
