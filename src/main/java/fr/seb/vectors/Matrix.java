/*
 * Copyright (c) 2023. Seb-sti1. See LICENSE.
 */

package fr.seb.vectors;

import fr.seb.Expression;
import fr.seb.function.Addition;
import fr.seb.function.Product;
import fr.seb.function.Scalar;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;

public class Matrix {

    public enum LATEX_MATRIX_ENV {pmatrix, bmatrix, Bmatrix, vmatrix}
    LATEX_MATRIX_ENV matrix_env = LATEX_MATRIX_ENV.pmatrix;

    final List<List<Expression<Variable>>> matrix = new ArrayList<>();
    final int n, p;

    /**
     * Create a matrix
     * @param n the number of lines
     * @param p the number of columns
     */
    public Matrix(int n, int p) {

        this.n = n;
        this.p = p;

        for (int i = 0; i < n; i++) {
            List<Expression<Variable>> line = new ArrayList<>();

            for (int j = 0; j < p; j++) {
                line.add(new Scalar(0));
            }
            matrix.add(line);
        }
    }

    /**
     * Set the way to display it in latex
     * @param env use the enum
     */
    public void setLatexMatrixEnv(LATEX_MATRIX_ENV env) {
        this.matrix_env = env;
    }

    /**
     * Return the (i, j) value
     * @param i the line
     * @param j the column
     * @return the value
     *
     * @throws IndexOutOfBoundsException : if out of the matrix
     */
    public Expression<Variable> get(int i, int j) {
        if (i < 0 || j < 0 || i >= n || j >= p) {
            throw new IndexOutOfBoundsException();
        }

        return matrix.get(i).get(j);
    }

    /**
     * Set the (i, j) value
     * @param i the line
     * @param j the column
     *
     * @throws IndexOutOfBoundsException : if out of the matrix
     */
    public void set(int i, int j, Expression<Variable> value) {
        if (i < 0 || j < 0 || i >= n || j >= p) {
            throw new IndexOutOfBoundsException();
        }

        List<Expression<Variable>> line = matrix.get(i);
        line.set(j, value);
        matrix.set(i, line);
    }

    /**
     * add the (i, j) value
     * @param i the line
     * @param j the column
     * @param value the value to add
     *
     * @throws IndexOutOfBoundsException : if out of the matrix
     */
    public void add(int i, int j, Expression<Variable> value) {
        if (i < 0 || j < 0 || i >= n || j >= p) {
            throw new IndexOutOfBoundsException();
        }

        set(i, j, Addition.CreateVariable(get(i, j), value));
    }

    /**
     * Do this + m
     * @param m the matrix to add
     * @return the result
     * @throws OperationNotSupportedException if number of column or row are not the same
     */
    public Matrix add(Matrix m) throws OperationNotSupportedException {
        if (m.n != this.n || m.p != this.p) {
            throw new OperationNotSupportedException();
        }

        Matrix result = new Matrix(n, p);

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.p; j++) {
                result.add(i, j, m.get(i, j));
            }
        }

        return result;
    }


    /**
     * Compute the matrix product of self*m
     * @param m the matrix to multiply (right multiplication)
     * @return the result
     * @throws OperationNotSupportedException the number of column or line must allow the operation
     */
    public Matrix product(Matrix m) throws OperationNotSupportedException {
        if (m.n != this.p) {
            throw new OperationNotSupportedException();
        }

        Matrix result = new Matrix(this.n, m.p);

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < m.p; j++) {

                List<Expression<Variable>> l = new ArrayList<>();

                for (int k = 0; k < this.p; k++) {
                    l.add(Product.Create(this.get(i, k), m.get(k, j)));
                }

                result.set(i, j, Addition.CreateVariable(l));
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder latex = new StringBuilder();

        latex.append(String.format("\\begin{%s}", this.matrix_env));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                latex.append(get(i,j).toString());

                if (j != p - 1)  {
                    latex.append(" & ");
                }
            }

            if (i != n - 1) {
                latex.append("\\\\");
            }
        }
        latex.append(String.format("\\end{%s}", this.matrix_env));

        return latex.toString();
    }

    // todo equals

}
