package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;
import fr.seb.vectors.Variable;

import java.util.List;

public class Scalar extends Expression<Variable> {

    public int n;

    public Scalar(int n) {
        this.n = n;
    }

    @Override
    public List<Expression<Variable>> getChildren() {
        return null;
    }

    @Override
    public Expression<Variable> calcul() {
        return this;
    }

    @Override
    public Expression<Variable> derive(Space R) {
        return new Scalar(0);
    }

    @Override
    public Expression<Variable> derive(int recursionDepth, Space R) {
        if (recursionDepth == 0) {
            return this;
        } else if (recursionDepth == 1) {
            return new Derivation<>(this, R);
        } else {
            return new Scalar(0);
        }
    }

    @Override
    public Expression<Variable> invertSign() {
        this.n = - this.n;

        return this;
    }

    @Override
    public String toString() {
        return String.valueOf(n);
    }
}
