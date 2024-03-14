package com.uvg.edu.gt;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class EvaluatorTest {

    @Test
    public void testSimpleAddition() {
        Evaluator evaluator = new Evaluator();
        List<Object> expression = Arrays.asList("+", 1, 2);
        assertEquals(3.0, evaluator.eval(expression, evaluator.getGlobalEnvironment()));
    }

    @Test
    public void testSubtraction() {
        Evaluator evaluator = new Evaluator();
        List<Object> expression = Arrays.asList("-", 5, 3);
        assertEquals(2.0, evaluator.eval(expression, evaluator.getGlobalEnvironment()));
    }

    @Test
    public void testMultiplication() {
        Evaluator evaluator = new Evaluator();
        List<Object> expression = Arrays.asList("*", 4, 3);
        assertEquals(12.0, evaluator.eval(expression, evaluator.getGlobalEnvironment()));
    }

    @Test
    public void testDivision() {
        Evaluator evaluator = new Evaluator();
        List<Object> expression = Arrays.asList("/", 8, 2);
        assertEquals(4.0, evaluator.eval(expression, evaluator.getGlobalEnvironment()));
    }
}
