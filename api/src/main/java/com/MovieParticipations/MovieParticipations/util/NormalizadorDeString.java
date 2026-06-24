package com.MovieParticipations.MovieParticipations.util;

import static java.text.Normalizer.Form.NFD;
import static java.text.Normalizer.normalize;

public class NormalizadorDeString {
    public static String normalizar(String string) {
        return normalize(string, NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .trim();
    }
}
