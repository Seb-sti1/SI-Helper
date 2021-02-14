package fr.seb.vectors;

import fr.seb.Expression;
import fr.seb.function.Addition;
import fr.seb.function.Scalar;

import java.util.ArrayList;
import java.util.List;

public class Matrix {

    public enum LATEX_MATRIX_ENV {pmatrix, bmatrix, Bmatrix, vmatrix}
    LATEX_MATRIX_ENV matrix_env = LATEX_MATRIX_ENV.pmatrix;

    final List<List<Expression<Variable>>> matrix = new ArrayList<>();
    final int n, p;

    public Matrix(int n, int p) {

        this.n = n;
        this.p = p;

        for (int i = 0; i < n; i++) {
            List<Expression<Variable>> line = new ArrayList<>();

            for (int j = 0; i < p; i++) {
                line.add(new Scalar<>(0));
            }
            matrix.add(line);
        }
    }

    public void setLatexMatrixEnv(LATEX_MATRIX_ENV env) {
        this.matrix_env = env;
    }

    public Expression<Variable> get(int i, int j) {
        if (i >= n || j >= p) {
            throw new IndexOutOfBoundsException();
        }

        return matrix.get(i).get(j);
    }

    public void set(int i, int j, Expression<Variable> value) {
        if (i >= n || j >= p) {
            throw new IndexOutOfBoundsException();
        }

        List<Expression<Variable>> line = matrix.get(i);
        line.set(j, value);
        matrix.set(i, line);
    }

    public void add(int i, int j, Expression<Variable> value) {
        if (i >= n || j >= p) {
            throw new IndexOutOfBoundsException();
        }

        set(i, j, new Addition<>(get(i, j), value));
    }

    // Todo : Matrix addition and product

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
                latex.append("//");
            }
        }
        latex.append(String.format("\\end{%s}", this.matrix_env));

        return latex.toString();
    }

}
