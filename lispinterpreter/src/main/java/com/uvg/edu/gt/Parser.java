package com.uvg.edu.gt;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase para analizar y parsear expresiones LISP simplificadas.
 */
public class Parser {

    /**
     * Analiza una cadena de entrada y la convierte en una estructura de datos
     * adecuada para su evaluación.
     *
     * @param input la cadena de entrada a analizar.
     * @return la estructura de datos representando la expresión.
     */
    public static Object parse(String input) {
        List<Object> tokens = tokenize(input);
        return parseTokens(tokens);
    }

    /**
     * Divide una cadena de entrada en tokens individuales.
     *
     * @param input la cadena de entrada a tokenizar.
     * @return una lista de tokens.
     */
    private static List<Object> tokenize(String input) {
        List<Object> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inNumber = false;

        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                if (sb.length() > 0) {
                    tokens.add(getToken(sb.toString(), inNumber));
                    sb.setLength(0);
                    inNumber = false;
                }
            } else if (c == '(' || c == ')') {
                if (sb.length() > 0) {
                    tokens.add(getToken(sb.toString(), inNumber));
                    sb.setLength(0);
                    inNumber = false;
                }
                tokens.add(String.valueOf(c));
            } else {
                sb.append(c);
                if (Character.isDigit(c) && !inNumber) {
                    inNumber = true;
                }
            }
        }

        if (sb.length() > 0) {
            tokens.add(getToken(sb.toString(), inNumber));
        }

        return tokens;
    }

    /**
     * Convierte un token en el tipo de dato apropiado.
     *
     * @param token    el token a convertir.
     * @param isNumber indica si el token representa un número.
     * @return el token convertido al tipo de dato apropiado.
     */
    private static Object getToken(String token, boolean isNumber) {
        if (isNumber) {
            return Double.parseDouble(token);
        } else {
            return token;
        }
    }

    /**
     * Convierte una lista de tokens en una estructura de datos representando la
     * expresión LISP.
     *
     * @param tokens la lista de tokens a convertir.
     * @return la estructura de datos representando la expresión LISP.
     */
    private static Object parseTokens(List<Object> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Expresión vacía");
        }

        String token = (String) tokens.get(0);
        tokens.remove(0);

        if ("(".equals(token)) {
            List<Object> list = new ArrayList<>();
            while (!tokens.isEmpty() && !")".equals(tokens.get(0))) {
                list.add(parseTokens(tokens));
            }
            if (tokens.isEmpty()) {
                throw new IllegalArgumentException("Paréntesis no cerrado");
            }
            tokens.remove(0);
            return list;
        } else if (")".equals(token)) {
            throw new IllegalArgumentException("Paréntesis no abierto");
        } else {
            return token;
        }
    }
}
