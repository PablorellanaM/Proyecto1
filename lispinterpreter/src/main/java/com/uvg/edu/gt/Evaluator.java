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
        globalEnvironment.define("+", (args) -> {
            double sum = 0;
            for (Object arg : args) {
                sum += Double.parseDouble(arg.toString());
            }
            return sum;
        });

        globalEnvironment.define("-", (args) -> {
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

    }

    public Object eval(Object expr, Environment env) {
        if (expr instanceof List) {
            List<?> list = (List<?>) expr;
            if (list.isEmpty()) {
                throw new IllegalArgumentException("Empty expression");
            }
            String operator = list.get(0).toString();
            List<Object> args = new ArrayList<>(list.subList(1, list.size()));
            if (!env.isDefined(operator)) {
                throw new IllegalArgumentException("Unknown operator: " + operator);
            }
            Function<List<Object>, Object> function = env.lookup(operator);
            return function.apply(args);
        } else if (expr instanceof String && env.isDefined((String) expr)) {
            return env.lookup((String) expr).apply(null);
        } else {
            return expr; // Numbers and other literals are returned as is
        }
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

    public boolean isDefined(String name) {
        return data.containsKey(name);
    }

    public Function<List<Object>, Object> lookup(String name) {
        return data.get(name);
    }
}
