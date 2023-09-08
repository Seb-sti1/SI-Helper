/*
 * Copyright (c) 2023. Seb-sti1. See LICENSE.
 */

package fr.seb;

import fr.seb.function.Addition;
import fr.seb.function.Scalar;
import fr.seb.function.ScalarProduct;
import fr.seb.si.Acceleration;
import fr.seb.space.FixedSpace;
import fr.seb.space.Space;
import fr.seb.space.Space.VECTOR;
import fr.seb.space.SpinningSpace;
import fr.seb.vectors.Point;
import fr.seb.vectors.Variable;

public class Main {

    public static void main(String[] args) {



        Variable alpha = new Variable("\\alpha", 4);
        Variable beta = new Variable("\\beta", 4);

        Variable H = new Variable("H", 1);
        Variable e = new Variable("e", 1);
        Variable L = new Variable("L", 1);
        Variable X = new Variable("X", 5);
        Variable Z = new Variable("Z", 5);

        Space R0 = new FixedSpace();
        Space R1 = new SpinningSpace(R0, 1, R0.getFixedPoint(), VECTOR.Z, VECTOR.Z, alpha, VECTOR.X, VECTOR.X);

        Expression<Variable> a = Addition.CreateVariable(L, e);

        Point A = new Point("A", R0.getFixedPoint(), Addition.CreateVector(new ScalarProduct(H, R0.getUnitaryVector(VECTOR.Z)),
                                                                        new ScalarProduct(e, R1.getUnitaryVector(VECTOR.Y))));
        Point B = new Point("B", A, new ScalarProduct(L, R1.getUnitaryVector(VECTOR.X)));
        Point C = new Point("C", B, new ScalarProduct(X, R1.getUnitaryVector(VECTOR.X)));

        Space R2 = new SpinningSpace(R1, 2, A, VECTOR.X, VECTOR.X, beta, VECTOR.Y, VECTOR.Y);

        Space R3 = new SpinningSpace(R2, 3, B, VECTOR.X, VECTOR.X, new Scalar(0), VECTOR.Y, VECTOR.Y);
        Space R4 = new SpinningSpace(R3, 4, C, VECTOR.X, VECTOR.X, new Scalar(0), VECTOR.Y, VECTOR.Y);


        Point G4 = new Point("G4", C, new ScalarProduct(Z, R2.getUnitaryVector(VECTOR.Z)));

        Acceleration TRDQ2 = new Acceleration(G4, R4, R0);

        pl(TRDQ2.calculate());

        //pl(TRDQ2.calculateFromInertiaMatrix().calcul().calcul().calcul().calcul().calcul());
        //pl("");
        //pl(Utils.dotProduct(TRDQ2.calculateFromInertiaMatrix().calcul().calcul().calcul().calcul().calcul(), R2.getUnitaryVector(VECTOR.Z)));


    }

    public static void p(Object o) {
        System.out.print(o.toString());
    }

    public static void pl(Object o) {
        System.out.println(o.toString());
    }

    public static void pl(Object o, boolean dollars) {
        System.out.printf("$%s$\n\n", o.toString());
    }
}
