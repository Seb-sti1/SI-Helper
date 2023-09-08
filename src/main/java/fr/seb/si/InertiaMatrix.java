/*
 * Copyright (c) 2021-2023. Seb-sti1. See LICENSE.
 */

package fr.seb.si;

import fr.seb.Expression;
import fr.seb.Utils;
import fr.seb.function.Addition;
import fr.seb.function.Power;
import fr.seb.function.Product;
import fr.seb.function.Scalar;
import fr.seb.space.Space;
import fr.seb.space.Space.VECTOR;
import fr.seb.vectors.Matrix;
import fr.seb.vectors.Point;
import fr.seb.vectors.Variable;
import fr.seb.vectors.Vector;

import java.util.EnumSet;
import java.util.Set;

import static fr.seb.space.Space.VECTOR.*;


@SuppressWarnings("unused")
public class InertiaMatrix extends Matrix {


    Point P;
    Space R;
    Variable m;


    public InertiaMatrix(Point P, Space R, Variable m) {
        super(3, 3);
        this.setLatexMatrixEnv(LATEX_MATRIX_ENV.bmatrix);

        this.P = P;
        this.R = R;
        this.m = m;
    }


    /**
     * Create a inertia matrix with
     *
     * @param G the center of gravity
     * @param R the space attached to the object
     * @param m the mass of the object
     * @return the associate inertia matrix
     */
    public static InertiaMatrix CreateDefault(Point G, Space R, int id, Variable m) {
        InertiaMatrix im = new InertiaMatrix(G, R, m);

        Variable A = new Variable(String.format("A_{%s}", id), 1);
        Variable B = new Variable(String.format("B_{%s}", id), 1);
        Variable C = new Variable(String.format("C_{%s}", id), 1);
        Expression<Variable> negD = new Variable(String.format("D_{%s}", id), 1).invertSign();
        Expression<Variable> negE = new Variable(String.format("E_{%s}", id), 1).invertSign();
        Expression<Variable> negF = new Variable(String.format("F_{%s}", id), 1).invertSign();

        im.set(0, 0, A);
        im.set(0, 1, negF);
        im.set(0, 2, negE);

        im.set(1, 0, negF);
        im.set(1, 1, B);
        im.set(1, 2, negD);

        im.set(2, 0, negE);
        im.set(2, 1, negD);
        im.set(2, 2, C);

        return im;
    }

    /**
     * Create a inertia matrix for an object that have a reflectional symmetry
     *
     * @param G   the center of gravity
     * @param R   the space attached to the object
     * @param m   the mass of the object
     * @param Vp1 the first axis (in R) for the plane of symmetry
     * @param Vp2 the second axis (in R) for the plane of symmetry
     * @return the associate inertia matrix
     */
    public static InertiaMatrix CreateWithReflectionalSymmetry(Point G, Space R, int id, Variable m, VECTOR Vp1, VECTOR Vp2) {
        InertiaMatrix im = CreateDefault(G, R, id, m);

        // determine the last vector
        Set<VECTOR> used = EnumSet.of(X, Y, Z);
        used.remove(Vp1);
        used.remove(Vp2);
        VECTOR last = (VECTOR) used.toArray()[0];

        im.set(last.ordinal(), Vp1.ordinal(), new Scalar(0));
        im.set(Vp1.ordinal(), last.ordinal(), new Scalar(0));

        im.set(last.ordinal(), Vp2.ordinal(), new Scalar(0));
        im.set(Vp2.ordinal(), last.ordinal(), new Scalar(0));

        return im;
    }

    /**
     * Create a inertia matrix for an object that have two reflectional symmetry
     *
     * @param G the center of gravity
     * @param R the space attached to the object
     * @param m the mass of the object
     * @return the associate inertia matrix
     */
    public static InertiaMatrix CreateWithTwoReflectionalSymmetry(Point G, Space R, int id, Variable m) {
        InertiaMatrix im = new InertiaMatrix(G, R, m);

        Variable A = new Variable(String.format("A_{%s}", id), 1);
        Variable B = new Variable(String.format("B_{%s}", id), 1);
        Variable C = new Variable(String.format("C_{%s}", id), 1);

        im.set(0, 0, A);
        im.set(1, 1, B);
        im.set(2, 2, C);

        return im;
    }

    /**
     * Create a inertia matrix for an object that have a rotational symmetry
     *
     * @param G the center of gravity
     * @param R the space attached to the object
     * @param m the mass of the object
     * @return the associate inertia matrix
     */
    public static InertiaMatrix CreateWithRotationalSymmetry(Point G, Space R, int id, Variable m, VECTOR axis) {
        InertiaMatrix im = new InertiaMatrix(G, R, m);

        Variable A = new Variable(String.format("A_{%s}", id), 1);
        Variable B = new Variable(String.format("B_{%s}", id), 1);
        Variable C = new Variable(String.format("C_{%s}", id), 1);

        switch (axis) {
            case X:
                im.set(0, 0, A);
                im.set(1, 1, B);
                im.set(2, 2, B);
                break;
            case Y:
                im.set(0, 0, A);
                im.set(1, 1, B);
                im.set(2, 2, A);
            case Z:
                im.set(0, 0, A);
                im.set(1, 1, A);
                im.set(2, 2, C);
                break;
        }
        return im;
    }

    /**
     * Create a inertia matrix for an object that have a spherical symmetry
     *
     * @param G the center of gravity
     * @param R the space attached to the object
     * @param m the mass of the object
     * @return the associate inertia matrix
     */
    public static InertiaMatrix CreateWithSphericalSymmetry(Point G, Space R, int id, Variable m) {
        InertiaMatrix im = new InertiaMatrix(G, R, m);

        Variable A = new Variable(String.format("A_{%s}", id), 1);

        im.set(0, 0, A);
        im.set(1, 1, A);
        im.set(2, 2, A);

        return im;
    }

    public void changePoint(Point newP) {
        Expression<Vector> x =  Utils.getVector(this.P, newP);

        Expression<Variable> a = Utils.dotProduct(x, this.R.getUnitaryVector(VECTOR.X));
        Expression<Variable> b = Utils.dotProduct(x, this.R.getUnitaryVector(VECTOR.Y));
        Expression<Variable> c = Utils.dotProduct(x, this.R.getUnitaryVector(VECTOR.Z));

        this.P = newP;

        add(0,0, Product.Create(m, Addition.CreateVariable(new Power(c, 2), new Power(b ,2))));
        add(0,1, Product.Create(m, Product.Create(a, b)).invertSign());
        add(0,2, Product.Create(m, Product.Create(a, c)).invertSign());

        add(1,0, Product.Create(m, Product.Create(b, a)).invertSign());
        add(1,1, Product.Create(m, Addition.CreateVariable(new Power(a, 2), new Power(c,2))));
        add(1,2, Product.Create(m, Product.Create(b, c)).invertSign());

        add(0,1, Product.Create(m, Product.Create(c, a)).invertSign());
        add(0,2, Product.Create(m, Product.Create(c, b)).invertSign());
        add(0,0, Product.Create(m, Addition.CreateVariable(new Power(a, 2), new Power(b ,2))));
    }

}
