package fr.seb;

import fr.seb.function.Scalar;
import fr.seb.function.WedgeProduct;
import fr.seb.space.Space;
import fr.seb.vectors.VectorNull;

import java.util.List;

public abstract class Expression<T> {

    boolean hasMinus = false;

    /**
     * Calculate the expression
     *
     * @return the calculated expression
     */
    public abstract Expression<T> calcul();

    /**
     * If the expression a minus before itself
     *
     * @return true or false;
     */
    public boolean hasMinus() {
        return hasMinus;
    }

    /**
     * Invert the sign of the expression
     */
    public Expression<T> invertSign() {
        hasMinus = !hasMinus;
        return this;
    }

    /**
     * If the expression is null
     *
     * @return if the expression is null
     */
    public boolean isNull() {
        return (this instanceof Scalar && ((Scalar) this).n == 0) || (this instanceof VectorNull);
    }

    /**
     * If it's an vector expression
     *
     * @return isVectorial
     */
    public boolean isVectorial() {
        return this instanceof Scalar || this instanceof WedgeProduct;
    }

    /**
     * Derivation of the expression
     *
     * @param R the space (can be null if T is Variable)
     * @return the derivative
     */
    public abstract Expression<T> derive(Space R);

    /**
     * Derivation of the expression
     *
     * @param recursionDepth number maximal of recursion (-1 is the same as derive(Space R); 0 is doing nothing)
     * @param R              the space (can be null if T is Variable)
     * @return the derivative
     */
    public abstract Expression<T> derive(int recursionDepth, Space R);

    /**
     * List the child of the expression (NOT RECURSE !)
     *
     * @return the child of the expression
     */
    public abstract List<Expression<T>> getChildren();

    /**
     * @return latex code of the expression
     */
    public abstract String toString();

}
