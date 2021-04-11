package fr.seb.vectors;

import fr.seb.Expression;
import fr.seb.function.Scalar;
import fr.seb.space.Space;

import java.util.Arrays;
import java.util.List;

public class Variable extends Expression<Variable> {

    private final String name;
    private final boolean[] constant;
    private final int derive;

    public Variable(String name, boolean[] cnst) {
        // Todo: change cnst to int (max derive)
        this.name = name;

        if (cnst.length > 0) {
            this.constant = cnst;
        } else {
            this.constant = new boolean[]{true};
        }

        this.derive = 0;
    }

    public Variable(String name, boolean[] cnst, int derive) {
        this.name = name;

        if (cnst.length > 0) {
            this.constant = cnst;
        } else {
            this.constant = new boolean[]{true};
        }
        this.derive = derive;
    }

    @Override
    public Expression<Variable> derive(Space R) {
        if (constant[0]) {
            return new Scalar(0);
        } else {
            Variable v = new Variable(name, Arrays.copyOfRange(this.constant, 1, this.constant.length), derive + 1);

            if (this.hasMinus()) {
                v.invertSign();
            }

            return v;
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
        return null;
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
    public Expression<Variable> invertSign() {
        System.out.println("Warming inverted sign of variable");
        return super.invertSign();
    }

}
