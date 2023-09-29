package program;

import program.*;
import matrix.*;
import java.util.Scanner;
import program.SPL;
import errors.Errors;

public class LinierBerganda {
    public static void app() {
        Scanner sc = new Scanner(System.in);

        // Input Jumlah Peubah
        System.out.println("Masukkan Jumlah Peubah X: ");
        System.out.println("=======================================================");
        int n = sc.nextInt(); // Jumlah Peubah
        while (n < 0) {
            System.out.println("Masukkan Jumlah Peubah X > 0! ");
            n = sc.nextInt();
        }

        // Input Jumlah Sampel
        System.out.println("=======================================================");
        System.out.println("Masukkan Jumlah Sampel: ");
        System.out.println("=======================================================");
        int m = sc.nextInt();
        while (m < 0) {
            System.out.println("Masukkan Jumlah Sampel > 0! ");
            m = sc.nextInt();
        }

        // Matriks Menyimpan bentuk mSPL
        Matrix mSPL = new Matrix(n + 1, n + 2);
        // Matriks Menyimpan Input
        Matrix inputM = new Matrix(m, n + 1);

        // Input File
        System.out.println("Input File:");
        inputM.readMatrixFile();

        // Konversi ke SPL
        double value;
        int k = 0;
        for (int i = 0; i < mSPL.getRow(); i++) {
            for (int j = 0; j < mSPL.getCol(); j++) {

                // Baris Pertama
                if (i == 0) {
                    if (j == 0) {
                        value = inputM.getRow();
                    } else {
                        value = Sum(inputM, j - 1);
                    }
                }
                // Selain Baris Pertama
                else {
                    // b0
                    if (j == 0) {
                        value = Sum(inputM, k);
                    } else {
                        value = SumMultiply(inputM, j - 1, k);
                    }
                }
                mSPL.setElmt(i, j, value);
            }
            if (i != 0) {
                k++;
            }

        }

        // Print SPL
        System.out.println("================= SPL yang diperoleh: =================");
        mSPL.printMatrix();
        System.out.println("=======================================================");

        // Matriks Echelon Tereduksi
        Matrix echelon = new Matrix();
        echelon.copy(mSPL);
        echelon.transformToReducedEchelonForm();
        // Outputnya
        System.out.println("================ Reduced Echelon Form: ================");
        echelon.printMatrix();
        System.out.println("=======================================================");

        // Matriks Yang menyimpan solusi
        Matrix solution = new Matrix();
        solution = solveWithCramer(echelon);

        // Bentuk Persamaan
        System.out.println("======================  RESULT  =======================");
        for (int i = 0; i < solution.getCol(); i++) {
            if (i == 0) {
                System.out.println("f(x) = " + solution.getElmt(0, 0));
            } else {
                System.out.println(" " + solution.getElmt(0, i) + "x_" + i);
            }

        }
        System.out.println("=======================================================");

        // Input Peubah Yang Ingin Ditaksir
        Matrix input = new Matrix(1, solution.getCol());
        System.out.println("============== Masukkan nilai-nilai x_i ===============");
        for (int i = 0; i < input.getCol(); i++) {
            System.out.println("Masukkan nilai x_" + (i + 1) + ": ");
            input.setElmt(0, i, sc.nextDouble());
        }

        // Taksiran
        double total = 0;
        double first = solution.getElmt(0, 0);
        for (int i = 1; i < solution.getCol(); i++) {
            total += solution.getElmt(0, i) * input.getElmt(0, i);
        }

        // Output Taksiran
        System.out.println("==================== Hasil Taksiran ===================");
        System.out.println("f(x_i) = " + total);

        // Close Scanner
        sc.close();

    }

    public static double Sum(Matrix m, int colIdx) {
        double sum = 0;
        for (int i = 0; i < (m.getRow()); i++) {
            sum += m.getElmt(i, colIdx);
        }
        return sum;
    }

    public static double SumMultiply(Matrix m, int colIdx, int colMultiply) {
        double sum = 0;
        for (int i = 0; i < (m.getRow()); i++) {

            sum += m.getElmt(i, colIdx) * m.getElmt(i, colMultiply);
        }
        return sum;
    }

}