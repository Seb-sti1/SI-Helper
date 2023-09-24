/*
 * Copyright (c) 2023. Seb-sti1. See LICENSE.
 */

package fr.seb.function;

import fr.seb.Expression;
import fr.seb.vectors.Variable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AdditionTest {


    // Create a test that check if the result of an addition is correct
    // using the CreateVariable method
    @Test
    @DisplayName("Test CreateVariable")
    public void testCreateVariable() {
        Variable a = new Variable("a", 1);
        Variable b = new Variable("b", 1);

        Expression<Variable> e = Addition.CreateVariable(a, b);

        assert e.getChildren().get(0).equals(a);
        assert e.getChildren().get(0).equals(b);
    }


}
