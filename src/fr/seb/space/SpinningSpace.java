package fr.seb.space;

import fr.seb.Expression;
import fr.seb.Utils;
import fr.seb.function.Addition;
import fr.seb.function.Cos;
import fr.seb.function.ScalarProduct;
import fr.seb.function.Sin;
import fr.seb.vectors.Point;
import fr.seb.vectors.Variable;
import fr.seb.vectors.Vector;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static fr.seb.space.Space.VECTOR.*;

public class SpinningSpace implements Space {

    final Space father;
    final Expression<Variable> angle;
    final Point fixedPoint;

    final Vector x;
    final Vector y;
    final Vector z;

    final VECTOR spinning;

    final int id;

    final private String s;

    /**
     * Create a new space rotation around of fixeR1 in father. the angle is between angleFather and angleR2.
     *
     * @param father the father space
     * @param id the id of the space
     * @param fixedPoint the point that is fixed in this space
     * @param fixedFather the rotation vector
     * @param spinning this vector will be the rotation vector in the new space
     * @param angle the angle of rotation
     * @param angleFather the vector in first space delimiting the angle
     * @param angleR2 the vector in second space delimiting the angle
     */
    public SpinningSpace(Space father, int id, Point fixedPoint, VECTOR fixedFather, VECTOR spinning, Expression<Variable> angle, VECTOR angleFather, VECTOR angleR2) {
        this.father = father;
        this.angle = angle;
        this.id = id;
        this.fixedPoint = fixedPoint;

        x = new Vector("x", id, this, VECTOR.X);
        y = new Vector("y", id, this, VECTOR.Y);
        z = new Vector("z", id, this, VECTOR.Z);

        Vector firstSpinningFather = father.getUnitaryVector(angleFather);

        // determine the last vector
        Set<VECTOR> used = EnumSet.of(X, Y, Z);
        used.remove(fixedFather); // the fixed vector in father
        used.remove(angleFather); // the second vector in father
        Vector secondSpinningFather = father.getUnitaryVector((VECTOR) used.toArray()[0]); // the last vector of father

        this.spinning = spinning;
        switch (spinning) {
            case X:
                x.setExpression(father.getUnitaryVector(fixedFather));
                break;
            case Y:
                y.setExpression(father.getUnitaryVector(fixedFather));
                break;
            case Z:
                z.setExpression(father.getUnitaryVector(fixedFather));
                break;
        }

        Expression<Vector> firstSpinningR2 = Addition.CreateVector(new ScalarProduct(new Cos(angle), firstSpinningFather),
                new ScalarProduct(new Sin(angle), secondSpinningFather));

        switch (angleR2) {
            case X:
                x.setExpression(firstSpinningR2);
                break;
            case Y:
                y.setExpression(firstSpinningR2);
                break;
            case Z:
                z.setExpression(firstSpinningR2);
                break;
        }

        Expression<Vector> secondSpinningR2 = Addition.CreateVector(new ScalarProduct(new Sin(angle).invertSign(), firstSpinningFather),
                new ScalarProduct(new Cos(angle), secondSpinningFather));

        VECTOR secondAngle;
        if (x.getExpression() == null) {
            x.setExpression(secondSpinningR2);
            secondAngle = X;
        } else if (y.getExpression() == null) {
            y.setExpression(secondSpinningR2);
            secondAngle = Y;
        } else {
            z.setExpression(secondSpinningR2);
            secondAngle = Z;
        }

        s = String.format(Utils.projectionFigure,
                firstSpinningFather.toString(), secondSpinningFather.toString(), father.getUnitaryVector(fixedFather),
                getUnitaryVector(spinning).toString(), getUnitaryVector(angleR2).toString(), getUnitaryVector(secondAngle).toString(),
                angle.toString(), angle);

    }

    @Override
    public List<Space> getFathers() {
        Space R = this;

        List<Space> Fathers = new ArrayList<>();

        while (R.getFather() != null) {
            Fathers.add(R.getFather());
            R = R.getFather();
        }

        return Fathers;
    }

    @Override
    public Vector getUnitaryVector(VECTOR VECTOR) {
        switch (VECTOR) {
            case X:
                return x;
            case Y:
                return y;
            case Z:
                return z;
        }
        return null;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Point getFixedPoint() {
        return this.fixedPoint;
    }

    @Override
    public Space getFather() {
        return this.father;
    }

    @Override
    public Expression<Vector> getRotationVector() {
        return new ScalarProduct(angle.derive(null), this.getUnitaryVector(this.spinning));
    }

    @Override
    public String toString() {
        return s;
    }

}
