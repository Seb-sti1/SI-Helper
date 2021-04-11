package fr.seb.vectors;

import fr.seb.Expression;
import fr.seb.space.Space;

import java.util.List;

public class VectorNull extends Expression<Vector> {

    public VectorNull() {}

    @Override
    public List<Expression<Vector>> getChildren() {
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

    @Override
    public Expression<Vector> invertSign() {
        System.out.println("Warming inverted sign of vector null");
        return super.invertSign();
    }
}
