package fr.seb.si;

import fr.seb.space.Space;
import fr.seb.vectors.Point;

public class Torque {

    final Point P;

    final Space R_fixed;


    public Torque(Point P, Space R_in, Space R_fixed) {
        this.P = P;
        this.R_fixed = R_fixed;
    }

    // Todo : Calculation from AngularMomentum derivation

    // Todo : Shifting of point

}
