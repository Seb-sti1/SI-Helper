package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;
import fr.seb.vectors.VectorNull;

import java.util.Arrays;
import java.util.List;

public class Addition<T> implements Expression<T> {

    public final Expression<T> left;
    public final Expression<T> right;

    public Addition(Expression<T> left, Expression<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public List<Expression<T>> getChild() {
        return Arrays.asList(left, right);
    }

    @Override
    public Expression<T> calcul() {

        if (left.calcul() instanceof Scalar && ((Scalar<T>) left.calcul()).n == 0) { // remove addition of zero
            return right.calcul();
        }

        if (right.calcul() instanceof Scalar && ((Scalar<T>) right.calcul()).n == 0) { // remove addition of zero
            return left.calcul();
        }

        if (left.calcul() instanceof VectorNull) { // remove addition of zero
            return right.calcul();
        }

        if (right.calcul() instanceof VectorNull) { // remove addition of zero
            return left.calcul();
        }

        return new Addition<>(left.calcul(), right.calcul()); // temporaire
    }

    @Override
    public Expression<T> derive(Space R) {
        return new Addition<>(left.derive(R), right.derive(R));
    }

    @Override
    public Expression<T> derive(int recursionDepth, Space R) {
        if (recursionDepth == 1) {
            return new Addition<>(new Derivation<>(left, R), new Derivation<>(right, R));
        } else {
            return new Addition<>(left.derive(recursionDepth - 1, R), right.derive(recursionDepth - 1, R));
        }
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "+" + right.toString() + ")";
    }
}
