/*
 * Copyright (c) 2021. Sébastien Kerbourc'h. See LICENSE for more information.
 */

package fr.seb.td;

import fr.seb.function.ScalarProduct;
import fr.seb.si.Acceleration;
import fr.seb.si.Velocity;
import fr.seb.space.FixedSpace;
import fr.seb.space.Space;
import fr.seb.space.SpinningSpace;
import fr.seb.vectors.Point;
import fr.seb.vectors.Variable;

import static fr.seb.Main.pl;

public class MPSITD2 {

    public static void main(String[] args) {
        Variable alpha = new Variable("\\alpha", 3);
        Variable beta = new Variable("\\beta", 3);

        Space R0 = new FixedSpace();
        Space R1 = new SpinningSpace(R0, 1, R0.getFixedPoint(), Space.VECTOR.X, Space.VECTOR.X, alpha, Space.VECTOR.Y, Space.VECTOR.Y);

        Variable a = new Variable("a", 1);
        Point A = new Point("A", R0.getFixedPoint(), new ScalarProduct(a, R1.getUnitaryVector(Space.VECTOR.Y)));

        Space R2 = new SpinningSpace(R1, 2, A, Space.VECTOR.Z, Space.VECTOR.Z, beta, Space.VECTOR.X, Space.VECTOR.X);

        Variable b = new Variable("b", 1);
        Point G = new Point("G", A, new ScalarProduct(b, R2.getUnitaryVector(Space.VECTOR.X)));

        Velocity v = new Velocity(G, R2, R0);
        pl(v.calculate().calcul().toString());

        pl("");

        Acceleration acceleration = new Acceleration(G, R2, R0);
        pl(acceleration.calculate(), true);
        pl(acceleration.calculate().calcul(), true);
        pl(acceleration.calculate().calcul().calcul(), true);
    }

}
