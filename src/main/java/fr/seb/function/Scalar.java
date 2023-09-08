/*
 * Copyright (c) 2023. Seb-sti1. See LICENSE.
 */

package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;
import fr.seb.vectors.Variable;

import java.util.List;

public class Scalar extends Expression<Variable> {

    public int n;

    public Scalar(int n) {
        if (n < 0) {
            this.invertSign();
            this.n = -n;
        } else {
            this.n = n;
        }
    }

    @Override
    public List<Expression<Variable>> getChildren() {
        return null;
    }

    @Override
    public Expression<Variable> calcul() {
        return this;
    }

    @Override
    public Expression<Variable> derive(Space R) {
        return new Scalar(0);
    }

    @Override
    public Expression<Variable> derive(int recursionDepth, Space R) {
        if (recursionDepth == 0) {
            return this;
        } else if (recursionDepth == 1) {
            return new Derivation<>(this, R).needToInvertSign(this.hasMinus());
        } else {
            return new Scalar(0);
        }
    }

    @Override
    public String toString() {
        if (this.hasMinus()) {
            return "-" + this.n;
        } else {
            return String.valueOf(this.n);
        }
    }

    @Override
    public Expression<Variable> clone() {
        return new Scalar(this.n).setSign(this.hasMinus());
    }

    @Override
    public Expression<Variable> getPositiveClone() {
        return new Scalar(this.n);
    }

    @Override
    public boolean isNull() {
        return n == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Scalar) {
            return ((Scalar) o).n == this.n && this.hasMinus() == ((Scalar) o).hasMinus();
        }


        return false;
    }
}
