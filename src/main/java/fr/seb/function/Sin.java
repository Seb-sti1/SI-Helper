/*
 * Copyright (c) 2023. Seb-sti1. See LICENSE.
 */

package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;
import fr.seb.vectors.Variable;

import java.util.Collections;
import java.util.List;

public class Sin extends Expression<Variable> {

    final Expression<Variable> child;

    public Sin(Expression<Variable> param) {
        this.child = param;
    }


    @Override
    public List<Expression<Variable>> getChildren() {
        return Collections.singletonList(child);
    }

    @Override
    public Expression<Variable> calcul() {
        Expression<Variable> childCalc = child.calcul();

        if (childCalc.isNull()) {
            return new Scalar(0);
        }

        return new Sin(childCalc).needToInvertSign(this.hasMinus());
    }

    @Override
    public Expression<Variable> derive(Space R) {
        return Product.Create(child.derive(R), new Cos(child)).needToInvertSign(this.hasMinus());
    }

    @Override
    public Expression<Variable> derive(int recursionDepth, Space R) {
        if (recursionDepth == 0) {
            return this;
        } else if (recursionDepth == 1) {
            return Product.Create(new Derivation<>(child, R), new Cos(child)).needToInvertSign(this.hasMinus());
        } else {
            return Product.Create(child.derive(recursionDepth - 1, R), new Cos(child)).needToInvertSign(this.hasMinus());
        }
    }

    @Override
    public String toString() {
        String s = "";

        if (this.hasMinus()) {
            s = "-";
        }

        s += String.format("\\sin(%s)", child.toString());

        return s;
    }

    @Override
    public Expression<Variable> clone() {
        return new Sin(this.child).setSign(this.hasMinus());
    }

    @Override
    public Expression<Variable> getPositiveClone() {
        return new Sin(this.child);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Sin) {
            return this.child.equals(((Sin) o).child) && this.hasMinus() == ((Sin) o).hasMinus();
        }

        return false;
    }
}
