package program;

import matrix.*;
import errors.Errors;
import java.util.Scanner;

public class SPL {
    // Main SPL App
    public static void app() {
        int methodOption, inputOption;
        Scanner scanner = new Scanner(System.in);

        // Tampilan menu metode perhitungan
        System.out.println("=============  Pilih Metode Perhitungan:  =============");
        System.out.println("1. Gauss");
        System.out.println("2. Gauss - Jordan");
        System.out.println("3. Matriks Balikan");
        System.out.println("4. Cramer");

        // Input dan validasi metode perhitungan
        methodOption = scanner.nextInt();
        System.out.println("=======================================================");
        while (methodOption < 1 || methodOption > 4) {
            System.out.println("======================  ERROR  ========================");
            System.out.println("Input opsi tidak valid. Opsi harus bernilai bilangan bulat 1 sampai 4 (inklusif).");
            methodOption = scanner.nextInt();
            System.out.println("=======================================================");
        }

        // Tampilan menu pemilihan metode
        System.out.println("================  Pilih Metode Input:  ================");
        System.out.println("Pilih opsi masukan matriks:");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        // Input dan validasi metode input
        inputOption = scanner.nextInt();
        System.out.println("=======================================================");
        while (inputOption < 1 || inputOption > 2) {
            System.out.println("======================  ERROR  ========================");
            System.out.println("Input opsi tidak valid. Opsi harus bernilai bilangan bulat 1 sampai 2 (inklusif).");
            inputOption = scanner.nextInt();
            System.out.println("=======================================================");
        }

        // Input nilai matriks
        Matrix augmentedAb = new Matrix();
        System.out.println("=======================================================");
        System.out.println("Masukkan nilai matriks augmented A | b dimana Ax = b");
        switch (inputOption) {
            case 1:
                augmentedAb.readMatrixKeyboard();
                System.out.println("=======================================================");

                while (augmentedAb.getRow() > augmentedAb.getCol() - 1) {
                    System.out.println("======================  ERROR  ========================");
                    System.out.println(
                            "ERROR: Input matriks SPL tidak valid. Persamaan n variabel seharusnya memiliki paling banyak n persamaan.");
                    augmentedAb.readMatrixKeyboard();
                    System.out.println("=======================================================");
                }
                break;
            case 2:
                augmentedAb.readMatrixFile();
                System.out.println("=======================================================");

                while (augmentedAb.getRow() > augmentedAb.getCol() - 1) {
                    System.out.println("======================  ERROR  ========================");
                    System.out.println(
                            "ERROR: Input matriks tidak valid. Persamaan n variabel seharusnya memiliki paling banyak n persamaan.");
                    augmentedAb.readMatrixFile();
                    System.out.println("=======================================================");
                }
                break;
        }

        scanner.close();

        // Solve SPL
        switch (methodOption) {
            case 1:
                // Calculate
                String gaussResult = solveWithGauss(augmentedAb);

                // Print result
                System.out.println("======================  RESULT  =======================");
                System.out.println(gaussResult);
                System.out.println("=======================================================");

                break;
            case 2:
                // Calculate
                String gaussJordanResult = solveWithGaussJordan(augmentedAb);

                // Print result
                System.out.println("======================  RESULT  =======================");
                System.out.println(gaussJordanResult);
                System.out.println("=======================================================");

                break;
            case 3:
                try {
                    // Calculate
                    Matrix inverseResult = new Matrix();
                    inverseResult = solveWithInverse(augmentedAb);

                    // Print result
                    System.out.println("======================  RESULT  =======================");
                    inverseResult.printMatrix();
                    System.out.println("=======================================================");

                } catch (Errors.SPLUnsolvable e) {
                    System.out.println("======================  ERROR  ========================");
                    e.printStackTrace();
                    System.out.println("=======================================================");
                }
                break;
            case 4:
                try {
                    // Calculate
                    Matrix cramerResult = new Matrix();
                    cramerResult = solveWithCramer(augmentedAb);

                    // Print result
                    System.out.println("======================  RESULT  =======================");
                    cramerResult.printMatrix();
                    System.out.println("=======================================================");

                } catch (Errors.SPLUnsolvable e) {
                    System.out.println("======================  ERROR  ========================");
                    e.printStackTrace();
                    System.out.println("=======================================================");
                }
                break;
        }
    }

