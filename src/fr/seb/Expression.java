package fr.seb;

import fr.seb.space.Space;

import java.util.List;

public interface Expression<T> {

    List<Expression<T>> getChild();

    /**
     * Calculate the expression
     * @return the calculated expression
     */
    Expression<T> calcul();

    /**
     * @return latex code of the expression
     */
    String toString();

    /**
     * Derivation of the expression
     * @param R the space (can be null if T is Variable)
     * @return the derivative
     */
    Expression<T> derive(Space R);

    /**
     * Derivation of the expression
     * @param recursionDepth number maximal of recursion
     * @param R the space (can be null if T is Variable)
     * @return the derivative
     */
    Expression<T> derive(int recursionDepth, Space R);
}
