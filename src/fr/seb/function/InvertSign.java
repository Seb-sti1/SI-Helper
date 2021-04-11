/*
 * Copyright (c) 2021. Sébastien Kerbourc'h. See LICENSE for more information.
 */

package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;

import java.util.Collections;
import java.util.List;

public class InvertSign<T> extends Expression<T> {

    Expression<T> child;

    boolean hasMinus = true;

    public InvertSign(Expression<T> child) {
        Expression<T> c = child;

        while (c instanceof InvertSign) {
            hasMinus = !hasMinus;
            c = c.getChildren().get(0);
        }

        this.child = c;
    }

    @Override
    public Expression<T> calcul() {
        if (this.hasMinus) {
            return new InvertSign<>(this.child.calcul());
        } else {
            return this.child.calcul();
        }
    }

    @Override
    public Expression<T> derive(Space R) {
        if (this.hasMinus) {
            return new InvertSign<>(this.child.derive(R));
        } else {
            return this.child.calcul();
        }
    }

    @Override
    public Expression<T> derive(int recursionDepth, Space R) {
        if (this.hasMinus) {
            return new InvertSign<>(this.child.derive(recursionDepth, R));
        } else {
            return this.child.calcul();
        }
    }

    @Override
    public List<Expression<T>> getChildren() {
        return Collections.singletonList(child);
    }

    @Override
    public String toString() {
        if (this.hasMinus) {
            return "- " + this.child.toString();
        } else {
            return this.child.toString();
        }
    }
}
