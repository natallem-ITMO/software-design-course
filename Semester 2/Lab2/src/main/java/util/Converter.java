package util;

import java.util.Map;

public class Converter {
    /**
     * Mapping from currency value to unified value
     */
    private final Map<Currency, Integer> values;

    public Converter(Map<Currency, Integer> values) {
        this.values = values;
    }

    public int convertToUnifiedValue(int concurrencyValue, Currency currentConcurrency) {
        int unifiedValueInCurrency = values.get(currentConcurrency);
        return concurrencyValue * unifiedValueInCurrency;
    }

    public int convertToCurrencyValue(int unifiedValue, Currency currentConcurrency) {
        int unifiedValueInCurrency = values.get(currentConcurrency);
        return unifiedValue / unifiedValueInCurrency;
    }

}
