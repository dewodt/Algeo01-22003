package program;

import matrix.*;
import matrix.Error.InvalidMatrixSizeException;
import matrix.Error.NoInverseMatrixException;

import java.util.Scanner;

public class SPL {
    // Main SPL App
    public static void app() {
        int methodOption, inputOption;
        Scanner scanner = new Scanner(System.in);

        // Input opsi metode penyelesaian SPL
        System.out.println("============== SPL ===============");
        System.out.println("Pilih opsi metode penyelesain SPL:");
        System.out.println("1. Gauss");
        System.out.println("2. Gauss - Jordan");
        System.out.println("3. Matriks Balikan");
        System.out.println("4. Cramer");
        System.out.println("==================================");

        methodOption = scanner.nextInt();
        while (methodOption < 1 || methodOption > 4) {
            System.out.println("==================================");
            System.out.println("Input opsi tidak valid. Opsi harus bernilai bilangan bulat 1 sampai 4 (inklusif).");
            methodOption = scanner.nextInt();
            System.out.println("==================================");
        }

        // Input opsi pemasukan data
        System.out.println("============== SPL ===============");
        System.out.println("Pilih opsi masukan matriks:");
        System.out.println("1. Keyboard");
        System.out.println("2. File");
        System.out.println("==================================");

        inputOption = scanner.nextInt();
        while (inputOption < 1 || inputOption > 2) {
            System.out.println("==================================");
            System.out.println("Input opsi tidak valid. Opsi harus bernilai bilangan bulat 1 sampai 2 (inklusif).");
            inputOption = scanner.nextInt();
            System.out.println("==================================");
        }

        // Input nilai matriks
        Matrix augmentedAb = new Matrix();
        System.out.println("==================================");
        System.out.println("Masukkan nilai matriks augmented A | b dimana Ax = b");
        switch (inputOption) {
            case 1:
                augmentedAb.readMatrixKeyboard();
                while (augmentedAb.getRow() > augmentedAb.getCol() - 1) {
                    System.out.println(
                            "ERROR: Input matriks SPL tidak valid. Persamaan n variabel seharusnya memiliki paling banyak n persamaan.");
                    augmentedAb.readMatrixKeyboard();
                }
            case 2:
                augmentedAb.readMatrixFile();
                while (augmentedAb.getRow() > augmentedAb.getCol() - 1) {
                    System.out.println(
                            "ERROR: Input matriks tidak valid. Persamaan n variabel seharusnya memiliki paling banyak n persamaan.");
                    augmentedAb.readMatrixFile();
                }
        }

        // Pisah augmented matriks a dan b

        scanner.close();
        System.out.println("==================================");

        // Solve SPL
        switch (methodOption) {
            case 1:
                System.out.println("GAUSS");
            case 2:
                System.out.println("GAUSS JORDAN");
            case 3:
                System.out.println("Matriks Balikan");
                solveWithInverse(augmentedAb);
            case 4:
                System.out.println("Cramer");
        }
    }

    // Solve with inverse
    public static void solveWithInverse(Matrix augmented) {
        // Metode balikan matriks hanya bisa menyelesaikan SPL dengan solusi unik.

        // Get augmented row & col
        int augRow = augmented.getRow();
        int augCol = augmented.getCol();

        // Pisah matriks augmented A | b.
        Matrix augmentedA = new Matrix(augRow, augCol - 1);
        Matrix augmentedB = new Matrix(augRow, 1);
        for (int i = 0; i < augRow; i++) {
            for (int j = 0; j < augCol; j++) {
                double elmt = augmented.getElmt(i, j);
                if (j < augCol - 1) {
                    // Augmented A (left)
                    augmentedA.setElmt(i, j, elmt);
                } else {
                    // Augmented b (right)
                    augmentedB.setElmt(i, 0, elmt);
                }
            }
        }

        // Jika memiliki inverse matriks
        try {
            Matrix inverseA = new Matrix(augRow, augCol);
            inverseA = augmentedA.inverseByERO();

            Matrix result = new Matrix(augRow, 1);
            result = Matrix.multiplyMatrix(inverseA, augmentedB);

            result.printMatrix();
        } catch (NoInverseMatrixException | InvalidMatrixSizeException e) {
            System.out.println("Gagal menggunakan metode balikan matriks");
            System.out.println(e);
        }
    }

    // Solve with cramer
    public void solveWithCramer() {

    }
}
