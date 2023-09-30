import program.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Tampilan menu
        System.out.println("====================  Pilih Menu:  ====================");
        System.out.println("1. Sistem Persamaan Linear");
        System.out.println("2. Determinan");
        System.out.println("3. Matriks Balikan");
        System.out.println("4. Interpolasi Polinom");
        System.out.println("5. Interpolasi Bicubic Spline");
        System.out.println("6. Regresi Linear Berganda");
        System.out.println("7. Keluar");

        // Input menu dan validasi
        Scanner reader = new Scanner(System.in);
        int menuInput = reader.nextInt();
        System.out.println("=======================================================");
        while (menuInput < 1 || menuInput > 7) {
            System.out.println("======================  ERROR  =======================");
            System.out.println("Pilihan harus angka bilangan bulat dari 1 sampai 7!");
            menuInput = reader.nextInt();
            System.out.println("=======================================================");
        }

        // Input sudah valid
        switch (menuInput) {
            case 1:
                // SPL
                SPL.app();
                break;
            case 2:
                // Determinan
                Determinant.app();
                break;
            case 3:
                // Inverse matriks
                Inverse.app();
                break;
            case 4:
                Interpolasi.app();
                break;
            case 5:
                // Bicubic spline
                BicubicSpline.app();
                break;
            case 6:
                // Regresi linier berganda
                LinierBerganda.app();
                break;
            case 7:
                // Keluar
                System.exit(0);
                break;

        }
        reader.close();
    }
}