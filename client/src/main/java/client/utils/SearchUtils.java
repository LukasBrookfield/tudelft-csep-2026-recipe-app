package client.utils;

import java.text.Normalizer;
import java.util.Locale;

public class SearchUtils {

    private SearchUtils(){}

    public static String normalizeForSearch(String s) {
        if (s == null ) return "";

        String lower = s.toLowerCase(Locale.ROOT).trim();
        // Locale.ROOT makes special characters from different languages the "default english ones"

        String normalized = Normalizer.normalize(lower, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        // Similarly, this decomposes special accents into the "default letters" + their accents / special features
        // (and then replaces those accents / features, which are Mark characters

        return normalized.replaceAll("\\s+", " ");
    }

    public static boolean containsNormalized(String searching, String found){
        return normalizeForSearch(searching).contains(normalizeForSearch(found));
    }
}
