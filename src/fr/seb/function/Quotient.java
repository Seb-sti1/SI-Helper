package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;
import fr.seb.vectors.Variable;

import java.util.Arrays;
import java.util.List;

@Deprecated
public class Quotient implements Expression<Variable> {

    final Expression<Variable> p;
    Expression<Variable> q;

    public Quotient(Expression<Variable> p, Expression<Variable> q) throws Error {
        this.p = p;

        if (q instanceof Scalar && ((Scalar<Variable>) q).n == 0) {
            throw new Error("Can't divide by zero !");
        } else {
            this.q = q;
        }
    }


    @Override
    public List<Expression<Variable>> getChild() {
        return Arrays.asList(p, q);
    }

    @Override
    public Expression<Variable> calcul() {
        return null;
    }

    @Override
    public Expression<Variable> derive(Space R) {
        Expression<Variable> numerateur = new Addition<>(new Product(p.derive(R), q), new Product(q.derive(R), p));
        Expression<Variable> denominateur = new Product(q, q);

        return new Quotient(numerateur, denominateur);
    }

    @Override
    public Expression<Variable> derive(int recursionDepth, Space R) {
        return null;
    }

    @Override
    public String toString() {
        return "\\frac{" + p.toString() + "}{" + q.toString() + "}";
    }
}