    // Solve with Gauss
    public static String solveWithGauss(Matrix aug) {
        // Lakukan substitusi balik dulu.
        // Substitusi balik = nge-0in angka diatas leading one.
        Matrix subsBalik = new Matrix();
        subsBalik.copy(aug);
        subsBalik.transformToEchelonForm();

        for (int i = 0; i < subsBalik.getRow(); i++) {
            int leadingOneIndex = 0;
            while (leadingOneIndex < subsBalik.getCol() - 1 && subsBalik.getElmt(i, leadingOneIndex) == 0) {
                leadingOneIndex += 1;
            }

            // Kasus 0 semua
            if (leadingOneIndex == subsBalik.getCol() - 1) {
                continue;
            }

            // Kasus ada leading one ketemu, substitusikan variable2 atasnya dengan cara
            // menambahkan rownya.
            for (int j = i - 1; j >= 0; j--) {
                subsBalik.addRow1WithKRow2(j, i, -1 * subsBalik.getElmt(j, leadingOneIndex));
            }
        }

        // Pisah matriks aug A | b.
        Matrix augA = new Matrix(subsBalik.getRow(), subsBalik.getCol() - 1);
        Matrix augB = new Matrix(subsBalik.getRow(), 1);
        for (int i = 0; i < subsBalik.getRow(); i++) {
            for (int j = 0; j < subsBalik.getCol(); j++) {
                double elmt = subsBalik.getElmt(i, j);
                if (j < subsBalik.getCol() - 1) {
                    // Augmented A (left)
                    augA.setElmt(i, j, elmt);
                } else {
                    // Augmented b (right)
                    augB.setElmt(i, 0, elmt);
                }
            }
        }

        // SOLUSI TIDAK ADA
        // 0 0 ... 0 | != 0
        boolean noSolution = true;
        for (int i = 0; i < subsBalik.getCol(); i++) {
            // Last row of augmented A is not all zero
            if (((i < subsBalik.getCol() - 1) && subsBalik.getElmt(subsBalik.getRow() - 1, i) != 0)
                    || ((i == subsBalik.getCol() - 1) && subsBalik.getElmt(subsBalik.getRow() - 1, i) == 0)) {
                noSolution = false;
            }
        }
        if (noSolution) {
            return "SPL ini tidak memiliki solusi sama sekali.";
        }

        // SOLUSI UNIK
        // Handle solusi unik = Square and All diagonal one
        // Cek apakah diagonal augmentedA bernilai 1 semua (leading ones)
        if (augA.isSquare() && augA.isIdentity()) {
            String msg = "";
            for (int i = 0; i < augB.getRow(); i++) {
                msg += String.format("x_%d = %.2f\n", i + 1, augB.getElmt(i, 0));
            }
            return msg;
        }

        // SOLUSI BANYAK
        String msg = "";

        // Handle kasus suatu kolom 0 semua xi = ti
        for (int j = 0; j < augA.getCol(); j++) {
            boolean allZero = true;
            for (int i = 0; i < augA.getRow(); i++) {
                if (augA.getElmt(i, j) != 0) {
                    allZero = false;
                    break;
                }
            }

            if (allZero) {
                msg += String.format("x_%d = t_%d\n", j + 1, j + 1);
            }
        }

        for (int i = 0; i < augA.getRow(); i++) {
            // Get leading one index
            int leadingOneIndex = 0;
            while (augA.getElmt(i, leadingOneIndex) == 0) {
                leadingOneIndex += 1;
                if (leadingOneIndex == augA.getCol()) {
                    break;
                }
            }

            // Tidak ada leading one
            if (leadingOneIndex == augA.getCol()) {
                continue;
            }

            // Ada leading 1
            // String definisi variable sebagai parameter, misalnya x_i = t_i
            for (int j = leadingOneIndex + 1; j < augA.getCol(); j++) {
                if (augA.getElmt(i, j) == 0) {
                    continue;
                }

                if (i == 0) {
                    msg += String.format("x_%d = t_%d\n", j + 1, j + 1);
                } else if (i > 0) {
                    boolean noParamPrinted = true;
                    for (int k = i - 1; k >= 0; k--) {
                        if (augA.getElmt(k, j) != 0) {
                            noParamPrinted = false;
                        }
                    }
                    if (noParamPrinted) {
                        msg += String.format("x_%d = t_%d\n", j + 1, j + 1);
                    }
                }
            }

            // String persamaan menggunakan parametrik2 yang didefinisikan sebelumnya
            // misalnya x1 = 2 + 2t_1 + 2t_2 + ...
            msg += String.format("x_%d = ", leadingOneIndex + 1);

            // Cek apakah setelah leading one ada 0 atau tidak
            boolean allZeroAfterLeadingOne = true;
            for (int j = leadingOneIndex + 1; j < augA.getCol(); j++) {
                if (augA.getElmt(i, j) != 0) {
                    allZeroAfterLeadingOne = false;
                    break;
                }
            }

            double augBElmt = augB.getElmt(i, 0);
            boolean isFirst = true;
            // Semua after leading A 0 di augA (tidak ada parametrik), cetak augB jika
            // nilainya 0.
            if (allZeroAfterLeadingOne) {
                msg += String.format("%.2f", augBElmt);
                isFirst = false;
            }
            // Ada bilangan bukan 0 setelah leading one (ada parametrik), jangan cetak augB
            // jika nilainya 0.
            if (!allZeroAfterLeadingOne && augBElmt != 0) {
                msg += String.format("%.2f", augBElmt);
                isFirst = false;
            }

            // Parametrik
            // Pakai t_i karena mengingat karakter huruf terbatas.
            for (int j = leadingOneIndex + 1; j < augA.getCol(); j++) {
                double elmt = augA.getElmt(i, j);

                if (elmt == 0) {
                    continue;
                }

                if (isFirst) {
                    // Iterasi pertama dan elemen augB = 0
                    if (elmt != 1.0 && elmt != -1.0) {
                        // Koefisien elemen tidak 1 atau -1, cetak bilangannya juga
                        // (tanda berubah jika pindah ruas)
                        if (elmt > 0) {
                            msg += String.format("%.2f", -1 * elmt);
                        } else {
                            msg += String.format("%.2f", -1 * elmt);
                        }
                    } else {
                        // Jika koefisien satu, cetak tandanya saja
                        // (tanda berubah jika pindah ruas)
                        if (elmt == 1.0) {
                            msg += "-";
                        }
                    }

                    msg += String.format("t_%d", j + 1);
                    isFirst = false;
                } else {
                    // Iterasi selanjutnya
                    if (elmt != 1.0 && elmt != -1.0) {
                        // Koefisien elemen tidak 1 atau -1, cetak bilangannya juga
                        // (tanda berubah jika pindah ruas)
                        if (elmt > 0) {
                            msg += String.format(" - %.2f", elmt);
                        } else {
                            msg += String.format(" + %.2f", -1 * elmt);
                        }
                    } else {
                        // Jika koefisien satu atau -1, cetak tandanya saja
                        // (tanda berubah jika pindah ruas)
                        if (elmt == 1.0) {
                            msg += " - ";
                        } else {
                            msg += " + ";
                        }
                    }
                    msg += String.format("t_%d", j + 1);
                }
            }
            msg += "\n";
        }
        msg += "Dimana t_i adalah parametrik t ke i dan t anggota bilangan Real.";

        return msg;
    }

