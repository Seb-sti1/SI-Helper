package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;

import java.util.List;

public class Scalar<T> implements Expression<T> {

    final int n;

    public Scalar(int n) {
        this.n = n;
    }

    @Override
    public List<Expression<T>> getChild() {
        return null;
    }

    @Override
    public Expression<T> calcul() {
        return this;
    }

    @Override
    public Expression<T> derive(Space R) {
        return new Scalar<>(0);
    }

    @Override
    public Expression<T> derive(int recursionDepth, Space R) {
        if (recursionDepth == 0) {
            return this;
        } else if (recursionDepth == 1) {
            return new Derivation<>(this, R);
        } else {
            return new Scalar<>(0);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(n);
    }
}
