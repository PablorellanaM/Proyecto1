package com.uvg.edu.gt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Analizador de expresiones LISP.
 */
public class Parser {

    /**
     * Convierte una cadena de texto que representa una expresión LISP en una lista
     * de objetos.
     *
     * @param code La expresión LISP como cadena de texto.
     * @return Una lista de objetos representando la expresión LISP.
     */
    public static List<Object> parse(String code) {
        List<String> tokens = tokenize(code);
        if (!tokens.isEmpty()) {
            return readFromTokens(tokens);
        }
        return new ArrayList<>();
    }

    /**
     * Divide el código LISP en tokens individuales.
     *
     * @param code El código LISP como cadena de texto.
     * @return Una lista de tokens como cadenas.
     */
    private static List<String> tokenize(String code) {
        String[] tokens = code.replace("(", " ( ").replace(")", " ) ").trim().split("\\s+");
        return new ArrayList<>(Arrays.asList(tokens));
    }

    /**
     * Lee los tokens para construir una lista anidada que representa la expresión
     * LISP.
     *
     * @param tokens Los tokens a leer.
     * @return Una lista anidada de objetos representando la expresión LISP.
     */
    private static List<Object> readFromTokens(List<String> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalStateException("No más tokens para leer");
        }
        String token = tokens.remove(0);
        if ("(".equals(token)) {
            List<Object> L = new ArrayList<>();
            while (!")".equals(tokens.get(0))) {
                L.add(readFromTokens(tokens));
            }
            tokens.remove(0); // Elimina el token ')'
            return L;
        } else if (")".equals(token)) {
            throw new IllegalStateException("')' inesperado");
        } else {
            return new ArrayList<>(Arrays.asList(atom(token)));
        }
    }

    /**
     * Convierte un token en un átomo, que puede ser un número o un símbolo.
     *
     * @param token El token a convertir.
     * @return El átomo como objeto, que puede ser un Integer, Double o String.
     */
    private static Object atom(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            try {
                return Double.parseDouble(token);
            } catch (NumberFormatException e2) {
                return token; // Es un símbolo
            }
        }
    }
}
