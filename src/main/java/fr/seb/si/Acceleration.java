/*
 * Copyright (c) 2023. Seb-sti1. See LICENSE.
 */

package fr.seb.si;

import fr.seb.Expression;
import fr.seb.function.Derivation;
import fr.seb.space.Space;
import fr.seb.vectors.Point;
import fr.seb.vectors.Vector;

public class Acceleration {

    final Point P;

    final Space R_fixed;
    final Space R_in;

    public Acceleration(Point P, Space R_in, Space R_fixed) {
        this.P = P;
        this.R_fixed = R_fixed;
        this.R_in = R_in;
    }

    public Expression<Vector> calculate() {
        Velocity v = new Velocity(P, R_in, R_fixed);
        return new Derivation<>(v.calculate(), R_fixed);
    }

    /**
     * Calculate the acceleration
     *
     * @param v the velocity of P in R_in considering R_fixed constant
     * @return the acceleration
     */
    public Expression<Vector> calculateFromVelocity(Expression<Vector> v) {
        return new Derivation<>(v, R_fixed);
    }

    @Override
    public String toString() {
        return String.format("\\overrightarrow{\\Gamma_{%s \\in %s/%s}}", P.toString(), R_in.getId(), R_fixed.getId());
    }
}
