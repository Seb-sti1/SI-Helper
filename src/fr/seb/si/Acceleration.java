package fr.seb.si;

import fr.seb.Expression;
import fr.seb.function.Derivation;
import fr.seb.space.Space;
import fr.seb.vectors.Point;
import fr.seb.vectors.Vector;

public class Acceleration {

    final Velocity v;

    final Point P;

    final Space R_fixed;

    public Acceleration(Point P, Space R_in, Space R_fixed) {
        v = new Velocity(P, R_in);
        this.P = P;
        this.R_fixed = R_fixed;
    }

    public Expression<Vector> calculate() {
        return new Derivation<>(v.calculate(R_fixed), R_fixed);
    }
}
