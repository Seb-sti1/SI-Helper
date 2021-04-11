package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;
import fr.seb.vectors.Variable;
import fr.seb.vectors.Vector;
import fr.seb.vectors.VectorNull;

import java.util.List;

public class ScalarProduct extends Expression<Vector> {

    public final Expression<Variable> scalaire;
    public final Expression<Vector> vecteur;

    public ScalarProduct(Expression<Variable> scalaire, Expression<Vector> vecteur) {
        this.scalaire = scalaire;
        this.vecteur = vecteur;
    }


    @Override
    public List<Expression<Vector>> getChildren() {
        return null;
    }

    @Override
    public Expression<Vector> calcul() {
        if (scalaire instanceof Scalar && ((Scalar) scalaire).n == 0) {
            return new VectorNull();
        } else if (scalaire instanceof Scalar && ((Scalar) scalaire).n == 1) {
            return vecteur.calcul();
        }

        if (vecteur.calcul() instanceof VectorNull) {
            return new VectorNull();
        }

        if (vecteur.calcul() instanceof ScalarProduct) {
            ScalarProduct calculated = (ScalarProduct) vecteur.calcul();

            return new ScalarProduct(Product.Create(this.scalaire, calculated.scalaire), calculated.vecteur).calcul();
        }

        return new ScalarProduct(scalaire.calcul(), vecteur.calcul());
    }

    @Override
    public Expression<Vector> derive(Space R) {
        return Addition.CreateVector(new ScalarProduct(scalaire.derive(R), vecteur), new ScalarProduct(scalaire, vecteur.derive(R)));
    }

    @Override
    public Expression<Vector> derive(int recursionDepth, Space R) {
        if (recursionDepth == 0) {
            return this;
        } else if (recursionDepth == 1) {
            return Addition.CreateVector(new ScalarProduct(new Derivation<>(scalaire, R), vecteur), new ScalarProduct(scalaire, new Derivation<>(vecteur, R)));
        } else {
            return Addition.CreateVector(new ScalarProduct(scalaire.derive(recursionDepth - 1, R), vecteur), new ScalarProduct(scalaire, vecteur.derive(recursionDepth - 1, R)));
        }
    }

    @Override
    public String toString() {
        return String.format("%s \\times %s", scalaire.toString(), vecteur.toString());
    }
}
