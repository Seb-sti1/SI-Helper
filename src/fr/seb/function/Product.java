package fr.seb.function;

import fr.seb.Expression;
import fr.seb.Utils;
import fr.seb.space.Space;
import fr.seb.vectors.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Product extends Expression<Variable> {

    private final List<Expression<Variable>> l;

    private Product(List<Expression<Variable>> l) {
        this.l = l;
    }

    /**
     * Clean a list of Expression to create a clean product
     * - Only one scalar
     * - all sub elements are positive
     * Todo : use power to simplify expression
     *
     * @param list the list to clean
     * @return a clean list of element. The sign is on the first element
     */
    private static cleanList cleanListOfExpressions(List<Expression<Variable>> list) {
        List<Expression<Variable>> r = new ArrayList<>();

        boolean hasMinus = false;
        int scalarPart = 1;

        for (Expression<Variable> term : list) {
            if (term.isNull()) {
                return new cleanList(Collections.singletonList(new Scalar(0)), false); // if a term is null then the product too
            } else {
                Expression<Variable> positiveTerm = term;

                if (term.hasMinus()) { // remove the signe
                    positiveTerm = term.getPositiveClone(); // take a clone only if needed
                    hasMinus = !hasMinus;
                }

                if (positiveTerm instanceof Scalar) {
                    scalarPart *= ((Scalar) positiveTerm).n; // Scalar.n is positive by construction
                } else if (positiveTerm instanceof Product) {
                    cleanList cList = cleanListOfExpressions(positiveTerm.getChildren());

                    hasMinus = hasMinus ^ cList.hasMinus; // there is a minus only one the two (exactly) has one
                    r.addAll(cList.l);
                } else {
                    r.add(positiveTerm);
                }
            }
        }

        if (r.size() == 0) {
            return new cleanList(Collections.singletonList(new Scalar(scalarPart)), false);
        } else {
            if (scalarPart != 1) {
                r.add(new Scalar(scalarPart));
            }
            return new cleanList(r, hasMinus);
        }
    }

    /**
     * Create a product with a list of term
     *
     * @param l the list of term
     * @return the product
     */
    public static Expression<Variable> Create(List<Expression<Variable>> l) {
        cleanList cList = cleanListOfExpressions(l);

        if (cList.l.size() == 1) {
            return cList.l.get(0).clone().needToInvertSign(cList.hasMinus);
        } else {
            return new Product(cList.l).needToInvertSign(cList.hasMinus);
        }
    }

    /**
     * Create a product with only two terms
     *
     * @param a the first term
     * @param b the second term
     * @return the product
     */
    public static Expression<Variable> Create(Expression<Variable> a, Expression<Variable> b) {
        return Create(Arrays.asList(a, b));
    }

    @Override
    public Expression<Variable> calcul() {
        List<Expression<Variable>> r = new ArrayList<>();

        for (Expression<Variable> terms : this.l) {
            Expression<Variable> termsCalculated = terms.calcul();

            r.add(termsCalculated);
        }

        cleanList cList = cleanListOfExpressions(r);

        if (cList.l.size() == 1) {
            return cList.l.get(0).clone().needToInvertSign(this.hasMinus() ^ cList.hasMinus);
        } else {
            return new Product(cList.l).needToInvertSign(this.hasMinus());
        }
    }

    @Override
    public List<Expression<Variable>> getChildren() {
        return this.l;
    }

    @Override
    public Expression<Variable> derive(int recursionDepth, Space R) {
        if (recursionDepth == 0) {
            return this;
        } else {
            List<Expression<Variable>> s = new ArrayList<>();

            for (Expression<Variable> terms : this.l) {
                List<Expression<Variable>> p =  new ArrayList<>();

                Expression<Variable> derivTerms = terms.derive(recursionDepth - 1, R);

                if (recursionDepth == 1) {
                    derivTerms = new Derivation<>(terms, R);
                } else if (recursionDepth == -1) {
                    derivTerms = terms.derive(R);
                }

                if (!derivTerms.isNull()) {
                    for (Expression<Variable> v : this.l) {
                        if (v != terms) {
                            p.add(v);
                        }
                    }
                    p.add(derivTerms);

                    s.add(Create(p));
                }
            }

            if (s.size() == 0) {
                return new Scalar(0);
            } else {
                return Addition.CreateVariable(s).needToInvertSign(this.hasMinus());
            }
        }
    }

    @Override
    public Expression<Variable> derive(Space R) {
        return derive(-1, R);
    }

    @Override
    public boolean isNull() {
        for (Expression<Variable> ele : l) {
            if (ele.isNull()) {
                return true;
            }
        }
        return false;
    }

    /**
     * To carry the information of the elements and the sign
     */
    private static class cleanList {
        public final List<Expression<Variable>> l;
        public final boolean hasMinus;

        public cleanList(List<Expression<Variable>> l, boolean hasMinus) {
            this.l = l;
            this.hasMinus = hasMinus;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        if (this.hasMinus()) {
            s.append("- ");
        }

        for (Expression<Variable> v : this.l) {
            s.append(v.toString());
            s.append(" \\times ");
        }

        return s.substring(0, s.length() - 8);
    }

    @Override
    public Expression<Variable> clone() {
        return new Product(this.l).setSign(this.hasMinus());
    }

    @Override
    public Expression<Variable> getPositiveClone() {
        return new Product(this.l);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Product) {
            return Utils.listEqualsIgnoreOrders(this.l, ((Product) o).l) && this.hasMinus() == ((Product) o).hasMinus();
        }

        return false;
    }
}
