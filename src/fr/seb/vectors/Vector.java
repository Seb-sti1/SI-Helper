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
    final Space.VECTOR type;

    Expression<Vector> vectorExpression = null;

    public Vector(String name, int indice, Space R, Space.VECTOR type) {
        this.name = name;
        this.indice = indice;
        this.R = R;
        this.type = type;
    }

    public Space.VECTOR getType() {
        return type;
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
    public Expression<Vector> clone() {
        Vector v = new Vector(this.name, this.indice, this.R, Space.VECTOR.X);
        v.setExpression(this.vectorExpression);

        return v.setSign(this.hasMinus());
    }

    @Override
    public Expression<Vector> getPositiveClone() {
        Vector v = new Vector(this.name, this.indice, this.R, this.type);
        v.setExpression(this.vectorExpression);

        return v;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Vector) {
            return this.name.equals(((Vector) o).name) && this.indice == ((Vector) o).indice && this.R == ((Vector) o).R && this.hasMinus() == ((Vector) o).hasMinus();
        }

        return false;
    }

}
