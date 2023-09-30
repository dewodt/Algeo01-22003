package program;

import matrix.*;

import java.util.Scanner;
import errors.Errors;
import lib.fileOutput;

public class Inverse {
    public static void app() {
        // Tampilan menu metode perhitungan
        System.out.println("=============  Pilih Metode Perhitungan:  =============");
        System.out.println("1. Operasi Baris Elementer");
        System.out.println("2. Adjoin dan Determinan");

        // Input dan validasi metode perhitungan
        Scanner reader = new Scanner(System.in);
        int calcMethod = reader.nextInt();
        System.out.println("=======================================================");
        while (calcMethod < 1 || calcMethod > 2) {
            System.out.println("======================  ERROR  =======================");
            System.out.println("Pilihan harus angka bilangan bulat dari 1 sampai 2!");
            calcMethod = reader.nextInt();
            System.out.println("=======================================================");
        }

        // Tampilan menu metode input
        System.out.println("================  Pilih Metode Input:  ================");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        // Input dan validasi metode input
        int inputMethod = reader.nextInt();
        System.out.println("=======================================================");
        while (inputMethod < 1 || inputMethod > 2) {
            System.out.println("======================  ERROR  =======================");
            System.out.println("Pilihan harus angka bilangan bulat dari 1 sampai 2!");
            calcMethod = reader.nextInt();
            System.out.println("=======================================================");
        }

        // Penanganan pilihan input
        Matrix matrix = new Matrix();
        switch (inputMethod) {
            case 1:
                matrix.readMatrixKeyboard();
                break;
            case 2:
                matrix.readMatrixFile();
                break;
        }

        // Penangaan pilihan metode
        switch (calcMethod) {
            case 1:
                // Inverse matriks melalui operasi baris elementer
                try {
                    // Calculate
                    Matrix inverse = new Matrix();

                    inverse = matrix.getInverseByERO();

                    // Print result
                    System.out.println("======================  RESULT  =======================");
                    inverse.printMatrix();
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("=======================================================");
                    System.out.println("Apakah anda ingin menyimpan hasilnya? (y/n)");
                    String save = scanner.nextLine();
                    if (save.equals("y")) {
                        fileOutput saveFile = new fileOutput();
                        saveFile.saveMatrix(inverse);
                    }
                    scanner.close();
                    System.out.println("=======================================================");

                } catch (Errors.NoInverseMatrixException e) {
                    System.out.println("======================  ERROR  =======================");
                    e.printStackTrace();
                    System.out.println("=======================================================");
                }
                break;
            case 2:
                // Inverse matriks melalui adjoin
                try {
                    // Calculate
                    Matrix inverse = new Matrix();
                    inverse = matrix.getInverseByAdjoint();

                    // Print result
                    System.out.println("======================  RESULT  =======================");
                    inverse.printMatrix();
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("=======================================================");
                    System.out.println("Apakah anda ingin menyimpan hasilnya? (y/n)");
                    String save = scanner.nextLine();
                    if (save.equals("y")) {
                        fileOutput saveFile = new fileOutput();
                        saveFile.saveMatrix(inverse);
                    }
                    scanner.close();
                    System.out.println("=======================================================");

                } catch (Errors.NoInverseMatrixException e) {
                    System.out.println("======================  ERROR  =======================");
                    e.printStackTrace();
                    System.out.println("=======================================================");
                }
                break;
        }

        // Close reader
        reader.close();
    }
}
