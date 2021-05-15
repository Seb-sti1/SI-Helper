package fr.seb.vectors;

import fr.seb.Expression;
import fr.seb.function.Scalar;
import fr.seb.space.Space;

import java.util.ArrayList;
import java.util.List;

public class Variable extends Expression<Variable> {

    private final String name;
    private final int successiveDerivativeNull;
    private final int derive;

    /**
     * Create a variable
     * @param name the displayed name
     * @param successiveDerivativeNull the number of successive derivation to get a 0 (example : 1 means the variable is constant)
     */
    public Variable(String name, int successiveDerivativeNull) {
        this.name = name;
        this.successiveDerivativeNull = successiveDerivativeNull;
        this.derive = 0;
    }

    /**
     * Create a variable
     * @param name the displayed name
     * @param successiveDerivativeNull the number of successive derivation to get a 0
     * @param derive the number of successive derivation already applied
     */
    public Variable(String name, int successiveDerivativeNull, int derive) {
        this.name = name;
        this.successiveDerivativeNull = successiveDerivativeNull;
        this.derive = derive;
    }

    @Override
    public Expression<Variable> derive(Space R) {
        if (successiveDerivativeNull == 1) {
            return new Scalar(0);
        } else {
            return new Variable(name, this.successiveDerivativeNull - 1, derive + 1).setSign(this.hasMinus());
        }
    }

    @Override
    public Expression<Variable> derive(int recursionDepth, Space R) {
        if (recursionDepth == 0) {
            return this;
        } else {
            return this.derive(R);
        }
    }

    @Override
    public List<Expression<Variable>> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public Expression<Variable> calcul() {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        if (this.hasMinus()) {
            s.append("- ");
        }

        if (derive == 0) {
            s.append(name);
        } else {
            s.append("\\");

            for (int i = 0; i < derive; i++) {
                s.append("d");
            }

            s.append("ot{").append(name).append("}");
        }

        return s.toString();
    }

    @Override
    public Expression<Variable> clone() {
        return new Variable(this.name, this.successiveDerivativeNull, derive).setSign(this.hasMinus());
    }

    @Override
    public Expression<Variable> getPositiveClone() {
        return new Variable(this.name, this.successiveDerivativeNull, derive);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Variable) {
            return ((Variable) o).name.equals(this.name) && this.derive == ((Variable) o).derive && this.successiveDerivativeNull == ((Variable) o).successiveDerivativeNull
                    && this.hasMinus() == ((Variable) o).hasMinus();
        }

        return false;
    }

}
