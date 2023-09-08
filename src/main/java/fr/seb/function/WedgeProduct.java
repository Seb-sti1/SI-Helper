package fr.seb.function;

import fr.seb.Expression;
import fr.seb.Utils;
import fr.seb.space.Space;
import fr.seb.vectors.Vector;
import fr.seb.vectors.VectorNull;

import java.util.ArrayList;
import java.util.List;

public class WedgeProduct extends Expression<Vector> {

    Expression<Vector> left, right;

    public WedgeProduct(Expression<Vector> left, Expression<Vector> right) {
        if (left.hasMinus()) {
            this.invertSign();
            this.left = left.getPositiveClone();
        } else {
            this.left = left;
        }

        if (right.hasMinus()) {
            this.invertSign();
            this.right = right.getPositiveClone();
        } else {
            this.right = right;
        }

    }


    @Override
    public List<Expression<Vector>> getChildren() {
        return null;
    }

    @Override
    public Expression<Vector> calcul() {
        if (this.left instanceof Addition) {
            Addition<Vector> add = (Addition<Vector>) this.left;

            List<Expression<Vector>> list = new ArrayList<>();

            for (Expression<Vector> child : add.getChildren()) {
                list.add(new WedgeProduct(child, this.right));
            }

            return Addition.CreateVector(list).needToInvertSign(this.hasMinus()).calcul();
        } else if (this.left instanceof ScalarProduct) {
            ScalarProduct pe = (ScalarProduct) this.left;

            return new ScalarProduct(pe.scalar, new WedgeProduct(pe.vector, this.right)).needToInvertSign(this.hasMinus()).calcul();
        }

        if (this.right instanceof Addition) {
            Addition<Vector> add = (Addition<Vector>) this.right;

            List<Expression<Vector>> list = new ArrayList<>();

            for (Expression<Vector> child : add.getChildren()) {
                list.add(new WedgeProduct(this.left, child));
            }

            return Addition.CreateVector(list).needToInvertSign(this.hasMinus()).calcul();
        } else if (this.right instanceof ScalarProduct) {
            ScalarProduct pe = (ScalarProduct) this.right;

            return new ScalarProduct(pe.scalar, new WedgeProduct(this.left, pe.vector)).needToInvertSign(this.hasMinus()).calcul();
        }

        if (this.left instanceof VectorNull || this.right instanceof VectorNull ) {
            return new VectorNull();
        }

        if (this.right instanceof Vector && this.left instanceof Vector) {
            if (this.right == this.left) {
                return new VectorNull();
            } else {
                return Utils.gammaRule((Vector) this.left, (Vector) this.right).needToInvertSign(this.hasMinus());
            }
        }

        return new WedgeProduct(left.calcul(), right.calcul());
    }


    @Override
    public Expression<Vector> derive(Space R) {
        return Addition.CreateVector(new WedgeProduct(this.left.derive(R), this.right),
                new WedgeProduct(this.left, this.right.derive(R)));
    }

    @Override
    public Expression<Vector> derive(int recursionDepth, Space R) {
        return Addition.CreateVector(new WedgeProduct(this.left.derive(recursionDepth - 1, R), this.right),
                new WedgeProduct(this.left, this.right.derive(recursionDepth - 1, R)));
    }

    @Override
    public String toString() {
        if (this.hasMinus()) {
            return String.format("- (%s \\land %s)", left.toString(), right.toString());
        }

        return String.format("(%s \\land %s)", left.toString(), right.toString());
    }

    @Override
    public boolean isNull() {
        return this.left.isNull() || this.right.isNull();
    }

    @Override
    public Expression<Vector> clone() {
        return new WedgeProduct(this.left, this.right).setSign(this.hasMinus());
    }

    @Override
    public Expression<Vector> getPositiveClone() {
        return new WedgeProduct(this.left, this.right);
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof WedgeProduct) {
            return this.left.equals(((WedgeProduct) o).left) && this.right.equals(((WedgeProduct) o).right) && this.hasMinus() == ((WedgeProduct) o).hasMinus();
        }

        return false;
    }
}
