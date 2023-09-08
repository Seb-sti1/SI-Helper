/*
 * Copyright (c) 2023. Seb-sti1. See LICENSE.
 */

package fr.seb.si;

import fr.seb.Expression;
import fr.seb.Utils;
import fr.seb.function.Addition;
import fr.seb.function.Derivation;
import fr.seb.function.ScalarProduct;
import fr.seb.function.WedgeProduct;
import fr.seb.space.Space;
import fr.seb.vectors.Point;
import fr.seb.vectors.Variable;
import fr.seb.vectors.Vector;

public class Torque {

    final Point P;

    final Space R_in;
    final Space R_fixed;


    public Torque(Point P, Space R_in, Space R_fixed) {
        this.P = P;
        this.R_in = R_in;
        this.R_fixed = R_fixed;
    }

    /**
     * Calculates from an InertiaMatrix
     *
     * @param I need to be created with param Point G (next point), Space R_in
     * @param G the center of gravity
     * @param M the mass
     * @return the torque of solid associated with R_in in R_fixed at P
     */
    public Expression<Vector> calculate(InertiaMatrix I, Point G, Expression<Variable> M) {
        AngularMomentum aM = new AngularMomentum(G, R_in, R_fixed);
        return calculate(aM.calculateFromInertiaMatrix(I, G, M), P, M);
    }

    /**
     * Calculates from an Angular Momentum
     *
     * @param angularMomentum the result of angular momentum of solid associated with R_in in R_fixed at G
     * @param G               the center of gravity
     * @param M               the mass
     * @return the torque of solid associated with R_in in R_fixed at P
     */
    public Expression<Vector> calculate(Expression<Vector> angularMomentum, Point G, Expression<Variable> M) {
        return calculate(angularMomentum, new Acceleration(G, R_in, R_fixed).calculate(), G, M);
    }

    /**
     * Calculates from an Angular Momentum and acceleration
     *
     * @param angularMomentum the result of angular momentum of solid associated with R_in in R_fixed at G
     * @param acceleration    the acceleration of G in R_in considering R_fixed fixed
     * @param G               the center of gravity
     * @param M               the mass
     * @return the torque of solid associated with R_in in R_fixed at P
     */
    public Expression<Vector> calculate(Expression<Vector> angularMomentum, Expression<Vector> acceleration, Point G, Expression<Variable> M) {
        Expression<Vector> torque = new Derivation<>(angularMomentum, R_fixed);

        if (!G.equals(P)) {
            torque = Addition.CreateVector(torque,
                    new WedgeProduct(Utils.getVector(P, G),
                            new ScalarProduct(M, acceleration)));
        }

        return torque;
    }


    public String toString() {
        return String.format("\\overrightarrow{\\delta_{%s \\in %s/%s}}", P.toString(), R_in.getId(), R_fixed.getId());
    }

}
