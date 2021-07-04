package fr.seb.function;

import fr.seb.Expression;
import fr.seb.Utils;
import fr.seb.space.Space;
import fr.seb.vectors.Variable;
import fr.seb.vectors.Vector;
import fr.seb.vectors.VectorNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Addition<T> extends Expression<T> {

    public List<Expression<T>> l;

    private Addition(List<Expression<T>> l) {
        this.l = l;
    }

    /**
     * For an element in the list :
     * - add children it's an addition (after cleaning them)
     * - remove the element if null (according to the isNull property)
     *
     * @param l   the list to clean
     * @param <U> either Variable or Vector
     * @return a clean list
     */
    protected static <U> List<Expression<U>> cleanListOfExpressions(List<Expression<U>> l) {
        List<Expression<U>> r = new ArrayList<>();

        for (Expression<U> ele : l) {
            if (!ele.isNull()) {
                if (ele instanceof Addition) {
                    // add sub element with the right sign
                    cleanListOfExpressions(ele.getChildren()).forEach(subEle -> r.add(subEle.clone().needToInvertSign(ele.hasMinus())));
                } else {
                    r.add(ele);
                }
            }
        }

        return r;
    }

    /**
     * Create an addition of vector (after cleaning the list)
     *
     * @param l the list of vector to add
     * @return the result (type might not be Addition)
     */
    public static Expression<Vector> CreateVector(List<Expression<Vector>> l) {
        List<Expression<Vector>> r = cleanListOfExpressions(l);

        if (r.size() == 0) {
            return new VectorNull();
        } else if (r.size() == 1) {
            return r.get(0).clone();
        } else {
            return new Addition<>(r);
        }
    }

    /**
     * Create a addition with only two terms
     *
     * @param a the first term
     * @param b the second term
     * @return the addition object
     */
    public static Expression<Vector> CreateVector(Expression<Vector> a, Expression<Vector> b) {
        return CreateVector(Arrays.asList(a, b));
    }

    /**
     * Create an addition of variable (after cleaning the list)
     *
     * @param l the list of variable to add
     * @return the result (type might not be Addition)
     */
    public static Expression<Variable> CreateVariable(List<Expression<Variable>> l) {
        List<Expression<Variable>> r = new ArrayList<>();
        int scalarPart = 0;

        for (Expression<Variable> ele : cleanListOfExpressions(l)) {
            if (ele instanceof Scalar) {
                scalarPart += ((Scalar) ele).n;
            } else {
                r.add(ele);
            }
        }

        if (r.size() == 0) {
            return new Scalar(scalarPart);
        } else if (r.size() == 1 && scalarPart == 0) {
            return r.get(0);
        } else {
            r.add(new Scalar(scalarPart));
            return new Addition<>(r);
        }
    }


    /**
     * Create a addition with only two terms
     *
     * @param a the first term
     * @param b the second term
     * @return the addition object
     */
    public static Expression<Variable> CreateVariable(Expression<Variable> a, Expression<Variable> b) {
        return CreateVariable(Arrays.asList(a, b));
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

        r = cleanListOfExpressions(r);

        if (r.size() == 0) {
            if (this.isVectorial()) {
                return (Expression<T>) new VectorNull(); // todo : try to constrain T
            } else {
                return (Expression<T>) new Scalar(0);
            }
        } else if (r.size() == 1) {
            return r.get(0).clone().needToInvertSign(this.hasMinus());
        } else {
            return new Addition<>(r).needToInvertSign(this.hasMinus());
        }

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

            r = cleanListOfExpressions(r);

            if (r.size() == 0) {
                if (this.isVectorial()) {
                    return (Expression<T>) new VectorNull(); //todo : try to constrain T
                } else {
                    return (Expression<T>) new Scalar(0);
                }
            } else if (r.size() == 1) {
                return r.get(0).clone().needToInvertSign(this.hasMinus());
            } else {
                return new Addition<>(r).needToInvertSign(this.hasMinus());
            }
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
            s.append(e);

            first = false;
        }

        if (this.l.size() > 1) {
            s.append(")");
        }

        return s.toString();
    }

    @Override
    public Expression<T> clone() {
        return new Addition<>(this.l).setSign(this.hasMinus());
    }

    @Override
    public Expression<T> getPositiveClone() {
        return new Addition<>(this.l);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Addition) {
            if (this.isVectorial() == ((Addition<?>) o).isVectorial()) { // check if the T in the same for both additions
                return Utils.listEqualsIgnoreOrders(this.l, ((Addition<T>) o).l)
                        && this.hasMinus() == ((Addition<?>) o).hasMinus();
            }
        }

        return false;
    }
}
