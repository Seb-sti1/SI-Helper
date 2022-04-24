/*
 * Copyright (c) 2021. Sébastien Kerbourc'h. See LICENSE for more information.
 */

package fr.seb.td;

import fr.seb.function.Product;
import fr.seb.function.Scalar;
import fr.seb.function.ScalarProduct;
import fr.seb.si.Velocity;
import fr.seb.space.FixedSpace;
import fr.seb.space.Space;
import fr.seb.space.SpinningSpace;
import fr.seb.vectors.Point;
import fr.seb.vectors.Variable;

import static fr.seb.Main.pl;

public class MPSITD3 {

    public static void main(String[] args) {
        Space R0 = new FixedSpace();

        Variable theta1 = new Variable("\\theta_{1}", 2);
        Space R1 = new SpinningSpace(R0, 1, R0.getFixedPoint(), Space.VECTOR.Z, Space.VECTOR.Z, theta1, Space.VECTOR.X, Space.VECTOR.X);

        Variable L0 = new Variable("L_{0}", 1);
        Point O1 = new Point("O_{1}", R0.getFixedPoint(), new ScalarProduct(L0, R0.getUnitaryVector(Space.VECTOR.Z)));

        Variable theta2 = new Variable("\\theta_{2}", 2);
        Space R2 = new SpinningSpace(R1, 2, O1, Space.VECTOR.X, Space.VECTOR.X, theta2, Space.VECTOR.Y, Space.VECTOR.Y);

        Variable L1 = new Variable("L_{1}", 1);
        Point O2 = new Point("O_{2}", O1, new ScalarProduct(L1, R2.getUnitaryVector(Space.VECTOR.Y)));

        Variable theta3 = new Variable("\\theta_{3}", 2);
        Space R3 = new SpinningSpace(R2, 3, O2, Space.VECTOR.X, Space.VECTOR.X, theta3, Space.VECTOR.Y, Space.VECTOR.Y);

        Variable L2 = new Variable("L_{2}", 1);
        Point O3 = new Point("O_{3}", O2, new ScalarProduct(L2, R3.getUnitaryVector(Space.VECTOR.Y)));

        Variable L3 = new Variable("L_{3}", 2);
        Point O4 = new Point("O_{4}", O3, new ScalarProduct(Product.Create(L3, new Scalar(-1)), R3.getUnitaryVector(Space.VECTOR.Z)));

        Space R4 = new SpinningSpace(R3, 4, O4, Space.VECTOR.X, Space.VECTOR.X, new Scalar(0), Space.VECTOR.Y, Space.VECTOR.Y);

        pl(R1.toString());
        pl(R2.toString());
        pl(R3.toString());
        pl(R4.toString());

        pl(String.format("\\overrightarrow{%s%s} = %s", O1.getFather(), O1, O1.getFromFather().toString()), true);
        pl(String.format("\\overrightarrow{%s%s} = %s", O2.getFather(), O2, O2.getFromFather().toString()), true);
        pl(String.format("\\overrightarrow{%s%s} = %s", O3.getFather(), O3, O3.getFromFather().toString()), true);
        pl(String.format("\\overrightarrow{%s%s} = %s", O4.getFather(), O4, O4.getFromFather().toString()), true);

        Velocity v = new Velocity(O4, R4, R0);

        StringBuilder vstr1 = new StringBuilder(v + " = ");
        StringBuilder vstr2 = new StringBuilder(v + " = ");
        StringBuilder vstr3 = new StringBuilder(v + " = ");

        for (Velocity vdec : v.getDecomposition()) {
            vstr1.append(vdec).append("+");
            vstr2.append(vdec.calculate()).append("+");
            vstr3.append(vdec.calculate().calcul()).append("+");
        }

        pl(vstr1.substring(0, vstr1.length() - 1), true);
        pl(vstr2.substring(0, vstr2.length() - 1), true);
        pl(vstr3.substring(0, vstr3.length() - 1), true);
        pl(v + " = " + v.calculate().calcul(), true);
    }

}
