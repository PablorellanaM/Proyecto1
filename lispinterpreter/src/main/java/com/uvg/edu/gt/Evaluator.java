package com.uvg.edu.gt;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Evaluator {
    private final Environment globalEnvironment;

    public Evaluator() {
        this.globalEnvironment = new Environment();

    }

    public Object eval(Object expr, Environment env) {

        return null;
    }

}

class Environment {
    private final Map<String, Object> variables;

    public Environment() {
        this.variables = new HashMap<>();
    }

}
