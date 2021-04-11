package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;
import fr.seb.vectors.Variable;
import fr.seb.vectors.Vector;
import fr.seb.vectors.VectorNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Addition<T> extends Expression<T> {

    public List<Expression<T>> l;

    public Addition(List<Expression<T>> l) {
        if (l.size() == 0) {
            throw new Error("Can't create addition with no term");
        }

        this.l = new ArrayList<>();

        int scalarPart = 0;

        for (Expression<T> ele : l) {

            if (!ele.isNull()) {

                if (ele instanceof Addition) {
                    this.l.addAll(ele.getChildren());
                } else if (ele instanceof Scalar) {
                    scalarPart += ((Scalar) ele).n;
                } else {
                    this.l.add(ele);
                }
            }
        }

        if (scalarPart != 0) {
            this.l.add((Expression<T>) new Scalar(scalarPart)); // work because no scalar part with vector
        }

        if (this.l.size() == 0) {
            if (l.get(0).isVectorial()) { // verify for cast
                this.l.add((Expression<T>) new VectorNull());
            } else {
                this.l.add((Expression<T>) new Scalar(0));
            }
        }

    }

    /**
     * Create a addition with only two terms
     *
     * @param a the first term
     * @param b the second term
     * @return the addition object
     */
    public static Addition<Vector> CreateVector(Expression<Vector> a, Expression<Vector> b) {
        return new Addition<>(Arrays.asList(a, b));
    }

    /**
     * Create a addition with only two terms
     *
     * @param a the first term
     * @param b the second term
     * @return the addition object
     */
    public static Addition<Variable> CreateVariable(Expression<Variable> a, Expression<Variable> b) {
        return new Addition<>(Arrays.asList(a, b));
    }

    @Override
    public List<Expression<T>> getChildren() {
        return this.l;
    }

    @Override
    public Expression<T> calcul() {
        List<Expression<T>> r = new ArrayList<>();

        for (Expression<T> e : this.l) {
            r.add(e.calcul());
        }

        // r not empty because l neither
        //if (r.size() == 1) {
        //    return r.get(0).invertSign();
        //} else {
        if (this.hasMinus()) {
            return new Addition<>(r).invertSign();
        }

        return new Addition<>(r);
        //}

    }

    @Override
    public Expression<T> derive(Space R) {
        return derive(-1, R);
    }

    @Override
    public Expression<T> derive(int recursionDepth, Space R) {
        if (recursionDepth == 0) {
            return this;
        } else {
            List<Expression<T>> r = new ArrayList<>();

            for (Expression<T> e : this.l) {
                switch (recursionDepth) {
                    case -1:
                    r.add(e.derive(R));
                        break;
                    case 1:
                    r.add(new Derivation<>(e, R));
                        break;
                    default:
                    r.add(e.derive(recursionDepth - 1, R));
                        break;
                }
            }

            Addition<T> a = new Addition<>(r);

            if (this.hasMinus()) {
                a.invertSign();
            }

            return a;
        }
    }

    @Override
    public boolean isVectorial() {
        return this.l.get(0).isVectorial();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        if (this.hasMinus()) {
            s.append("- ");
        }

        if (this.l.size() > 1) {
            s.append("(");
        }

        boolean first = true;

        for (Expression<T> e : this.l) {
            if (e.hasMinus()) {
                s.append(" ");
            } else if (!first) {
                s.append(" + ");
            }
            s.append(e.toString());

            first = false;
        }

        if (this.l.size() > 1) {
            s.append(")");
        }

        return s.toString();
    }
}
