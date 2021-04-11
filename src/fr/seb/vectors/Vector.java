package fr.seb.vectors;


import fr.seb.Expression;
import fr.seb.Utils;
import fr.seb.function.WedgeProduct;
import fr.seb.space.Space;

import java.util.List;

public class Vector extends Expression<Vector> {

    final String name;
    final int indice;
    final Space R;

    Expression<Vector> vectorExpression = null;

    public Vector(String name, int indice, Space R) {
        this.name = name;
        this.indice = indice;
        this.R = R;
    }

    @Override
    public List<Expression<Vector>> getChildren() {
        return null;
    }

    @Override
    public Expression<Vector> calcul() {
        return this;
    }

    public void setExpression(Expression<Vector> value) {
        this.vectorExpression = value;
    }

    public Expression<Vector> getExpression() {
        return vectorExpression;
    }

    @Override
    public Expression<Vector> derive(Space R) {
        if (this.R == R) {
            return new VectorNull();
        } else {
            return new WedgeProduct(Utils.getProjectionVector(this.R, R), this);
        }
    }

    @Override
    public Expression<Vector> derive(int recursionDepth, Space R) {
        return this.derive(R);
    }

    public Space getSpace() {
        return this.R;
    }

    @Override
    public String toString() {
        return String.format("\\overrightarrow{%s_{%s}}", this.name, this.indice);
    }

    @Override
    public Expression<Vector> invertSign() {
        System.out.println("Warming inverted sign of vector");
        return super.invertSign();
    }

}
