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

public class Utils {


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
            Expression<Vector> rotationVector = null;

            Space R_dedans = R2;

            while (R_dedans != R1) {
                if (R_dedans.getRotationVector() != null) {
                    if (rotationVector == null) {
                        rotationVector = R_dedans.getRotationVector();
                    } else {
                        rotationVector = new Addition<>(rotationVector, R_dedans.getRotationVector());
                    }
                }
                R_dedans = R_dedans.getFather();
            }

            return rotationVector;
        } else { // the opposite
            return new ScalarProduct(new Scalar<>( -1), getProjectionVector(R1, R2));
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
            Vector x = R2.getUnitaryVector(Space.VECTOR.X);
            Vector y = R2.getUnitaryVector(Space.VECTOR.Y);
            Vector z = R2.getUnitaryVector(Space.VECTOR.Z);

            Expression<Variable> v1dotx = dotProduct(v1, x);
            Expression<Variable> v1doty = dotProduct(v1, y);
            Expression<Variable> v1dotz = dotProduct(v1, z);

            Expression<Variable> v2dotx = dotProduct(v2, x);
            Expression<Variable> v2doty = dotProduct(v2, y);
            Expression<Variable> v2dotz = dotProduct(v2, z);

            Expression<Variable> resultx = new Addition<>(new Product(v1doty, v2dotz), new Product(new Scalar<>(-1), new Product(v1dotz, v2doty))).calcul();
            Expression<Variable> resulty = new Addition<>(new Product(v1dotz, v2dotx), new Product(new Scalar<>(-1), new Product(v1dotx, v2dotz))).calcul();
            Expression<Variable> resultz = new Addition<>(new Product(v1dotx, v2doty), new Product(new Scalar<>(-1), new Product(v1doty, v2dotx))).calcul();

            return new Addition<>(new ScalarProduct(resultx, x),
                                    new Addition<>(new ScalarProduct(resulty, y),
                                                    new ScalarProduct(resultz, z))).calcul();

        } else {
            return new ScalarProduct(new Scalar<>(-1), gammaRule(v2, v1));
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
        if (vectorLeft instanceof Addition) {
            Addition<Vector> a = (Addition<Vector>) vectorLeft;

            return new Addition<>(dotProduct(a.left, vectorRight), dotProduct(a.right, vectorRight)).calcul();
        } else if (vectorLeft instanceof ScalarProduct) {
            ScalarProduct pe = (ScalarProduct) vectorLeft;

            return new Product(pe.scalaire, dotProduct(pe.vecteur, vectorRight)).calcul();
        }

        if (vectorRight instanceof Addition) {
            Addition<Vector> a = (Addition<Vector>) vectorRight;

            return new Addition<>(dotProduct(vectorLeft, a.left), dotProduct(vectorLeft, a.right)).calcul();
        } else if (vectorRight instanceof ScalarProduct) {
            ScalarProduct pe = (ScalarProduct) vectorRight;

            return new Product(pe.scalaire, dotProduct(pe.vecteur, vectorLeft)).calcul();
        }

        if (vectorRight instanceof Vector && vectorLeft instanceof Vector) {
            Vector vL = (Vector) vectorLeft;
            Vector vR = (Vector) vectorRight;

            if (vL.getSpace() == vR.getSpace()) {
                if (vL == vR) {
                    return new Scalar<>(1);
                } else {
                    return new Scalar<>(0);
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
            Expression<Vector> result = new VectorNull();

            Point C = A;

            while (C != B) {
                result = new Addition<>(result, C.getFromFather());

                C = C.getFather();
            }

            return new ScalarProduct(new Scalar<>(-1), result.calcul()); // because the calculation is done oppositely (because of getFromFather)
        } else if (B.getFathers().contains(A)) { // the other way around
            return new ScalarProduct(new Scalar<>(-1), getVector(B, A)).calcul();
        }

        throw new Error("Can't find a way to calculate this vector !");
    }

}
