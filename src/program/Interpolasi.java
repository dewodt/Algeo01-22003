package program;

import matrix.*;

import java.util.NoSuchElementException;
import java.util.Scanner;

import lib.fileOutput;

public class Interpolasi {
    private static Scanner reader = new Scanner(System.in);

    // Tampilan menu metode input
    public static void app() {
        System.out.println("================  Pilih Metode Input:  ================");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        int input = reader.nextInt();
        while (input < 1 || input > 2) {
            System.out.println("======================  ERROR  ========================");
            System.out.println("Pilihan harus angka bilangan bulat dari 1 sampai 2!");
            input = reader.nextInt();
            System.out.println("=======================================================");
        }

        // Tangani pilihan input
        Matrix data = new Matrix();
        switch (input) {
            case 1:
                System.out.println("================  Input Jumlah Data :  ================");
                int matrixSize = reader.nextInt();
                data = new Matrix(matrixSize, 2);
                for (int i = 0; i < matrixSize; i++) {
                    System.out.println("Masukkan nilai x ke-" + (i + 1));
                    data.setElmt(i, 0, reader.nextDouble());
                    System.out.println("Masukkan nilai y ke-" + (i + 1));
                    data.setElmt(i, 1, reader.nextDouble());
                }
                Matrix augMatrix = generateMatrix(data);
                double result = solveInterpolation(augMatrix);

                System.out.println("Nilai f(x) = " + result);
                String msg = " ";
                // Simpan hasil persamaan dalam bentuk string (ke msg) untuk disimpan ke file
                for (int i = 0; i < augMatrix.getRow(); i++) {
                    if (i == 0) {
                        msg += augMatrix.getElmt(i, augMatrix.getCol() - 1);
                    } else if (augMatrix.getElmt(i, augMatrix.getCol() - 1) >= 0) {
                        msg += " + " + augMatrix.getElmt(i, augMatrix.getCol() - 1) + "x^" + i;
                    } else {
                        msg += " - " + augMatrix.getElmt(i, augMatrix.getCol() - 1) * -1 + "x^" + i;
                    }
                }
                msg += "\n";
                // Convert hasil dari taksiran ke string agar bisa disimpan
                msg += String.format("%.4f", result);

                System.out.println("=======================================================");
                System.out.println("Apakah anda ingin menyimpan hasilnya? (y/n)");
                String save = reader.next();
                if (save.equals("y")) {
                    fileOutput saveFile = new fileOutput();
                    saveFile.saveString(msg);
                }
                System.out.println("=======================================================");
                break;
            case 2:
                data.readMatrixFile();
                while (data.getCol() != 2) {
                    System.out.println("File tidak valid, silahkan masukkan file yang valid (kolom = 2) !");
                    data.readMatrixFile();
                }
                Matrix augMatrix2 = generateMatrix(data);
                double result2 = solveInterpolation(augMatrix2);
                System.out.println("Nilai f(x) = " + result2);
                String msg2 = " ";
                // Simpan hasil persamaan dalam bentuk string (ke msg) untuk disimpan ke file
                for (int i = 0; i < augMatrix2.getRow(); i++) {
                    if (i == 0) {
                        msg2 += augMatrix2.getElmt(i, augMatrix2.getCol() - 1);
                    } else if (augMatrix2.getElmt(i, augMatrix2.getCol() - 1) >= 0) {
                        msg2 += " + " + augMatrix2.getElmt(i, augMatrix2.getCol() - 1) + "x^" + i;
                    } else {
                        msg2 += " - " + augMatrix2.getElmt(i, augMatrix2.getCol() - 1) * -1 + "x^" + i;
                    }
                }
                msg2 += "\n";
                // Convert hasil dari taksiran ke string agar bisa disimpan
                msg2 += String.format("%.4f", result2);

                System.out.println("=======================================================");
                System.out.println("Apakah anda ingin menyimpan hasilnya? (y/n)");
                String save2 = reader.next();
                if (save2.equals("y")) {
                    fileOutput saveFile = new fileOutput();
                    saveFile.saveString(msg2);
                }
                System.out.println("=======================================================");
                break;
        }

    }

    // Ambil data dari file dengan isi (x,y) atau dari input user dan ubah ke
    // augmented matrix
    public static Matrix generateMatrix(Matrix data) {
        double a, b, powX;
        int lenData;

        // Ambil jumlah data
        lenData = data.getRow();

        // Buat matrix n x n+1 dengan n jumlah data dan kolom terakhir y
        Matrix augMatrix = new Matrix(lenData, lenData + 1);

        for (int i = 0; i < lenData; i++) {
            a = data.getElmt(i, 0); // a adalah x
            b = data.getElmt(i, 1); // b adalah y
            powX = 1; // powX adalah nilai pangkat x dimulai dari 1 (x pangkat 0), x, x^2, x^3, dst
            for (int j = 0; j < augMatrix.getCol(); j++) {
                if (j != augMatrix.getCol() - 1) {
                    // Set powX untuk x utnuk a0 (x pangkat 0), a1 (x), a2 (x^2),dst
                    augMatrix.setElmt(i, j, powX);
                    powX = powX * a;
                } else {
                    // Otherwise, kalau j = kolom terakhir matrix augmented (kolom n+1),
                    // maka set untuk nilai y (kolom n+1)
                    augMatrix.setElmt(i, j, b);
                }
            }
        }
        return augMatrix;
    }

    public static double solveInterpolation(Matrix augMatrix) throws NoSuchElementException {
        double result = 0, pangkatX = 1;

        // Jadika Augmented matrix tadi ke bentuk reduced echelon form
        augMatrix.transformToReducedEchelonForm();

        // Tampilkan nilai a yang diperoleh dengan reduced echelon form
        System.out.println("Berikut adalah f(x) nya dengan nilai a yang telah diperoleh dari OBE : ");
        for (int i = 0; i < augMatrix.getRow(); i++) {
            // a0
            if (i == 0) {
                System.out.print(augMatrix.getElmt(i, augMatrix.getCol() - 1));
            }
            // ai jika ai bukan negatif
            else if (augMatrix.getElmt(i, augMatrix.getCol() - 1) >= 0) {
                System.out.print(" + " + augMatrix.getElmt(i, augMatrix.getCol() - 1) + "x^" + i);
            }
            // ai jika nilainya negatif
            else {
                // kali dengan -1 agar tidak double negatif (supaya rapi aja)
                System.out.print(" - " + augMatrix.getElmt(i, augMatrix.getCol() - 1) * -1 + "x^" + i);
            }
        }

        System.out.println("\n\nAkan ditaksir nilai fungsi, silahkan masukkan X : \n");
        double x = reader.nextDouble();
        System.out.println(" ");
        for (int i = 0; i < augMatrix.getRow(); i++) {
            result += augMatrix.getElmt(i, augMatrix.getCol() - 1) * pangkatX;
            pangkatX = pangkatX * x;
        }

        return result;
    }
}
