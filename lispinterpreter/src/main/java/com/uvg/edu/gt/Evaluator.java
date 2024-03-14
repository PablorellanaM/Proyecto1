package com.uvg.edu.gt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Evaluator {

    private final Environment globalEnvironment;

    public Evaluator() {
        this.globalEnvironment = new Environment();
        initializeGlobalEnvironment();
    }

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
        // Aquí puedes definir más operadores si es necesario
    }

    public Object eval(List<Object> expr, Environment env) {
        if (expr.isEmpty()) {
            throw new IllegalArgumentException("Empty expression");
        }
        String operator = expr.get(0).toString();
        List<Object> args = expr.subList(1, expr.size());
        Function<List<Object>, Object> function = env.lookup(operator);
        return function.apply(args);
    }

    public Environment getGlobalEnvironment() {
        return globalEnvironment;
    }
}

class Environment {
    private final Map<String, Function<List<Object>, Object>> data;

    public Environment() {
        this.data = new HashMap<>();
    }

    public void define(String name, Function<List<Object>, Object> function) {
        data.put(name, function);
    }

    public Function<List<Object>, Object> lookup(String name) {
        if (!data.containsKey(name)) {
            throw new IllegalArgumentException("Unknown operator: " + name);
        }
        return data.get(name);
    }
}
