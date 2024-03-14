package com.uvg.edu.gt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    public static List<Object> parse(String code) {
        List<String> tokens = tokenize(code);
        return readFromTokens(tokens);
    }

    private static List<String> tokenize(String code) {
        // Reemplazamos paréntesis con espacios y dividimos
        String[] tokens = code.replace("(", " ( ").replace(")", " ) ").trim().split("\\s+");
        return new ArrayList<>(Arrays.asList(tokens));
    }

    private static List<Object> readFromTokens(List<String> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalStateException("Unexpected EOF while reading");
        }
        // El primer token en LISP suele ser '(' que inicia una expresión de lista
        String token = tokens.remove(0);
        if ("(".equals(token)) {
            List<Object> L = new ArrayList<>();
            while (!")".equals(tokens.get(0))) {
                L.add(readFromTokens(tokens));
            }
            tokens.remove(0); // remove ')'
            return L;
        } else if (")".equals(token)) {
            throw new IllegalStateException("Unexpected )");
        } else {
            return Arrays.asList(atom(token));
        }
    }

    private static Object atom(String token) {
        // Aquí determinamos si un token es un número o un símbolo (identificador)
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            try {
                return Double.parseDouble(token);
            } catch (NumberFormatException e2) {
                return token;
            }
        }
    }
}
