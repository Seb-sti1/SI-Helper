package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;
import fr.seb.vectors.Variable;

import java.util.Collections;
import java.util.List;

public class Cos extends Expression<Variable> {

    final Expression<Variable> child;

    public Cos(Expression<Variable> param) {
        this.child = param;
    }


    @Override
    public List<Expression<Variable>> getChildren() {
        return Collections.singletonList(child);
    }

    @Override
    public Expression<Variable> calcul() {
        if (child.calcul().isNull()) {
            return new Scalar(1);
        }


        return new Cos(child.calcul()).needToInvertSign(this.hasMinus());
    }

    @Override
    public Expression<Variable> derive(Space R) {
        return Product.Create(child.derive(R), new Sin(child)).needToInvertSign(!this.hasMinus()); // ! this.hasMinus() because the derivative of cos if -sin
    }

    @Override
    public Expression<Variable> derive(int recursionDepth, Space R) {
        if (recursionDepth == 1) {
            return Product.Create(new Derivation<>(child, R), new Sin(child)).needToInvertSign(!this.hasMinus());
        } else {
            return Product.Create(child.derive(recursionDepth - 1, R), new Sin(child)).needToInvertSign(!this.hasMinus());
        }
    }

    @Override
    public String toString() {
        String s = "";

        if (this.hasMinus()) {
            s = "-";
        }

        s += String.format("\\cos(%s)", child.toString());

        return s;
    }

    @Override
    public Expression<Variable> clone() {
        return new Cos(this.child).setSign(this.hasMinus());
    }

    @Override
    public Expression<Variable> getPositiveClone() {
        return new Cos(this.child);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Cos) {
            return this.child.equals(((Cos) o).child) && this.hasMinus() == ((Cos) o).hasMinus();
        }
        return false;
    }
}
