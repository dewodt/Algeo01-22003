package program;

import matrix.*;
import errors.*;
import java.util.Scanner;

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

        // Tampilan menu metode input
        System.out.println("================  Pilih Metode Input:  ================");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        // Input File
        int inputMethod = sc.nextInt();
        System.out.println("=======================================================");
        while (inputMethod < 1 || inputMethod > 2) {
            System.out.println("======================  ERROR  =======================");
            System.out.println("Pilihan harus angka bilangan bulat dari 1 sampai 2!");
            System.out.println("=======================================================");
        }

        // Penanganan pilihan input
        double tempo;
        switch (inputMethod) {
            case 1:
                System.out
                        .println("Masukkan nilai matriks (pisahkan kolom dengan spasi dan baris dengan enter/newline)");
                for (int i = 0; i < inputM.getRow(); i++) {
                    for (int j = 0; j < inputM.getCol(); j++) {
                        tempo = sc.nextDouble();
                        inputM.setElmt(i, j, tempo);
                    }
                }
                break;
            case 2:
                inputM.readMatrixFile();
                break;
        }

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
        // System.out.println("================= SPL yang diperoleh:
        // =================");
        // mSPL.printMatrix();
        // System.out.println("=======================================================");

        // Matriks Echelon Tereduksi
        Matrix echelon = new Matrix();
        echelon.copy(mSPL);
        echelon.transformToReducedEchelonForm();
        // Outputnya
        // System.out.println("================ Reduced Echelon Form:
        // ================");
        // echelon.printMatrix();
        // System.out.println("=======================================================");

        // Matriks Yang menyimpan solusi
        Matrix solution = new Matrix();
        try {
            // Calculate
            solution = Cramer(echelon);

            // Print result
            // System.out.println("====================== RESULT =======================");
            // solution.printMatrix();
            // System.out.println("=======================================================");

            // Bentuk Persamaan
            System.out.println("======================  RESULT  =======================");
            for (int i = 0; i < solution.getRow(); i++) {
                if (i == 0) {
                    System.out.print("f(x) = " + solution.getElmt(0, 0));
                } else {
                    if (solution.getElmt(i, 0) >= 0) {
                        System.out.print(" + " + solution.getElmt(i, 0) + " x_" + i);
                    } else {
                        System.out.print(" - " + (solution.getElmt(i, 0) * (-1)) + " x_" + i);
                    }

                }

            }
            System.out.println();
            System.out.println("=======================================================");

            // Input Peubah Yang Ingin Ditaksir
            double temp;
            Matrix input = new Matrix(n, 1);
            System.out.println("============== Masukkan nilai-nilai x_i ===============");
            for (int i = 0; i < n; i++) {
                System.out.println("Masukkan nilai x_" + (i + 1) + ": ");
                temp = sc.nextDouble();
                input.setElmt(i, 0, temp);
            }

            // Taksiran
            double total = 0;
            for (int i = 0; i < solution.getRow(); i++) {
                if (i == 0) {
                    total += solution.getElmt(i, 0);
                } else {
                    total += solution.getElmt(i, 0) * input.getElmt(i - 1, 0);
                }
            }

            // Output Taksiran
            System.out.println("==================== Hasil Taksiran ===================");
            System.out.print("f( " + input.getElmt(0, 0));
            for (int i = 1; i < n; i++) {
                System.out.print(", " + (input.getElmt(i, 0)));
            }
            System.out.println(" ) = " + total);
            System.out.println("=======================================================");

        } catch (Errors.SPLUnsolvable e) {
            System.out.println("======================  ERROR  ========================");
            e.printStackTrace();
            System.out.println("=======================================================");
        }

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

    // Solve with cramer
    public static Matrix Cramer(Matrix augmented) throws Errors.SPLUnsolvable {
        // Metode cramer hanya bisa SPL dengan solusi unik

        // Pisah matriks augmented A | b.
        Matrix augmentedA = new Matrix(augmented.getRow(), augmented.getCol() - 1);
        Matrix augmentedB = new Matrix(augmented.getRow(), 1);
        for (int i = 0; i < augmented.getRow(); i++) {
            for (int j = 0; j < augmented.getCol(); j++) {
                double elmt = augmented.getElmt(i, j);
                if (j < augmented.getCol() - 1) {
                    // Augmented A (left)
                    augmentedA.setElmt(i, j, elmt);
                } else {
                    // Augmented b (right)
                    augmentedB.setElmt(i, 0, elmt);
                }
            }
        }

        try {
            // Handle determinan 0, throw error
            double detA = augmentedA.getDeterminantByERO();

            if (detA == 0) {
                throw new Errors.DeterminanZeroException(
                        "SPL Tidak dapat diselesaikan dengan metode ini karena determinan = 0.");
            }

            // Determinan != 0
            Matrix result = new Matrix(augmented.getRow(), 1);

            for (int i = 0; i < augmentedA.getCol(); i++) {
                // Inisialisasi
                Matrix temp = new Matrix();
                temp.copy(augmentedA);

                // Ubah kolom ke i matriks temp menjadi matriks augmentedB
                for (int j = 0; j < augmentedA.getRow(); j++) {
                    double value = augmentedB.getElmt(j, 0);
                    temp.setElmt(j, i, value);
                }

                // Hitung nilai solusi xi
                double detTemp = temp.getDeterminantByERO();
                double x = detTemp / detA;

                // Simpan ke matriks result
                result.setElmt(i, 0, x);
            }

            return result;
        } catch (Errors.DeterminanZeroException | Errors.InvalidMatrixSizeException e) {
            System.out.println("Gagal menyelesaikan SPL menggunakan metode cramer.");
            e.printStackTrace();
            throw new Errors.SPLUnsolvable(
                    "SPL Ini tidak bisa diselesaikan dengan metode ini karena tidak memiliki solusi unik.");
        }
    }

}