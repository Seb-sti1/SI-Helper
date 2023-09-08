/*
 * Copyright (c) 2023. Seb-sti1. See LICENSE.
 */

package fr.seb.vectors;

import fr.seb.Expression;
import fr.seb.space.Space;

import java.util.ArrayList;
import java.util.List;

public class VectorNull extends Expression<Vector> {

    public VectorNull() {}

    @Override
    public List<Expression<Vector>> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public Expression<Vector> calcul() {
        return this;
    }

    @Override
    public Expression<Vector> derive(Space R) {
        return this;
    }

    @Override
    public Expression<Vector> derive(int recursionDepth, Space R) {
        return this;
    }

    @Override
    public String toString() {
        return "\\vec{0}";
    }

    @Override
    public Expression<Vector> clone() {
        return new VectorNull();
    }

    @Override
    public Expression<Vector> getPositiveClone() {
        return new VectorNull();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof VectorNull;
    }

    @Override
    public Expression<Vector> setSign(boolean hasMinus) {
        return this;
    }

    @Override
    public boolean hasMinus() {
        return false;
    }

    @Override
    public Expression<Vector> invertSign() {
        return this;
    }
}
