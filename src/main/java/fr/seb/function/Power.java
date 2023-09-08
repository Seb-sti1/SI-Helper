/*
 * Copyright (c) 2023. Seb-sti1. See LICENSE.
 */

package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;
import fr.seb.vectors.Variable;

import java.util.Arrays;
import java.util.List;

public class Power extends Expression<Variable> {

    private final Scalar pow;
    private final Expression<Variable> child;

    public Power(Expression<Variable> f, Scalar pow) throws Error {
        if (f.hasMinus()) {
            this.invertSign();
            this.child = f.getPositiveClone();
        } else {
            this.child = f;
        }

        this.pow = pow;
    }

    public Power(Expression<Variable> f, int pow) {
        if (f.hasMinus()) {
            this.invertSign();
            this.child = f.getPositiveClone();
        } else {
            this.child = f;
        }

        this.pow = new Scalar(pow);
    }

    @Override
    public List<Expression<Variable>> getChildren() {
        return Arrays.asList(child, pow);
    }

    public Scalar getPower() {
        return pow;
    }

    @Override
    public Expression<Variable> calcul() {
        Expression<Variable> R;
        switch (pow.n) {
            case 0:
                R = new Scalar(1);
                break;
            case 1:
                R = child.clone().needToInvertSign(this.hasMinus());
                break;
            default:
                R = this;
        }
        return R;
    }

    @Override
    public Expression<Variable> derive(Space R) {
        return Product.Create(Arrays.asList(pow, child.derive(R), new Power(child, new Scalar(pow.n - 1)))).needToInvertSign(this.hasMinus());
    }

    @Override
    public Expression<Variable> derive(int recursionDepth, Space R) {
        if (recursionDepth == 1) {
            return Product.Create(Arrays.asList(pow, new Derivation<>(child, R), new Power(child, new Scalar(pow.n - 1)))).needToInvertSign(this.hasMinus());
        } else {
            return Product.Create(Arrays.asList(pow, child.derive(recursionDepth - 1, R), new Power(child, new Scalar(pow.n - 1)))).needToInvertSign(this.hasMinus());
        }
    }

    @Override
    public String toString() {
        String s = "";

        if (this.hasMinus()) {
            s = "-";
        }

        s += String.format("(%s)^{%s}", child.toString(), pow.toString());

        return s;
    }

    @Override
    public Expression<Variable> clone() {
        return new Power(this.child, this.pow).setSign(this.hasMinus());
    }

    @Override
    public Expression<Variable> getPositiveClone() {
        return new Power(this.child, this.pow);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Power) {
            return ((Power) o).child.equals(this.child) && ((Power) o).pow == this.pow && this.hasMinus() == ((Power) o).hasMinus();
        }
        return false;
    }

}
