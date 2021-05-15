package fr.seb.function;

import fr.seb.Expression;
import fr.seb.space.Space;

import java.util.List;


public class Derivation<T> extends Expression<T> {

    final Expression<T> toDerive;
    final Space R;

    public Derivation(Expression<T> toDerive, Space R) {
        if (toDerive.hasMinus()) {
            this.invertSign();
            this.toDerive = toDerive.getPositiveClone();
        } else {
            this.toDerive = toDerive;
        }

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
        return this.toDerive.derive(R).needToInvertSign(this.hasMinus());
    }

    /**
     * For this class this function actually calculate the derivative (and don't derive another time)
     *
     * @param recursionDepth number maximal of recursion
     * @param R the space (can be null if T is Variable)
     * @return the derivative of toDerive
     */
    @Deprecated
    @Override
    public Expression<T> derive(int recursionDepth, Space R) {
        if (recursionDepth == 0) {
            return toDerive.needToInvertSign(this.hasMinus());
        } else {
            return toDerive.derive(recursionDepth - 1, R).needToInvertSign(this.hasMinus());
        }
    }

    /**
     * For this class this function actually calculate the derivative (and don't derive another time)
     *
     * @param R the space (can be null if T is Variable)
     * @return the derivative of toDerive
     */
    @Deprecated
    @Override
    public Expression<T> derive(Space R) {
        return this.toDerive.derive(R).needToInvertSign(this.hasMinus());
    }


    @Override
    public boolean isVectorial() {
        return this.toDerive.isVectorial();
    }

    @Override
    public String toString() {
        String minus = "";

        if (this.hasMinus()) {
            minus = "-";
        }

        if (this.R == null) {
            return String.format(minus + "\\dfrac{d %s}{dt}", toDerive.toString());
        } else {
            return String.format(minus + "\\left.\\dfrac{d %s}{dt}\\right|_{R_{%s}}", toDerive.toString(), R.getId());
        }
    }

    @Override
    public Expression<T> clone() {
        return new Derivation<>(this.toDerive, this.R).setSign(this.hasMinus());
    }

    @Override
    public Expression<T> getPositiveClone() {
        return new Derivation<>(this.toDerive, this.R);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Derivation) {
            if (((Derivation<?>) o).isVectorial() == this.isVectorial()) {

                if (this.isVectorial()) { // vector
                    return this.R == ((Derivation<?>) o).R && this.toDerive.equals(((Derivation<?>) o).toDerive) && this.hasMinus() == ((Derivation<?>) o).hasMinus();
                } else { // scalar
                    return this.toDerive.equals(((Derivation<?>) o).toDerive) && this.hasMinus() == ((Derivation<?>) o).hasMinus();
                }

            }
        }
        return false;
    }
}
