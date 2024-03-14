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
}
