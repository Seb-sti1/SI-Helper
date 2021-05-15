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
    List<Expression<Variable>> l;

    public Product(List<Expression<Variable>> l) {
        this.l = new ArrayList<>();

        int scalarPart = 1;

        for (Expression<Variable> term : l) {

            if (term.isNull()) {
                this.l = Collections.singletonList(new Scalar(0));
                break;
            } else {
                Expression<Variable> positiveTerm = term;

                if (term.hasMinus()) {
                    positiveTerm = term.getPositiveClone();
                    this.invertSign();
                }

                if (positiveTerm instanceof Scalar) {
                    Scalar s = (Scalar) positiveTerm;

                    if (s.n == -1) {
                        this.invertSign();
                    } else if (s.n != 1) {
                        scalarPart *= s.n;
                    }
                } else if (positiveTerm instanceof Product) {
                    this.l.addAll(positiveTerm.getChildren());
                } else {
                    this.l.add(positiveTerm);
                }
            }
        }

        if (scalarPart != 1 || this.l.size() == 0) {
            this.l.add(new Scalar(scalarPart));
        }

    }

    /**
     * Create a product with only two terms
     *
     * @param a the first term
     * @param b the second term
     * @return the product object
     */
    public static Product Create(Expression<Variable> a, Expression<Variable> b) {
        return new Product(Arrays.asList(a, b));
    }

    @Override
    public List<Expression<Variable>> getChildren() {
        return this.l;
    }

    @Override
    public Expression<Variable> calcul() {
        List<Expression<Variable>> r = new ArrayList<>();

        for (Expression<Variable> terms : this.l) {
            Expression<Variable> termsCalculated = terms.calcul();

            r.add(termsCalculated);
        }

        if (r.size() == 1) { // Todo : this is imperfect : r.size could be > 1 but the product created could have only one term (example if r = {1,1,-1})
            return r.get(0).clone().needToInvertSign(this.hasMinus());
        } else {
            return new Product(r).needToInvertSign(this.hasMinus());
        }
    }

    @Override
    public Expression<Variable> derive(Space R) {
        return derive(-1, R);
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

                    s.add(new Product(p));
                }
            }

            if (s.size() == 0) {
                return new Scalar(0);
            } else {
                return new Addition<>(s).needToInvertSign(this.hasMinus());
            }
        }
    }

    @Override
    public boolean isNull() {
        return l.get(0).isNull();
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
