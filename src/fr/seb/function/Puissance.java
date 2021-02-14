package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;
import fr.seb.vectors.Variable;

import java.util.Arrays;
import java.util.List;

@Deprecated
public class Puissance implements Expression<Variable> {

    private final Quotient pow;
    private final Expression<Variable> child;

    public Puissance(Expression<Variable> f, Quotient pow) throws Error {
        this.child = f;

        if (pow.getChild().get(0) instanceof Scalar && pow.getChild().get(1) instanceof Scalar) {
            this.pow = pow;
        } else {
            throw new Error("Impossible de creer une fonction Puissance avec une telle puissance : " + pow.toString());
        }
    }

    @Override
    public List<Expression<Variable>> getChild() {
        return Arrays.asList(child, pow);
    }

    public Quotient getPower() {
        return pow;
    }

    @Override
    public Expression<Variable> calcul() {
        /*Expression R;
        switch (p) {
            case 0:
                R = new Scalar(1);
                break;
            case 1:
                R = child;
                break;
            default:
                R = this;
        }
        return R;*/
        return null;
    }

    @Override
    public Expression<Variable> derive(Space R) {
        Quotient newpower = new Quotient(new Scalar<>(((Scalar<Variable>) pow.p).n - ((Scalar<Variable>) pow.q).n), pow.q);

        return new Product(pow, new Product(child.derive(R), new Puissance(child, newpower)));
    }

    @Override
    public Expression<Variable> derive(int recursionDepth, Space R) {
        return null;
    }

    @Override
    public String toString() {
        return "{" + child.toString() + "}^{" + pow.toString() + "}";
    }

}
