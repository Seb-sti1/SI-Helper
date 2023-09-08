package fr.seb.si;

import fr.seb.Expression;
import fr.seb.Utils;
import fr.seb.function.Addition;
import fr.seb.function.ScalarProduct;
import fr.seb.function.WedgeProduct;
import fr.seb.space.Space;
import fr.seb.space.Space.VECTOR;
import fr.seb.vectors.Matrix;
import fr.seb.vectors.Point;
import fr.seb.vectors.Variable;
import fr.seb.vectors.Vector;

import javax.naming.OperationNotSupportedException;
import java.util.Arrays;

public class AngularMomentum {

    Point P;
    Space R_in;
    Space R_fixed;

    public AngularMomentum(Point P, Space R_in, Space R_fixed) {
        this.P = P;
        this.R_in = R_in;
        this.R_fixed = R_fixed;
    }


    /**
     * Calculates from an InertiaMatrix
     *
     * @param I need to be created with param Point G (next point), Space R_in
     * @param G the center of gravity
     * @param M the mass
     * @return the angular momentum of solid associated with R_in in R_fixed at P
     */
    public Expression<Vector> calculateFromInertiaMatrix(InertiaMatrix I, Point G, Expression<Variable> M) {
        Expression<Vector> omega = Utils.getProjectionVector(R_in, R_fixed);

        Expression<Variable> omegaX = Utils.dotProduct(omega, R_in.getUnitaryVector(VECTOR.X));
        Expression<Variable> omegaY = Utils.dotProduct(omega, R_in.getUnitaryVector(VECTOR.Y));
        Expression<Variable> omegaZ = Utils.dotProduct(omega, R_in.getUnitaryVector(VECTOR.Z));

        Matrix m = new Matrix(3, 1);
        m.set(0, 0, omegaX);
        m.set(1, 0, omegaY);
        m.set(2, 0, omegaZ);

        Matrix result;
        try {
            result = I.product(m);


            Expression<Vector> angularMomentumX = new ScalarProduct(result.get(0, 0), R_in.getUnitaryVector(VECTOR.X));
            Expression<Vector> angularMomentumY = new ScalarProduct(result.get(1, 0), R_in.getUnitaryVector(VECTOR.Y));
            Expression<Vector> angularMomentumZ = new ScalarProduct(result.get(2, 0), R_in.getUnitaryVector(VECTOR.Z));

            Expression<Vector> angularMomentum = Addition.CreateVector(Arrays.asList(angularMomentumX, angularMomentumY, angularMomentumZ));

            if (!G.equals(P)) {
                angularMomentum = Addition.CreateVector(angularMomentum,
                        new WedgeProduct(Utils.getVector(P, G),
                                new ScalarProduct(M, new Velocity(G, R_in, R_fixed).calculate())));

            }

            return angularMomentum;
        } catch (OperationNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Todo : calculateFromInertiaMatrix from angular momentum on a specific axis


    public String toString() {
        return String.format("\\overrightarrow{\\sigma_{%s \\in %s/%s}}", P.toString(), R_in.getId(), R_fixed.getId());
    }
}
