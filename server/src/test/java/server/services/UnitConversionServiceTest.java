package server.services;

import commons.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitConversionServiceTest {

    private final UnitConversionService controller = new UnitConversionService();

    @Test
    public void toGrams_withKg_convertsCorrectly(){
        var grams = controller.toGrams(Unit.KG, 2.0);
        assertTrue(grams.isPresent());
        assertEquals(2000.0, grams.get(), 1e-6);
    }

    @Test
    public void toMilliliters_withTsbp_convertCorrectly() {
        var ml = controller.toMl(Unit.TBSP, 2.0);
        assertTrue(ml.isPresent());
        assertEquals(29.574, ml.get(), 1e-6);
    }

    @Test
    public void toBase_unkownUnit_returnsEmpty() {
        var base = controller.toBase(Unit.TO_TASTE, 1.0);
        assertTrue(base.isEmpty());
    }
}
