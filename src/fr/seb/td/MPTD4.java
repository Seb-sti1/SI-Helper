/*
 * Copyright (c) 2021. Sébastien Kerbourc'h. See LICENSE for more information.
 */

package fr.seb.td;

import fr.seb.Expression;
import fr.seb.function.Addition;
import fr.seb.function.ScalarProduct;
import fr.seb.latex.LatexFile;
import fr.seb.si.*;
import fr.seb.space.FixedSpace;
import fr.seb.space.Space;
import fr.seb.space.SpinningSpace;
import fr.seb.vectors.Point;
import fr.seb.vectors.Variable;
import fr.seb.vectors.Vector;

import java.util.ArrayList;
import java.util.List;

import static fr.seb.Main.pl;
import static fr.seb.space.Space.VECTOR.*;

public class MPTD4 {


    public static void main(String[] args) {
        LatexFile latex = new LatexFile("","");


        Variable alpha = new Variable("\\alpha", 3);
        Variable theta = new Variable("\\theta", 3);

        Variable H = new Variable("H", 1);
        Variable e = new Variable("e", 1);
        Variable a = new Variable("a", 1);
        Variable d = new Variable("d", 1);
        Variable h = new Variable("h", 1);
        Variable lambda = new Variable("\\lambda", 1);
        Variable M2 = new Variable("M_2", 1);



        Space R0 = new FixedSpace();
        Space R2 = new SpinningSpace(R0, 2, R0.getFixedPoint(), Z, Z, theta, X, X);
        Point B = new Point("B" , R0.getFixedPoint(), new ScalarProduct(e, R2.getUnitaryVector(X)));
        Space R1 = new SpinningSpace(R0, 1, B, Z, Z, alpha, X, X);

        List<Space> lSpace = new ArrayList<>();
        lSpace.add(R1);
        lSpace.add(R2);

        latex.addProjectionFigures(lSpace);


        Point C = new Point("C" , R0.getFixedPoint(), new ScalarProduct(H, R0.getUnitaryVector(X)));
        Point G1 = new Point("G_1", B, new ScalarProduct(d, R1.getUnitaryVector(X)));
        Point G2 = new Point("G_2", R0.getFixedPoint(),
                Addition.CreateVector(new ScalarProduct(a, R0.getUnitaryVector(Z)), new ScalarProduct(lambda, R2.getUnitaryVector(X))));
        Point G3 = new Point("G_3", C, new ScalarProduct(h, R1.getUnitaryVector(X)).invertSign());




        InertiaMatrix I1 = InertiaMatrix.CreateWithRotationalSymmetry(G1, R1, 1, null, X);
        InertiaMatrix I2 = InertiaMatrix.CreateWithReflectionalSymmetry(G2, R2, 2, null, X, Z);
        InertiaMatrix I3 = InertiaMatrix.CreateWithRotationalSymmetry(G3, R1, 3, null, X);

        List<InertiaMatrix> lInertia = new ArrayList<>();
        lInertia.add(I1);
        lInertia.add(I2);
        lInertia.add(I3);

        latex.addInertiaMatrix(lInertia);

        Velocity v = new Velocity(G2, R2, R0);

        Expression<Vector> velocityExpr = latex.addResult(v.toString(), v.calculate());
        latex.addVspace(1);

        Acceleration acc = new Acceleration(G2, R2, R0);
        Expression<Vector> accExpr = latex.addResult(acc.toString(), acc.calculateFromVelocity(velocityExpr));
        latex.addVspace(3);



        AngularMomentum aM1 = new AngularMomentum(R0.getFixedPoint(), R2, R0);
        Expression<Vector> result = aM1.calculateFromInertiaMatrix(I2, G2, M2);

        latex.addResult(aM1.toString(), result);
        latex.addVspace(3);



        AngularMomentum aM2 = new AngularMomentum(G2, R2, R0);
        Expression<Vector> aM2res = aM2.calculateFromInertiaMatrix(I2, G2, M2);

        aM2res = latex.addResult(aM2.toString(), aM2res);
        latex.addVspace(1);

        Torque t1 = new Torque(R0.getFixedPoint(), R2, R0);
        Expression<Vector> result2 = t1.calculate(aM2res, accExpr, G2, M2);
        latex.addResult(t1.toString(), result2);

        pl(latex.getDocument());
    }

}
