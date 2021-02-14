package fr.seb.vectors;

import fr.seb.Expression;
import fr.seb.function.Scalar;
import fr.seb.space.Space;

import java.util.Arrays;
import java.util.List;

public class Variable implements Expression<Variable> {

    private final String name;
    private final boolean[] constant;
    private final int derive;

    public Variable(String name, boolean[] cnst) {
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
            return new Scalar<>(0);
        } else {
            return new Variable(name, Arrays.copyOfRange(this.constant, 1, this.constant.length), derive + 1);
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
    public List<Expression<Variable>> getChild() {
        return null;
    }

    @Override
    public Expression<Variable> calcul() {
        return this;
    }

    @Override
    public String toString() {
        if (derive == 0) {
            return name;
        } else {
            StringBuilder d = new StringBuilder();

            for (int i = 0; i < derive; i++) {
                d.append("d");
            }

            return "\\" + d + "ot{" + name + "}";
        }

    }
}
