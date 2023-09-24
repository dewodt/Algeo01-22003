package program;

import matrix.*;
import errors.Errors;
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
                break;
            case 2:
                augmentedAb.readMatrixFile();
                while (augmentedAb.getRow() > augmentedAb.getCol() - 1) {
                    System.out.println(
                            "ERROR: Input matriks tidak valid. Persamaan n variabel seharusnya memiliki paling banyak n persamaan.");
                    augmentedAb.readMatrixFile();
                }
                break;
        }

        // Pisah augmented matriks a dan b

        scanner.close();
        System.out.println("==================================");

        // Solve SPL
        switch (methodOption) {
            case 1:
                System.out.println("GAUSS");
                break;
            case 2:
                System.out.println("GAUSS JORDAN");
                break;
            case 3:
                System.out.println("Matriks Balikan");
                try {
                    Matrix inverseResult = new Matrix();
                    inverseResult = solveWithInverse(augmentedAb);
                    inverseResult.printMatrix();
                } catch (Errors.SPLUnsolvable e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                System.out.println("Cramer");
                try {
                    Matrix cramerResult = new Matrix();
                    cramerResult = solveWithCramer(augmentedAb);
                    cramerResult.printMatrix();
                } catch (Errors.SPLUnsolvable e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    // Solve with Gauss

    // Solve with Gauss Jordan

    // Solve with inverse
    public static Matrix solveWithInverse(Matrix augmented) throws Errors.SPLUnsolvable {
        // Metode balikan matriks hanya bisa menyelesaikan SPL dengan solusi unik.

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

        // Jika memiliki inverse matriks
        try {
            Matrix result = new Matrix(augmented.getRow(), 1);
            Matrix inverseA = new Matrix();
            inverseA = augmentedA.inverseByERO();

            result = Matrix.multiplyMatrix(inverseA, augmentedB);

            return result;
        } catch (Errors.NoInverseMatrixException | Errors.InvalidMatrixSizeException e) {
            System.out.println("Gagal menggunakan metode balikan matriks");
            e.printStackTrace();
            throw new Errors.SPLUnsolvable(
                    "SPL Ini tidak bisa diselesaikan dengan metode ini karena tidak memiliki solusi unik.");
        }

    }

    // Solve with cramer
    public static Matrix solveWithCramer(Matrix augmented) throws Errors.SPLUnsolvable {
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
            double detA = augmentedA.determinantByERO();

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
                double detTemp = temp.determinantByERO();
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
