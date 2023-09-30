package program;

import matrix.*;
import errors.*;
import lib.fileOutput;

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

        // Input Peubah Yang Ingin Ditaksir
        double temp;
        Matrix input = new Matrix(n, 1);
        System.out.println("============== Masukkan nilai-nilai x_i ===============");

        for (int i = 0; i < n; i++) {
            if (i == 0) {
                System.out.print("Masukkan nilai x_" + (i + 1));
            } else {
                System.out.print(", x_" + (i + 1));
            }

        }
        System.out.println("");

        for (int i = 0; i < n; i++) {
            temp = sc.nextDouble();
            input.setElmt(i, 0, temp);
        }

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
            solution = SPL.solveWithCramer(echelon);

            // Print result
            // System.out.println("====================== RESULT =======================");
            // solution.printMatrix();
            // System.out.println("=======================================================");

            // Bentuk Persamaan
            System.out.println("======================  RESULT  =======================");
            String msg = " ";
            for (int i = 0; i < solution.getRow(); i++) {
                if (i == 0) {
                    System.out.print("f(x) = " + solution.getElmt(0, 0));
                    msg += solution.getElmt(0, 0);
                } else {
                    if (solution.getElmt(i, 0) >= 0) {
                        System.out.print(" + " + solution.getElmt(i, 0) + " x_" + i);
                        msg += " + " + solution.getElmt(i, 0) + " x_" + i;
                    } else {
                        System.out.print(" - " + (solution.getElmt(i, 0) * (-1)) + " x_" + i);
                        msg += " - " + (solution.getElmt(i, 0) * (-1)) + " x_" + i;
                    }
                }
            }
            msg += "\n";
            System.out.print(", ");

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
            System.out.println("");
            System.out.print("f( " + input.getElmt(0, 0));
            for (int i = 1; i < n; i++) {
                System.out.print(", " + (input.getElmt(i, 0)));
            }
            System.out.println(" ) = " + total);
            System.out.println("=======================================================");

            // Convert hasil dari taksiran ke string agar bisa disimpan
            msg += String.format("%.4f", total);

            Scanner reader = new Scanner(System.in);
            System.out.println("Apakah anda ingin menyimpan hasilnya? (y/n)");
            String save2 = reader.next();
            if (save2.equals("y")) {
                fileOutput saveFile = new fileOutput();
                saveFile.saveString(msg);
            }
            reader.close();

        } catch (Errors.SPLUnsolvable e) {
            System.out.println("========================  ERROR  ======================");
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
}