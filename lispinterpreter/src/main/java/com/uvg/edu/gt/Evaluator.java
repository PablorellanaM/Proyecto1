package com.uvg.edu.gt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Clase que representa un evaluador de expresiones.
 */
public class Evaluator {

    private final Environment globalEnvironment;

    /**
     * Constructor de la clase Evaluator.
     */
    public Evaluator() {
        this.globalEnvironment = new Environment();
        initializeGlobalEnvironment();
    }

    /**
     * Inicializa el entorno global con operadores predefinidos.
     */
    private void initializeGlobalEnvironment() {
        globalEnvironment.define("+",
                args -> args.stream().mapToDouble(arg -> Double.parseDouble(arg.toString())).sum());
        globalEnvironment.define("-", args -> {
            if (args.size() == 1) {
                return -Double.parseDouble(args.get(0).toString());
            } else {
                double result = Double.parseDouble(args.get(0).toString());
                for (int i = 1; i < args.size(); i++) {
                    result -= Double.parseDouble(args.get(i).toString());
                }
                return result;
            }
        });
        globalEnvironment.define("*", args -> args.stream().mapToDouble(arg -> Double.parseDouble(arg.toString()))
                .reduce(1, (a, b) -> a * b));
        globalEnvironment.define("/", args -> {
            if (args.size() == 1) {
                return 1 / Double.parseDouble(args.get(0).toString());
            } else {
                double result = Double.parseDouble(args.get(0).toString());
                for (int i = 1; i < args.size(); i++) {
                    result /= Double.parseDouble(args.get(i).toString());
                }
                return result;
            }
        });
        // Puedes agregar más definiciones aquí si lo deseas
    }

    /**
     * Evalúa una expresión en un entorno dado.
     *
     * @param expr la expresión a evaluar.
     * @param env  el entorno en el que se evalúa la expresión.
     * @return el resultado de la evaluación de la expresión.
     */
    public Object eval(Object expr, Environment env) {
        if (expr instanceof List) {
            List<?> list = (List<?>) expr;
            if (list.isEmpty()) {
                throw new IllegalArgumentException("Expresión vacía");
            }
            if (!(list.get(0) instanceof String)) {
                throw new IllegalArgumentException(
                        "El primer elemento de la lista debe ser un operador en forma de String");
            }
            String operator = (String) list.get(0);
            List<Object> args = new ArrayList<>(list.subList(1, list.size()));
            Function<List<Object>, Object> function = env.lookup(operator);
            return function.apply(args);
        } else {
            return expr; // Números y literales son devueltos como están
        }
    }

    /**
     * Obtiene el entorno global.
     *
     * @return el entorno global.
     */
    public Environment getGlobalEnvironment() {
        return globalEnvironment;
    }

    /**
     * Método principal que ejecuta el intérprete.
     *
     * @param args los argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        Evaluator evaluator = new Evaluator();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenido al intérprete LISP simplificado. Escribe 'salir' para terminar.");

        while (true) {
            System.out.print("lisp> ");
            String input = scanner.nextLine();

            if ("salir".equalsIgnoreCase(input)) {
                scanner.close();
                break;
            }

            try {
                Object result = evaluator.eval(Parser.parse(input), evaluator.getGlobalEnvironment());
                System.out.println("Resultado: " + result);
            } catch (Exception e) {
                System.out.println("Error al evaluar la expresión: " + e.getMessage());
            }
        }
    }
}

/**
 * Clase que representa un entorno de evaluación.
 */
class Environment {
    private final Map<String, Function<List<Object>, Object>> data;

    /**
     * Constructor de la clase Environment.
     */
    public Environment() {
        this.data = new HashMap<>();
    }

    /**
     * Define un operador en el entorno.
     *
     * @param name     el nombre del operador.
     * @param function la función asociada al operador.
     */
    public void define(String name, Function<List<Object>, Object> function) {
        data.put(name, function);
    }

    /**
     * Busca una función asociada a un operador en el entorno.
     *
     * @param name el nombre del operador.
     * @return la función asociada al operador.
     * @throws IllegalArgumentException si el operador no está definido en el
     *                                  entorno.
     */
    public Function<List<Object>, Object> lookup(String name) {
        if (!data.containsKey(name)) {
            throw new IllegalArgumentException("Operador desconocido: " + name);
        }
        return data.get(name);
    }
}
