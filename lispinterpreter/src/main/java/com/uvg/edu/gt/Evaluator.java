package com.uvg.edu.gt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Clase que representa un evaluador de expresiones en un intérprete LISP
 * simplificado.
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
        globalEnvironment.define("+", args -> args.stream().mapToDouble(arg -> (double) arg).sum());
        globalEnvironment.define("-", args -> {
            if (args.size() == 1) {
                return -((double) args.get(0));
            } else {
                double result = (double) args.get(0);
                for (int i = 1; i < args.size(); i++) {
                    result -= (double) args.get(i);
                }
                return result;
            }
        });
        globalEnvironment.define("*",
                args -> args.stream().mapToDouble(arg -> (double) arg).reduce(1, (a, b) -> a * b));
        globalEnvironment.define("/", args -> {
            if (args.size() == 1) {
                return 1 / (double) args.get(0);
            } else {
                double result = (double) args.get(0);
                for (int i = 1; i < args.size(); i++) {
                    result /= (double) args.get(i);
                }
                return result;
            }
        });
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
            List<Object> args = new ArrayList<>();
            for (int i = 1; i < list.size(); i++) {
                Object arg = list.get(i);
                if (arg instanceof List) {
                    arg = eval(arg, env); // Evaluar argumentos anidados
                }
                args.add(arg);
            }
            Function<List<Object>, Object> function = env.lookup(operator);
            return function.apply(args);
        } else if (expr instanceof Double) {
            return expr; // Si es un número, devolverlo tal cual
        } else {
            throw new IllegalArgumentException("Expresión no válida");
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
