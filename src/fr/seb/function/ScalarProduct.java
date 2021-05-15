package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;
import fr.seb.vectors.Variable;
import fr.seb.vectors.Vector;
import fr.seb.vectors.VectorNull;

import java.util.ArrayList;
import java.util.List;

public class ScalarProduct extends Expression<Vector> {

    public final Expression<Variable> scalar;
    public final Expression<Vector> vector;

    public ScalarProduct(Expression<Variable> scalar, Expression<Vector> vector) {
        List<Expression<Variable>> scalars = new ArrayList<>();

        if (scalar.hasMinus()) {
            this.invertSign();
            scalars.add(scalar.getPositiveClone());
        } else {
            scalars.add(scalar);
        }

        Expression<Vector> vec = vector;

        if (vector.hasMinus()) {
            this.invertSign();
            vec = vector.getPositiveClone();
        }


        while (vec instanceof ScalarProduct) {
            if (vec.hasMinus()) {
                this.invertSign();
            }

            scalars.add(((ScalarProduct) vec).scalar);
            vec = ((ScalarProduct) vec).vector;
        }

        this.scalar = new Product(scalars);
        this.vector = vec;
    }


    @Override
    public List<Expression<Vector>> getChildren() {
        return null;
    }

    @Override
    public Expression<Vector> calcul() {
        Expression<Variable> calculatedScalar = scalar.calcul();
        Expression<Vector> calculatedVector = vector.calcul();

        if (calculatedScalar instanceof Scalar && ((Scalar) calculatedScalar).n == 0) {
            return new VectorNull();
        } else if (calculatedScalar instanceof Scalar && ((Scalar) calculatedScalar).n == 1) {
            return calculatedVector.clone().needToInvertSign(this.hasMinus());
        }

        if (calculatedVector instanceof VectorNull) {
            return new VectorNull();
        }

        if (calculatedVector instanceof ScalarProduct) {
            ScalarProduct sp = (ScalarProduct) calculatedVector;

            return new ScalarProduct(Product.Create(calculatedScalar, sp.scalar), sp.vector).needToInvertSign(sp.hasMinus() ^ this.hasMinus()).calcul();
        }

        return new ScalarProduct(calculatedScalar, calculatedVector).needToInvertSign(this.hasMinus());
    }

    @Override
    public Expression<Vector> derive(Space R) {
        return Addition.CreateVector(new ScalarProduct(scalar.derive(R), vector), new ScalarProduct(scalar, vector.derive(R))).needToInvertSign(this.hasMinus());
    }

    @Override
    public Expression<Vector> derive(int recursionDepth, Space R) {
        if (recursionDepth == 0) {
            return this;
        } else if (recursionDepth == 1) {
            return Addition.CreateVector(new ScalarProduct(new Derivation<>(scalar, R), vector), new ScalarProduct(scalar, new Derivation<>(vector, R))).needToInvertSign(this.hasMinus());
        } else {
            return Addition.CreateVector(new ScalarProduct(scalar.derive(recursionDepth - 1, R), vector), new ScalarProduct(scalar, vector.derive(recursionDepth - 1, R))).needToInvertSign(this.hasMinus());
        }
    }

    @Override
    public boolean isNull() {
        return scalar.isNull() || vector.isNull();
    }


    @Override
    public String toString() {
        if (this.hasMinus()) {
            return String.format("- %s \\cdot %s", scalar.toString(), vector.toString());
        }
        return String.format("%s \\cdot %s", scalar.toString(), vector.toString());
    }

    @Override
    public Expression<Vector> clone() {
        return new ScalarProduct(this.scalar, this.vector).setSign(this.hasMinus());
    }

    @Override
    public Expression<Vector> getPositiveClone() {
        return new ScalarProduct(this.scalar, this.vector);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ScalarProduct) {
            return this.scalar.equals(((ScalarProduct) o).scalar) && this.vector.equals(((ScalarProduct) o).vector)
                    && this.hasMinus() == ((ScalarProduct) o).hasMinus();
        }

        return false;
    }
}
