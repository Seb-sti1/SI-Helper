package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;
import fr.seb.Utils;
import fr.seb.vectors.Vector;
import fr.seb.vectors.VectorNull;

import java.util.List;

public class WedgeProduct implements Expression<Vector> {

    Expression<Vector> left, right;

    public WedgeProduct(Expression<Vector> left, Expression<Vector> right) {
        this.left = left;
        this.right = right;
    }


    @Override
    public List<Expression<Vector>> getChild() {
        return null;
    }

    @Override
    public Expression<Vector> calcul() {
        if (this.left instanceof Addition) {
            Addition<Vector> a = (Addition<Vector>) this.left;

            return new Addition<>(new WedgeProduct(a.left, this.right), new WedgeProduct(a.right, this.right)).calcul();
        } else if (this.left instanceof ScalarProduct) {
            ScalarProduct pe = (ScalarProduct) this.left;

            return new ScalarProduct(pe.scalaire, new WedgeProduct(pe.vecteur, this.right)).calcul();
        }

        if (this.right instanceof Addition) {
            Addition<Vector> a = (Addition<Vector>) this.right;

            return new Addition<>(new WedgeProduct(this.left, a.left), new WedgeProduct(this.left, a.right)).calcul();
        } else if (this.right instanceof ScalarProduct) {
            ScalarProduct pe = (ScalarProduct) this.right;

            return new ScalarProduct(pe.scalaire, new WedgeProduct(pe.vecteur, this.left)).calcul();
        }

        if (this.left instanceof VectorNull || this.right instanceof VectorNull ) {
            return new VectorNull();
        }

        if (this.right instanceof Vector && this.left instanceof Vector) {
            if (this.right == this.left) {
                return new VectorNull();
            } else {
                return Utils.gammaRule((Vector) this.left, (Vector) this.right);
            }
        }

        return this;
    }

    @Override
    public Expression<Vector> derive(Space R) {
        System.out.println("C'est la groooosse merde");
        return null;
    }

    @Override
    public Expression<Vector> derive(int recursionDepth, Space R) {
        System.out.println("Si vous voyez ce message... et baaah... courrez");
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s \\land %s", left.toString(), right.toString());
    }
}
