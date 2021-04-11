package fr.seb.function;

import fr.seb.Expression;
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

        for (Expression<Variable> terms : l) {
            if (terms.hasMinus()) {
                terms.invertSign();
                this.invertSign();
            }

            if (terms.isNull()) {
                this.l = Collections.singletonList(new Scalar(0));
            } else if (terms instanceof Scalar) {
                Scalar s = (Scalar) terms;

                if (s.n == -1) {
                    this.invertSign();
                } else if (s.n != 1) {
                    scalarPart *= s.n;
                }
            } else if (terms instanceof Product) {
                this.l.addAll(terms.getChildren());
            } else {
                this.l.add(terms);
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

        // r not empty because l neither
        //if (r.size() == 1) {
        //    return r.get(0).invertSign();
        //} else {
        if (this.hasMinus()) {
            return new Product(r).invertSign();
        }
        return new Product(r);
        //}
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
                Addition<Variable> a = new Addition<>(s);

                if (this.hasMinus()) {
                    a.invertSign();
                }

                return a;
            }
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
}
