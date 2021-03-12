package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;
import fr.seb.vectors.Variable;

import java.util.List;

public class Product implements Expression<Variable> {

    final Expression<Variable> a;
    final Expression<Variable> b;

    public Product(Expression<Variable> a, Expression<Variable> b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public List<Expression<Variable>> getChild() {
        return null;
    }

    @Override
    public Expression<Variable> calcul() {
        if (a.calcul() instanceof Scalar && b.calcul() instanceof Scalar) {
            Scalar<Variable> calcula = (Scalar<Variable>) a.calcul();
            Scalar<Variable> calculb = (Scalar<Variable>) b.calcul();
            return new Scalar<>(calcula.n * calculb.n);
        }

        if (a.calcul() instanceof Scalar) {
            Scalar<Variable> calcul = (Scalar<Variable>) a.calcul();
            if (calcul.n == 0) {
                return new Scalar<>(0);
            } else if (calcul.n == 1) {
                return b.calcul();
            }
        }
        if (b.calcul() instanceof Scalar) {
            Scalar<Variable> calcul = (Scalar<Variable>) b.calcul();
            if (calcul.n == 0) {
                return new Scalar<>(0);
            } else if (calcul.n == 1) {
                return a.calcul();
            }
        }

        return new Product(a.calcul(), b.calcul());
    }

    @Override
    public Expression<Variable> derive(Space R) {
        Product left = new Product(a.derive(R), b);
        Product right = new Product(a, b.derive(R));

        return new Addition<>(left, right);
    }

    @Override
    public Expression<Variable> derive(int recursionDepth, Space R) {
        if (recursionDepth == 1) {

            return new Addition<>(new Product(new Derivation<>(a, R), b), new Product(a, new Derivation<>(b, R)));
        } else {
            Product left = new Product(a.derive(recursionDepth - 1, R), b);
            Product right = new Product(a, b.derive(recursionDepth - 1, R));

            return new Addition<>(left, right);
        }
    }

    @Override
    public String toString() {
        return "{" + a.toString() + "}\\times{" + b.toString() + "}";
    }
}