    // Solve with Gauss Jordan
    public static String solveWithGaussJordan(Matrix aug) {
        // Reduce to row echelon form
        Matrix reduced = new Matrix();
        reduced.copy(aug);
        reduced.transformToReducedEchelonForm();

        // Pisah matriks aug A | b.
        Matrix augA = new Matrix(aug.getRow(), aug.getCol() - 1);
        Matrix augB = new Matrix(aug.getRow(), 1);
        for (int i = 0; i < aug.getRow(); i++) {
            for (int j = 0; j < aug.getCol(); j++) {
                double elmt = reduced.getElmt(i, j);
                if (j < aug.getCol() - 1) {
                    // Augmented A (left)
                    augA.setElmt(i, j, elmt);
                } else {
                    // Augmented b (right)
                    augB.setElmt(i, 0, elmt);
                }
            }
        }

        // SOLUSI TIDAK ADA
        // 0 0 ... 0 | != 0
        boolean noSolution = true;
        for (int i = 0; i < aug.getCol(); i++) {
            // Last row of augmented A is not all zero
            if (((i < aug.getCol() - 1) && reduced.getElmt(aug.getRow() - 1, i) != 0)
                    || ((i == aug.getCol() - 1) && reduced.getElmt(aug.getRow() - 1, i) == 0)) {
                noSolution = false;
            }
        }
        if (noSolution) {
            return "SPL ini tidak memiliki solusi sama sekali.";
        }

        // SOLUSI UNIK
        // Handle solusi unik = Square and All diagonal one
        // Cek apakah diagonal augmentedA bernilai 1 semua (leading ones)
        if (augA.isSquare() && augA.isIdentity()) {
            String msg = "";
            for (int i = 0; i < augB.getRow(); i++) {
                msg += String.format("x_%d = %.2f\n", i + 1, augB.getElmt(i, 0));
            }
            return msg;
        }

        // SOLUSI BANYAK
        String msg = "";

        // Handle kasus suatu kolom 0 semua xi = ti
        for (int j = 0; j < augA.getCol(); j++) {
            boolean allZero = true;
            for (int i = 0; i < augA.getRow(); i++) {
                if (augA.getElmt(i, j) != 0) {
                    allZero = false;
                    break;
                }
            }

            if (allZero) {
                msg += String.format("x_%d = t_%d\n", j + 1, j + 1);
            }
        }

        for (int i = 0; i < augA.getRow(); i++) {
            // Get leading one index
            int leadingOneIndex = 0;
            while (augA.getElmt(i, leadingOneIndex) == 0) {
                leadingOneIndex += 1;
                if (leadingOneIndex == augA.getCol()) {
                    break;
                }
            }

            // Tidak ada leading one
            if (leadingOneIndex == augA.getCol()) {
                continue;
            }

            // Ada leading 1
            // String definisi variable sebagai parameter, misalnya x_i = t_i
            for (int j = leadingOneIndex + 1; j < augA.getCol(); j++) {
                if (augA.getElmt(i, j) == 0) {
                    continue;
                }

                if (i == 0) {
                    msg += String.format("x_%d = t_%d\n", j + 1, j + 1);
                } else if (i > 0) {
                    boolean noParamPrinted = true;
                    for (int k = i - 1; k >= 0; k--) {
                        if (augA.getElmt(k, j) != 0) {
                            noParamPrinted = false;
                        }
                    }
                    if (noParamPrinted) {
                        msg += String.format("x_%d = t_%d\n", j + 1, j + 1);
                    }
                }
            }

            // String persamaan menggunakan parametrik2 yang didefinisikan sebelumnya
            // misalnya x1 = 2 + 2t_1 + 2t_2 + ...
            msg += String.format("x_%d = ", leadingOneIndex + 1);

            // Cek apakah setelah leading one ada 0 atau tidak
            boolean allZeroAfterLeadingOne = true;
            for (int j = leadingOneIndex + 1; j < augA.getCol(); j++) {
                if (augA.getElmt(i, j) != 0) {
                    allZeroAfterLeadingOne = false;
                    break;
                }
            }

            double augBElmt = augB.getElmt(i, 0);
            boolean isFirst = true;
            // Semua after leading A 0 di augA (tidak ada parametrik), cetak augB jika
            // nilainya 0.
            if (allZeroAfterLeadingOne) {
                msg += String.format("%.2f", augBElmt);
                isFirst = false;
            }
            // Ada bilangan bukan 0 setelah leading one (ada parametrik), jangan cetak augB
            // jika nilainya 0.
            if (!allZeroAfterLeadingOne && augBElmt != 0) {
                msg += String.format("%.2f", augBElmt);
                isFirst = false;
            }

            // Parametrik
            // Pakai t_i karena mengingat karakter huruf terbatas.
            for (int j = leadingOneIndex + 1; j < augA.getCol(); j++) {
                double elmt = augA.getElmt(i, j);

                if (elmt == 0) {
                    continue;
                }

                if (isFirst) {
                    // Iterasi pertama dan elemen augB = 0
                    if (elmt != 1.0 && elmt != -1.0) {
                        // Koefisien elemen tidak 1 atau -1, cetak bilangannya juga
                        // (tanda berubah jika pindah ruas)
                        if (elmt > 0) {
                            msg += String.format("%.2f", -1 * elmt);
                        } else {
                            msg += String.format("%.2f", -1 * elmt);
                        }
                    } else {
                        // Jika koefisien satu, cetak tandanya saja
                        // (tanda berubah jika pindah ruas)
                        if (elmt == 1.0) {
                            msg += "-";
                        }
                    }

                    msg += String.format("t_%d", j + 1);
                    isFirst = false;
                } else {
                    // Iterasi selanjutnya
                    if (elmt != 1.0 && elmt != -1.0) {
                        // Koefisien elemen tidak 1 atau -1, cetak bilangannya juga
                        // (tanda berubah jika pindah ruas)
                        if (elmt > 0) {
                            msg += String.format(" - %.2f", elmt);
                        } else {
                            msg += String.format(" + %.2f", -1 * elmt);
                        }
                    } else {
                        // Jika koefisien satu atau -1, cetak tandanya saja
                        // (tanda berubah jika pindah ruas)
                        if (elmt == 1.0) {
                            msg += " - ";
                        } else {
                            msg += " + ";
                        }
                    }
                    msg += String.format("t_%d", j + 1);
                }
            }
            msg += "\n";
        }
        msg += "Dimana t_i adalah parametrik t ke i dan t anggota bilangan Real.";

        return msg;
    }

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
            inverseA = augmentedA.getInverseByERO();

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
