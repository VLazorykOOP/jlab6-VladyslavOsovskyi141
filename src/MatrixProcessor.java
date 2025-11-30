// MatrixProcessor.java
package src;
import java.io.*;
import java.util.Scanner;

public class MatrixProcessor {

    /**
     * Зчитує квадратну матрицю з файлу у форматі:
     * n
     * a11 a12 ... a1n
     * ...
     * an1 an2 ... ann
     *
     * Повертає double[n][n].
     *
     * Можливі виключення:
     * - FileNotFoundException
     * - NumberFormatException (через неверний формат чисел)
     * - IOException (якщо рядків/елементів менше ніж потрібно)
     */
    public static double[][] loadMatrixFromFile(File file) throws IOException {
        try (Scanner sc = new Scanner(file)) {
            if (!sc.hasNext()) throw new IOException("Empty file");
            String token = sc.next();
            int n;
            try {
                n = Integer.parseInt(token);
            } catch (NumberFormatException ex) {
                throw new NumberFormatException("First token must be integer n (size). Found: " + token);
            }
            if (n <= 0 || n > 20) throw new IOException("n must be between 1 and 20. Found: " + n);

            double[][] A = new double[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (!sc.hasNext()) throw new IOException("Not enough matrix elements in file (expected " + (n*n) + ").");
                    String val = sc.next();
                    try {
                        A[i][j] = Double.parseDouble(val);
                    } catch (NumberFormatException ex) {
                        throw new NumberFormatException("Invalid number at row " + (i+1) + ", col " + (j+1) + ": " + val);
                    }
                }
            }
            return A;
        }
    }

    /**
     * Обчислює вектор X згідно правила:
     * X(i) = сума модулів елементів між першим і останнім включно додатними елементами i-го рядка.
     * Якщо додатних елементів 0 або 1 -> X(i) = -1.
     *
     * Якщо для будь-якого рядка сума > threshold -> кидається MyArithmeticException з поясненням.
     */
    public static double[] computeX(double[][] A, double thresholdForException) {
        int n = A.length;
        double[] X = new double[n];

        for (int i = 0; i < n; i++) {
            int firstPos = -1, lastPos = -1;
            for (int j = 0; j < n; j++) {
                if (A[i][j] > 0) {
                    if (firstPos == -1) firstPos = j;
                    lastPos = j;
                }
            }
            if (firstPos == -1 || firstPos == lastPos) {
                X[i] = -1;
            } else {
                double sum = 0.0;
                for (int j = firstPos; j <= lastPos; j++) sum += Math.abs(A[i][j]);

                if (sum > thresholdForException) {
                    throw new MyArithmeticException("Sum for row " + (i+1) + " is " + sum + " > " + thresholdForException);
                }

                X[i] = sum;
            }
        }

        return X;
    }
}
