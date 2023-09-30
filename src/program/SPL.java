package program;

import matrix.*;
import errors.Errors;
import lib.fileOutput;

import java.util.Scanner;

public class SPL {
    private static Scanner scanner = new Scanner(System.in);

    // Main SPL App
    public static void app() {
        int methodOption, inputOption;

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
                break;
            case 2:
                augmentedAb.readMatrixFile();
                System.out.println("=======================================================");
                break;
        }

        // Solve SPL
        switch (methodOption) {
            case 1:
                // Calculate
                String gaussResult = solveWithGauss(augmentedAb);

                // Print result
                System.out.println("======================  RESULT  =======================");
                System.out.println(gaussResult);
                System.out.println("=======================================================");
                System.out.println("Apakah anda ingin menyimpan hasilnya? (y/n)");
                scanner.nextLine();
                String save = scanner.nextLine();
                if (save.equals("y")) {
                    fileOutput saveFile = new fileOutput();
                    saveFile.saveString(gaussResult);
                }
                System.out.println("=======================================================");

                break;
            case 2:
                // Calculate
                String gaussJordanResult = solveWithGaussJordan(augmentedAb);

                // Print result
                System.out.println("======================  RESULT  =======================");
                System.out.println(gaussJordanResult);
                System.out.println("=======================================================");
                System.out.println("Apakah anda ingin menyimpan hasilnya? (y/n)");
                scanner.nextLine();
                String save2 = scanner.nextLine();
                if (save2.equals("y")) {
                    fileOutput saveFile = new fileOutput();
                    saveFile.saveString(gaussJordanResult);
                }
                System.out.println("=======================================================");

                break;
            case 3:
                try {
                    // Calculate
                    Matrix inverseResult = new Matrix();
                    inverseResult = solveWithInverse(augmentedAb);
                    String msg = "";
                    for (int i = 0; i < inverseResult.getRow(); i++) {
                        msg += ("x_" + (i + 1) + " = " + inverseResult.getElmt(i, 0));
                        if (i != inverseResult.getRow() - 1) {
                            msg += "\n";
                        }
                    }

                    // Print result
                    System.out.println("======================  RESULT  =======================");
                    System.out.println(msg);
                    System.out.println("=======================================================");
                    System.out.println("Apakah anda ingin menyimpan hasilnya? (y/n)");
                    scanner.nextLine();
                    String save3 = scanner.nextLine();
                    if (save3.equals("y")) {
                        fileOutput saveFile = new fileOutput();
                        saveFile.saveString(msg);
                        ;
                    }
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
                    String msg = "";
                    for (int i = 0; i < cramerResult.getRow(); i++) {
                        msg += ("x_" + (i + 1) + " = " + cramerResult.getElmt(i, 0));
                        if (i != cramerResult.getRow() - 1) {
                            msg += "\n";
                        }
                    }
                    // Print result
                    System.out.println("======================  RESULT  =======================");
                    System.out.println(msg);
                    System.out.println("=======================================================");
                    System.out.println("Apakah anda ingin menyimpan hasilnya? (y/n)");
                    scanner.nextLine();
                    String save3 = scanner.nextLine();
                    if (save3.equals("y")) {
                        fileOutput saveFile = new fileOutput();
                        saveFile.saveString(msg);
                        ;
                    }
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
        // Update: Harus bisa handle kasus jumlah persamaan lebih banyak dari pada
        // jumlah variabel
        // Jika jumlah persamaan lebih banyak dari pada jumlah variabel, harus dilakukan
        // pengecekan bahwa (jmlh pers - jmlh var) terakhir harus bernilai 0000000. Jika
        // tidak, maka
        boolean noSolution = false;
        int countAllZeroAugARow = augA.getRow();
        for (int i = 0; i < aug.getRow(); i++) {
            boolean allZero = true;
            // Cek Aug A apakah 0 semua pada rownya
            for (int j = 0; j < augA.getCol(); j++) {
                if (augA.getElmt(i, j) != 0.0) {
                    allZero = false;
                    break;
                }
            }

            // Jika semua 0 pada augA, harus dilakukan pengecekan pada augB.
            // Jika augB 0, maka lanjut (aman).
            // Jika augB tidak sama dengan 0, maka tidak akan ada solusi.
            if (allZero) {
                countAllZeroAugARow -= 1;
                if (augB.getElmt(i, 0) != 0) {
                    noSolution = true;
                    // Jangan break, sekalian menghitung countAllZeroAugRow.
                }
            }
        }
        if (noSolution) {
            return "SPL ini tidak memiliki solusi sama sekali.";
        }

        // SOLUSI UNIK
        // Handle solusi unik = Square and All diagonal one
        // Cek apakah diagonal augmentedA bernilai 1 semua (leading ones)
        // Dapatkan matriks tanpa baris 0000 aug A
        Matrix augAWithout0Rows = new Matrix(countAllZeroAugARow, augA.getCol());
        for (int i = 0; i < augAWithout0Rows.getRow(); i++) {
            for (int j = 0; j < augAWithout0Rows.getCol(); j++) {
                double value = augA.getElmt(i, j);
                augAWithout0Rows.setElmt(i, j, value);
            }
        }

        // Jika identitas, memiloiki solusi
        if (augAWithout0Rows.isSquare() && augAWithout0Rows.isIdentity()) {
            String msg = "";
            for (int i = 0; i < augAWithout0Rows.getRow(); i++) {
                msg += String.format("x_%d = %.4f", i + 1, augB.getElmt(i, 0));

                // If not laast, add new line
                if (i != augAWithout0Rows.getRow() - 1) {
                    msg += "\n";
                }
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
                msg += String.format("%.4f", augBElmt);
                isFirst = false;
            }
            // Ada bilangan bukan 0 setelah leading one (ada parametrik), jangan cetak augB
            // jika nilainya 0.
            if (!allZeroAfterLeadingOne && augBElmt != 0) {
                msg += String.format("%.4f", augBElmt);
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
                            msg += String.format("%.4f", -1 * elmt);
                        } else {
                            msg += String.format("%.4f", -1 * elmt);
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
                            msg += String.format(" - %.4f", elmt);
                        } else {
                            msg += String.format(" + %.4f", -1 * elmt);
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
        // Update: Harus bisa handle kasus jumlah persamaan lebih banyak dari pada
        // jumlah variabel
        // Jika jumlah persamaan lebih banyak dari pada jumlah variabel, harus dilakukan
        // pengecekan bahwa (jmlh pers - jmlh var) terakhir harus bernilai 0000000. Jika
        // tidak, maka
        boolean noSolution = false;
        int countAllZeroAugARow = augA.getRow();
        for (int i = 0; i < aug.getRow(); i++) {
            boolean allZero = true;
            // Cek Aug A apakah 0 semua pada rownya
            for (int j = 0; j < augA.getCol(); j++) {
                if (augA.getElmt(i, j) != 0) {
                    allZero = false;
                    break;
                }
            }

            // Jika semua 0 pada augA, harus dilakukan pengecekan pada augB.
            // Jika augB 0, maka lanjut (aman).
            // Jika augB tidak sama dengan 0, maka tidak akan ada solusi.
            if (allZero) {
                countAllZeroAugARow -= 1;
                if (augB.getElmt(i, 0) != 0) {
                    noSolution = true;
                    // Jangan break, sekalian menghitung countAllZeroAugRow.
                }
            }
        }
        if (noSolution) {
            return "SPL ini tidak memiliki solusi sama sekali.";
        }

        // SOLUSI UNIK
        // Handle solusi unik = Square and All diagonal one
        // Cek apakah diagonal augmentedA bernilai 1 semua (leading ones)
        // Dapatkan matriks tanpa baris 0000 aug A
        Matrix augAWithout0Rows = new Matrix(countAllZeroAugARow, augA.getCol());
        for (int i = 0; i < augAWithout0Rows.getRow(); i++) {
            for (int j = 0; j < augAWithout0Rows.getCol(); j++) {
                double value = augA.getElmt(i, j);
                augAWithout0Rows.setElmt(i, j, value);
            }
        }

        // Jika identitas, memiloiki solusi
        if (augAWithout0Rows.isSquare() && augAWithout0Rows.isIdentity()) {
            String msg = "";
            for (int i = 0; i < augAWithout0Rows.getRow(); i++) {
                msg += String.format("x_%d = %.4f", i + 1, augB.getElmt(i, 0));

                // If not laast, add new line
                if (i != augAWithout0Rows.getRow() - 1) {
                    msg += "\n";
                }
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
                msg += String.format("%.4f", augBElmt);
                isFirst = false;
            }
            // Ada bilangan bukan 0 setelah leading one (ada parametrik), jangan cetak augB
            // jika nilainya 0.
            if (!allZeroAfterLeadingOne && augBElmt != 0) {
                msg += String.format("%.4f", augBElmt);
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
                            msg += String.format("%.4f", -1 * elmt);
                        } else {
                            msg += String.format("%.4f", -1 * elmt);
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
                            msg += String.format(" - %.4f", elmt);
                        } else {
                            msg += String.format(" + %.4f", -1 * elmt);
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
