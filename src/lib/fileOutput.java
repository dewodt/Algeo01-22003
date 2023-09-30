package lib;

import java.io.FileWriter;
import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import matrix.Matrix;

public class fileOutput {
    private Scanner input = new Scanner(System.in);

    public void saveMatrix(Matrix matrix) {
        // Buat save matrix ke dalam file.txt di folder test/input
        try {
            System.out.println("Masukkan nama file yg akan disimpan lengkap dengan .txt (extensionnya): ");
            String fileName = input.nextLine();
            File file = new File("test/output/" + fileName);
            if (!file.exists()) {
                // Jika file belum ada, buat dulu. Kalau udah ada, maka akan di overwrite
                file.createNewFile();
            }

            // Kemudian tulis matriks ke dalam file
            FileWriter writer = new FileWriter(file);
            int i, j;
            for (i = 0; i < matrix.getCol() - 1; i++) {
                for (j = 0; j < matrix.getRow() - 1; j++) {
                    writer.write(matrix.getElmt(i, j) + " ");
                }
                if (j == matrix.getRow() - 1) {
                    writer.write(matrix.getElmt(i, j) + "\n");
                }
            }

            // Jika baris terakhir, ga usah newline
            for (j = 0; j < matrix.getRow() - 1; j++) {
                writer.write(matrix.getElmt(i, j) + " ");
            }

            // Jika berhasil, akan muncul pesan ini
            System.out.println("Berhasil menyimpan file");
            writer.close();
        } catch (IOException e) {
            // Jika error, maka ini akan dieksekusi
            System.out.println("Telah terjadi error, file gagal disimpan. ");
            e.printStackTrace();
        }
    }

    public void saveDouble(double val) {
        // Buat save double ke dalam file.txt di folder test/input
        try {
            System.out.println("Masukkan nama file yg akan disimpan lengkap dengan .txt (extensionnya): ");
            String fileName = input.nextLine();
            File file = new File("test/output/" + fileName);
            if (!file.exists()) {
                // Jika file belum ada, buat dulu. Kalau udah ada, maka akan di overwrite
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);
            // Convert dulu double ke string baru tulis ke file agar tidak error
            writer.write(Double.toString(val));
            // Jika berhasil, akan muncul pesan ini
            System.out.println("Berhasil menyimpan file");
            writer.close();
        } catch (IOException e) {
            // Jika error, maka ini akan dieksekusi
            System.out.println("Telah terjadi error, file gagal disimpan. ");
            e.printStackTrace();
        }
    }

    public void saveString(String x) {
        try {
            System.out.println("Masukkan nama file yg akan disimpan lengkap dengan .txt (extensionnya): ");
            String fileName = input.nextLine();
            File file = new File("test/output/" + fileName);
            if (!file.exists()) {
                // Jika file belum ada, buat dulu. Kalau udah ada, maka akan di overwrite
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);
            // Tulis string ke file
            writer.write(x);
            // Jika berhasil, akan muncul pesan ini
            System.out.println("Berhasil menyimpan file");
            writer.close();
        } catch (IOException e) {
            // Jika error, maka ini akan dieksekusi
            System.out.println("Telah terjadi error, file gagal disimpan. ");
            e.printStackTrace();
        }
    }
}
