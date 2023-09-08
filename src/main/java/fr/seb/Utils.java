/*
 * Copyright (c) 2023. Seb-sti1. See LICENSE.
 */

package fr.seb;

import fr.seb.function.Addition;
import fr.seb.function.Product;
import fr.seb.function.Scalar;
import fr.seb.function.ScalarProduct;
import fr.seb.space.Space;
import fr.seb.vectors.Point;
import fr.seb.vectors.Variable;
import fr.seb.vectors.Vector;
import fr.seb.vectors.VectorNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utils {



    static final public String projectionFigure = "\\begin{tikzpicture}\n" +
            //"        % vectors\n" +
            "        \\node (x) at (3.2,0) {$%s$};\n" +
            "        \\node (y) at (0,3.2) {$%s$};\n" +
            "        \\node (z) at (-0.2,-0.5) {$%s = %s$};\n" +
            "\n" +
            "        \\node (xone) at (3.2,1.1) {$%s$};\n" +
            "        \\node (yone) at (-1.1,3.2) {$%s$};\n" +
            "\n" +
            "\n" +
            //"        % angle\n" +
            "        \\draw[thick, ->] (3,0.1) arc (0:15:3);\n" +
            "        \\node (alphab) at (3.2,0.5) {$%s$};\n" +
            "        \\draw[thick, ->, rotate=90] (3,0.1) arc (0:16:3);\n" +
            "        \\node (alphat) at (-0.5,3.2) {$%s$};\n" +
            "\n" +
            //"        % draw the arrow\n" +
            "        \\draw[->,line width=0.03cm] (0,0) to (3,0);\n" +
            "        \\draw[->,line width=0.03cm] (0,0) to (0,3);\n" +
            "\n" +
            "        \\draw[->,line width=0.03cm] (0,0) to (2.85,0.95);\n" +
            "        \\draw[->,line width=0.03cm] (0,0) to (-0.95,2.85);\n" +
            "\n" +
            //"        % draw a white circle to hide beginnings of arrow\n" +
            "        \\node[circle,fill,color=white] (a) at (0,0) {};\n" +
            "\n" +
            //"        % draw a black circle and dot at the origin\n" +
            "        \\node[circle,draw,line width=1pt] (a) at (0,0) {};\n" +
            "        \\node[circle,fill,inner sep=1pt] (b) at (0, 0) {};\n" +
            "    \\end{tikzpicture}";

    /**
     * Calculate the rotational vector
     *
     * @param R2 the first space
     * @param R1 the second space
     * @return \vec{\omega_{R2/R1}}
     */
    public static Expression<Vector> getProjectionVector(Space R2, Space R1) {
        if (R2 == R1) {
            return null;
        } else if (R2.getFathers() != null && R2.getFathers().contains(R1)) { // the vector is in the space declared before (need to go down)
            List<Expression<Vector>> list = new ArrayList<>();

            Space R_in = R2;

            while (R_in != R1) {
                if (R_in.getRotationVector() != null) {
                    list.add(R_in.getRotationVector());
                }
                R_in = R_in.getFather();
            }

            return Addition.CreateVector(list);
        } else { // the opposite
            return new ScalarProduct(new Scalar( -1), getProjectionVector(R1, R2));
        }
    }


    /**
     * Calculate the wedge product of the two vector with the gamma rule
     *
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the wedge product
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public static Expression<Vector> gammaRule(Vector v1, Vector v2) {
        Space R1 = v1.getSpace();
        Space R2 = v2.getSpace();

        if (R1 == R2 || (R1.getFather() != null && R1.getFathers().contains(R2))) {

            // search for other alias of v2
            if (R1 != R2) {
                Vector alias = R1.getUnitaryVector(v2.getType());
                Vector subAlias = alias;

                while (subAlias.getExpression() instanceof Vector && !subAlias.equals(v2)) {
                    subAlias = (Vector) subAlias.getExpression();
                }

                if (subAlias.equals(v2)) {// there is an easier way to calculateFromInertiaMatrix the wedge product
                    return gammaRule(v1, alias);
                }
            }

            // make the wedge product with the alias or v2
            Vector x = R2.getUnitaryVector(Space.VECTOR.X);
            Vector y = R2.getUnitaryVector(Space.VECTOR.Y);
            Vector z = R2.getUnitaryVector(Space.VECTOR.Z);

            Expression<Variable> v1dotx = dotProduct(v1, x);
            Expression<Variable> v1doty = dotProduct(v1, y);
            Expression<Variable> v1dotz = dotProduct(v1, z);

            Expression<Variable> v2dotx = dotProduct(v2, x);
            Expression<Variable> v2doty = dotProduct(v2, y);
            Expression<Variable> v2dotz = dotProduct(v2, z);

            Expression<Variable> resultx = Addition.CreateVariable(Product.Create(v1doty, v2dotz), Product.Create(v1dotz, v2doty).clone().invertSign()).calcul();
            Expression<Variable> resulty = Addition.CreateVariable(Product.Create(v1dotz, v2dotx), Product.Create(v1dotx, v2dotz).clone().invertSign()).calcul();
            Expression<Variable> resultz = Addition.CreateVariable(Product.Create(v1dotx, v2doty), Product.Create(v1doty, v2dotx).clone().invertSign()).calcul();

            return Addition.CreateVector(Arrays.asList(new ScalarProduct(resultx, x), new ScalarProduct(resulty, y), new ScalarProduct(resultz, z))).calcul();
        } else {
            return gammaRule(v2, v1).invertSign();
        }
    }


    /**
     * Makes the dot product of the two vectors
     *
     * @param vectorLeft the first vector
     * @param vectorRight the second vector
     * @return the dot product
     */
    public static Expression<Variable> dotProduct(Expression<Vector> vectorLeft, Expression<Vector> vectorRight) {

        // if one of the two vector is an addition -> decompose and restart the dot product with the decomposition
        if (vectorLeft instanceof Addition || vectorRight instanceof Addition) {
            Addition<Vector> add;
            Expression<Vector> other;

            if (vectorLeft instanceof Addition) {
                add = (Addition<Vector>) vectorLeft;
                other = vectorRight;
            } else {
                add = (Addition<Vector>) vectorRight;
                other = vectorLeft;
            }

            List<Expression<Variable>> list = new ArrayList<>();

            for (Expression<Vector> child : add.getChildren()) {
                list.add(dotProduct(child, other));
            }

            return Addition.CreateVariable(list).needToInvertSign(add.hasMinus()).calcul();
        }
        // if one of the two vector is a scalar product -> put the product before and restart the dot product with the new vector
        else if (vectorLeft instanceof ScalarProduct || vectorRight instanceof ScalarProduct) {
            ScalarProduct sp;
            Expression<Vector> other;

            if (vectorLeft instanceof ScalarProduct) {
                sp = (ScalarProduct) vectorLeft;
                other = vectorRight;
            } else {
                sp = (ScalarProduct) vectorRight;
                other = vectorLeft;
            }

            return Product.Create(sp.scalar, dotProduct(sp.vector, other)).needToInvertSign(sp.hasMinus()).calcul();
        }

        if (vectorRight instanceof Vector && vectorLeft instanceof Vector) {
            Vector vL = (Vector) vectorLeft;
            Vector vR = (Vector) vectorRight;

            if (vL.getSpace() == vR.getSpace()) {
                if (vL.equals(vR)) {
                    return new Scalar(1);
                } else {
                    return new Scalar(0);
                }
            } else {
                if (vR.getSpace().getFather() != null && vR.getSpace().getFathers().contains(vL.getSpace())) {
                    return dotProduct(vL, vR.getExpression());
                } else {
                    return dotProduct(vL.getExpression(), vR);
                }
            }
        }

        throw new Error("Can't find a way to do this dot product !");
    }

    /**
     * Calculate \vec{AB}
     * @param A the first point
     * @param B the second point
     * @return \vec{AB}
     */
    public static Expression<Vector> getVector(Point A, Point B) {
        if (A == B) {
            return new VectorNull();
        }

        if (A.getFathers().contains(B)) {
            List<Expression<Vector>> list = new ArrayList<>();
            Point C = A;

            while (C != B) {
                list.add(C.getFromFather());

                C = C.getFather();
            }

            if (list.isEmpty()) {
                return new VectorNull();
            }

            return Addition.CreateVector(list).invertSign(); // because the calculation is done oppositely (because of getFromFather)
        } else if (B.getFathers().contains(A)) { // the other way around
            return getVector(B, A).invertSign();
        } else if (!Collections.disjoint(A.getFathers(), B.getFathers())) {
            List<Point> intersect = A.getFathers();
            intersect.retainAll(B.getFathers());

            Point O = intersect.get(0);
            return Addition.CreateVector(Utils.getVector(A, O), Utils.getVector(O, B));
        }

        throw new Error("Can't find a way to calculateFromInertiaMatrix this vector !");
    }



    public static <T> boolean listEqualsIgnoreOrders(List<T> l1, List<T> l2) {
        return l1.size() == l2.size() && l1.containsAll(l2) && l2.containsAll(l1);
    }

}
