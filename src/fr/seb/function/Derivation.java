package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;

import java.util.List;


public class Derivation<T> extends Expression<T> {

    final Expression<T> toDerive;
    final Space R;
    public Derivation(Expression<T> toDerive, Space R) {
        this.toDerive = toDerive;
        this.R = R;
    }

    @Override
    public List<Expression<T>> getChildren() {
        return null;
    }

    /**
     * For Derivation class this function do nothing
     */
    @Override
    public Expression<T> calcul() {
        return this.toDerive.derive(R);
    }

    /**
     * For this class this function actually calculate the derivative (and don't derive another time)
     *
     * @param recursionDepth number maximal of recursion
     * @param R the space (can be null if T is Variable)
     * @return the derivative of toDerive
     */
    public Expression<T> derive(int recursionDepth, Space R) {
        if (recursionDepth == 0) {
            return toDerive;
        } else {
            return toDerive.derive(recursionDepth - 1, R);
        }
    }

    /**
     * For this class this function actually calculate the derivative (and don't derive another time)
     *
     * @param R the space (can be null if T is Variable)
     * @return the derivative of toDerive
     */
    @Override
    public Expression<T> derive(Space R) {
        return this.toDerive.derive(R);
    }


    @Override
    public boolean isVectorial() {
        return this.toDerive.isVectorial();
    }

    @Override
    public String toString() {
        if (this.R == null) {
            return String.format("\\dfrac{d %s}{dt}", toDerive.toString());
        } else {
            return String.format("\\left.\\dfrac{d %s}{dt}\\right|_{R_{%s}}", toDerive.toString(), R.getId());
        }
    }
}
