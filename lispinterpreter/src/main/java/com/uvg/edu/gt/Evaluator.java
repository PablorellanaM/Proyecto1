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
     * Constructor de la clase Evaluator que inicializa el entorno global.
     */
    public Evaluator() {
        this.globalEnvironment = new Environment();
        initializeGlobalEnvironment();
    }

    /**
     * Inicializa el entorno global con operadores aritméticos básicos.
     */
    private void initializeGlobalEnvironment() {
        globalEnvironment.define("+", args -> args.stream().mapToDouble(arg -> ((Number) arg).doubleValue()).sum());
        globalEnvironment.define("-", args -> {
            if (args.size() == 1) {
                return -((Number) args.get(0)).doubleValue();
            } else {
                double result = ((Number) args.get(0)).doubleValue();
                for (int i = 1; i < args.size(); i++) {
                    result -= ((Number) args.get(i)).doubleValue();
                }
                return result;
            }
        });
        globalEnvironment.define("*",
                args -> args.stream().mapToDouble(arg -> ((Number) arg).doubleValue()).reduce(1, (a, b) -> a * b));
        globalEnvironment.define("/", args -> {
            if (args.size() == 1) {
                return 1 / ((Number) args.get(0)).doubleValue();
            } else {
                double result = ((Number) args.get(0)).doubleValue();
                for (int i = 1; i < args.size(); i++) {
                    result /= ((Number) args.get(i)).doubleValue();
                }
                return result;
            }
        });

    }

    /**
     * Evalúa una expresión en un entorno dado.
     *
     * @param expr La expresión a evaluar.
     * @param env  El entorno en el que se evalúa la expresión.
     * @return El resultado de la evaluación de la expresión.
     */
    public Object eval(Object expr, Environment env) {
        if (expr instanceof List) {
            List<?> list = (List<?>) expr;
            if (list.isEmpty()) {
                throw new IllegalArgumentException("Expresión vacía");
            }
            String operator = (String) list.get(0);
            List<Object> args = new ArrayList<>();
            for (int i = 1; i < list.size(); i++) {
                args.add(eval(list.get(i), env));
            }
            Function<List<Object>, Object> function = env.lookup(operator);
            return function.apply(args);
        } else {
            return expr;
        }
    }

    /**
     * Método principal que proporciona una interfaz de línea de comandos para el
     * intérprete LISP.
     *
     * @param args Argumentos pasados al programa (no utilizados).
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

    public Environment getGlobalEnvironment() {
        return globalEnvironment;
    }

}

/**
 * Clase que representa el entorno de evaluación que mantiene un registro de
 * operadores y variables.
 */
class Environment {
    private final Map<String, Function<List<Object>, Object>> data;

    /**
     * Constructor de la clase Environment que inicializa el mapa de datos.
     */
    public Environment() {
        this.data = new HashMap<>();
    }

    /**
     * Define un operador en el entorno.
     *
     * @param name     El nombre del operador.
     * @param function La función asociada al operador.
     */
    public void define(String name, Function<List<Object>, Object> function) {
        data.put(name, function);
    }

    /**
     * Busca una función asociada a un operador en el entorno.
     *
     * @param name El nombre del operador.
     * @return La función asociada al operador.
     */
    public Function<List<Object>, Object> lookup(String name) {
        if (!data.containsKey(name)) {
            throw new IllegalArgumentException("Operador desconocido: " + name);
        }
        return data.get(name);
    }
}
