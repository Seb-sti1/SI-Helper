package fr.seb.vectors;

import fr.seb.Expression;
import fr.seb.space.Space;

import java.util.List;

public class VectorNull implements Expression<Vector> {

    public VectorNull() {}

    @Override
    public List<Expression<Vector>> getChild() {
        return null;
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
}
