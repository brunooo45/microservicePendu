package jeu.pendu.pendumicroservice.utils;

import java.text.Normalizer;

public class StringUtils {

    public static String normaliser(String input) {
        if (input == null) {
            return null;
        }
        String sansAccents = Normalizer.normalize(input, Normalizer.Form.NFD);
        sansAccents = sansAccents.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return sansAccents;
    }
}
