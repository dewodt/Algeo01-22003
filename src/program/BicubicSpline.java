package program;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
import matrix.*;
import errors.Errors;
import lib.fileOutput;

public class BicubicSpline {
    public static void app() {
        // y = Xa
        // Goal: nyari a lalu masukkan ke rumsu f(x, y) = Sum a_ij x^i y^j
        Scanner reader = new Scanner(System.in);

        // Tampilan menu metode input
        System.out.println("================  Pilih Metode Input:  ================");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        // Input dan validasi metode input
        int inputMethod = reader.nextInt();
        System.out.println("=======================================================");
        while (inputMethod < 1 || inputMethod > 2) {
            System.out.println("======================  ERROR  ========================");
            System.out.println("Pilihan harus angka bilangan bulat dari 1 sampai 2!");
            inputMethod = reader.nextInt();
            System.out.println("=======================================================");
        }

        Matrix functionMatrix = new Matrix(4, 4);
        double approxX = 0.0, approxY = 0.0;

        // Input dan validasi masukan matrix
        switch (inputMethod) {
            // KEYBOARD
            case 1:
                reader.nextLine();

                System.out.println("=======================================================");
                System.out.println("Input matriks berukuran 4x4 sesuaia dengan format input bicubic spline!");
                functionMatrix.readMatrixKeyboard();
                System.out.println("=======================================================");

                // Validasi
                while (functionMatrix.getRow() != 4 || functionMatrix.getCol() != 4) {
                    System.out.println("======================  ERROR  ========================");
                    System.out.println("Ukuran input matriks bicubic spline harus bernilai 4x4!");
                    functionMatrix.readMatrixKeyboard();
                    System.out.println("=======================================================");
                }

                // Input nilai x, y untuk ditaksir
                System.out.println("=======================================================");
                System.out.println("Input nilai x yang ingin ditaksir dalam rentang [0, 1]!");
                approxX = reader.nextDouble();
                System.out.println("Input nilai y yang ingin ditaksir dalam rentang [0, 1]!");
                approxY = reader.nextDouble();
                System.out.println("=======================================================");
                while (approxX < 0 || approxX > 1 || approxY < 0 || approxX > 1) {
                    System.out.println("====================== ERROR ========================");
                    System.out.println("Nilai x dan y harus berada dalam rentang [0, 1]!");
                    approxX = reader.nextDouble();
                    approxY = reader.nextDouble();
                    System.out.println("=======================================================");
                }

                break;

            // FILE
            case 2:
                // Input nama file pada directory test/input
                System.out.println("Masukkan nama file pada directory test/input lengkap dengan extensionnya:");
                reader.nextLine();
                String fileName = reader.nextLine();

                // Tidak bisa matrix.readFile() langsung karena format file berisi matriks dan
                // juga x dan y yang ingin diapproksimasikan (digabung)
                try {
                    // Read file
                    File fileObj = new File("test/input/" + fileName);

                    // Get matrix data
                    Scanner fileReader = new Scanner(fileObj);
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            double value = fileReader.nextDouble();
                            functionMatrix.setElmt(i, j, value);
                        }
                    }

                    // Get approxX and approxY data.
                    approxX = fileReader.nextDouble();
                    approxY = fileReader.nextDouble();

                    // Close file reader
                    fileReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("Terjadi sebuah kesalahan dalam membaca file.");
                    e.printStackTrace();
                }
                break;
        }

        // Buat matriks X yang berukuran 16 x 16 (karena menggunakan 4 titik).
        // y = Xa
        Matrix XMatrix = new Matrix(16, 16);
        for (int i = 0; i < 16; i++) {
            // Variation for x and y values (0 or 1)
            double iX = i % 2 == 0 ? 0.0 : 1.0;
            double iY = i % 4 == 0 || i % 4 == 1 ? 0.0 : 1.0;
            for (int j = 0; j < 16; j++) {
                // Variation for aij index (the i and j values)
                int ia = j % 4;
                int ja = Math.floorDiv(j, 4);
                double coef;
                if (i < 4) {
                    // coef for f
                    coef = getFCoef(ia, ja, iX, iY);
                } else if (i < 8) {
                    // coef for fx
                    coef = getFxCoef(ia, ja, iX, iY);
                } else if (i < 12) {
                    // coef for fy
                    coef = getFyCoef(ia, ja, iX, iY);
                } else {
                    // coef for fxy
                    coef = getFxyCoef(ia, ja, iX, iY);
                }
                XMatrix.setElmt(i, j, coef);
            }
        }

        // Dapatkan nilai koefisien
        // Konversi matriks input 4x4 ke matriks 16x1
        Matrix yMatrix = new Matrix(16, 1);
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double value = functionMatrix.getElmt(i, j);
                yMatrix.setElmt(k, 0, value);
                k += 1;
            }
        }

        // Solve nilai koefisien a
        Matrix aMatrix = new Matrix(16, 1);
        Matrix xInverseMatrix = new Matrix();
        try {
            xInverseMatrix = XMatrix.getInverseByERO();
            aMatrix = Matrix.multiplyMatrix(xInverseMatrix, yMatrix);
        } catch (Errors.NoInverseMatrixException | Errors.InvalidMatrixSizeException e) {
            e.printStackTrace();
        }

        // Hitung nilai f(x, y)
        double approxFunc = 0.0;
        for (int i = 0; i < 16; i++) {
            int ia = i % 4;
            int ja = Math.floorDiv(i, 4);
            approxFunc += aMatrix.getElmt(i, 0) * Math.pow(approxX, ia) * Math.pow(approxY, ja);
        }
        // Jika user memutuskan untuk simpan file, maka akan muncul pilihan ini
        System.out.println(approxFunc);
        Scanner scanner = new Scanner(System.in);
        System.out.println("=======================================================");
        System.out.println("Apakah anda ingin menyimpan hasilnya? (y/n)");
        String save = scanner.nextLine();
        if (save.equals("y")) {
            fileOutput saveFile = new fileOutput();
            // Save hasilnya jika user memilih y
            saveFile.saveDouble(approxFunc);
        }
        scanner.close();
        System.out.println("=======================================================");
        reader.close();
    }

    // function to calculate f(x, y) coefficient
    static double getFCoef(int i, int j, double x, double y) {
        return Math.pow(x, i) * Math.pow(y, j);
    }

    // function to calculate fx(x, y) coefficient
    static double getFxCoef(int i, int j, double x, double y) {
        // Hindari NaN saat i = 0 (pangkat -1 => pembagian)
        if (i == 0) {
            return 0;
        }
        return i * Math.pow(x, i - 1) * Math.pow(y, j);
    }

    // function to calculate fy(x, y) coefficient
    static double getFyCoef(int i, int j, double x, double y) {
        // Hindari NaN saat j = 0 (pangkat -1 => pembagian)
        if (j == 0) {
            return 0;
        }
        return j * Math.pow(x, i) * Math.pow(y, j - 1);
    }

    // function to calculate fxy(x, y) coefficient
    static double getFxyCoef(int i, int j, double x, double y) {
        // Hindari NaN saat i = 0 atau j = 0 (pangkat -1 => pembagian)
        if (i == 0 || j == 0) {
            return 0;
        }
        return i * j * Math.pow(x, i - 1) * Math.pow(y, j - 1);
    }

}
