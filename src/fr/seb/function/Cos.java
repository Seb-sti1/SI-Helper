package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;
import fr.seb.vectors.Variable;

import java.util.Collections;
import java.util.List;

public class Cos implements Expression<Variable> {

    final Expression<Variable> child;

    public Cos(Expression<Variable> param) {
        this.child = param;
    }


    @Override
    public List<Expression<Variable>> getChild() {
        return Collections.singletonList(child);
    }

    @Override
    public Expression<Variable> calcul() {
        return new Cos(child.calcul());
    }

    @Override
    public Expression<Variable> derive(Space R) {
        return new Product(child.derive(R), new Product(new Scalar<>(-1), new Sin(child)));
    }

    @Override
    public Expression<Variable> derive(int recursionDepth, Space R) {
        if (recursionDepth == 0) {
            return this;
        } else if (recursionDepth == 1) {
            return new Product(new Derivation<>(child, R), new Product(new Scalar<>(-1), new Sin(child)));
        } else {
            return new Product(child.derive(recursionDepth - 1, R), new Product(new Scalar<>(-1), new Sin(child)));
        }
    }

    @Override
    public String toString() {
        return String.format("\\cos(%s)", child.toString());
    }
}
