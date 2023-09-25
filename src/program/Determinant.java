package program;

import matrix.*;
import java.util.Scanner;
import errors.Errors;

public class Determinant {
    public static void app() {
        // Tampilan menu metode perhitungan
        System.out.println("=============  Pilih Metode Perhitungan:  =============");
        System.out.println("1. Operasi Baris Elementer");
        System.out.println("2. Ekspansi Kofaktor");

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
            System.out.println("======================  ERROR  ========================");
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
                // Operasi baris elemeenter
                try {
                    // Calculate
                    double det = matrix.getDeterminantByERO();
                    String msg = String.format("Determinan matriks: %.2f", det);

                    // Print result
                    System.out.println("======================  RESULT  =======================");
                    System.out.println(msg);
                    System.out.println("=======================================================");

                } catch (Errors.InvalidMatrixSizeException e) {
                    System.out.println("======================  ERROR  ========================");
                    e.printStackTrace();
                    System.out.println("=======================================================");
                }
                break;
            case 2:
                // Ekspansi kofaktor
                try {
                    // Calculate
                    double det = matrix.getDeterminantByCofac();
                    String msg = String.format("Determinan matriks: %.2f", det);

                    // Print result
                    System.out.println("======================  RESULT  =======================");
                    System.out.println(msg);
                    System.out.println("=======================================================");

                } catch (Errors.InvalidMatrixSizeException e) {
                    System.out.println("=======================  ERROR  =======================");
                    e.printStackTrace();
                    System.out.println("=======================================================");
                }
                break;
        }

        // Close reader
        reader.close();
    }
}
