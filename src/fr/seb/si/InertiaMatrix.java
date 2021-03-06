/*
 * Copyright (c) 2021. Sébastien Kerbourc'h. See LICENSE for more information.
 */

package fr.seb.si;

import fr.seb.Expression;
import fr.seb.Utils;
import fr.seb.function.Addition;
import fr.seb.function.Power;
import fr.seb.function.Product;
import fr.seb.space.Space;
import fr.seb.vectors.Matrix;
import fr.seb.vectors.Point;
import fr.seb.vectors.Variable;
import fr.seb.vectors.Vector;

public class InertiaMatrix extends Matrix {

    Point P;
    Space R;
    Variable m;

    public InertiaMatrix(Point P, Space R, Variable m) {
        super(3, 3);

        this.P = P;
        this.R = R;
        this.m = m;
    }



    public void changePoint(Point newP) {
        Expression<Vector> x =  Utils.getVector(this.P, newP);

        Expression<Variable> a = Utils.dotProduct(x, this.R.getUnitaryVector(Space.VECTOR.X));
        Expression<Variable> b = Utils.dotProduct(x, this.R.getUnitaryVector(Space.VECTOR.Y));
        Expression<Variable> c = Utils.dotProduct(x, this.R.getUnitaryVector(Space.VECTOR.Z));

        this.P = newP;

        add(0,0, Product.Create(m, Addition.CreateVariable(new Power(a, 2), new Power(b ,2))));
        add(0,1, Product.Create(m, Addition.CreateVariable(new Power(a, 2), new Power(b ,2))));
        // Todo : finir les formules


    }

}
