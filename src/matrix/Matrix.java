package matrix;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

public class Matrix {
    // Row, Col, and Matrix
    int nRow, nCol;
    double[][] matrix;

    // Constructor
    // Inisialisasi kosong. Matriks baru dibentuk dimensinya saat readMatrix.
    public Matrix() {
        nRow = 0;
        nCol = 0;
    }

    // Read input matrix from keyboard
    public void readMatrixKeyboard() {
        Scanner keyboardReader = new Scanner(System.in);

        // Input row matriks
        System.out.println("Masukkan jumlah baris matriks");
        nRow = keyboardReader.nextInt();
        while (nRow <= 0) {
            System.out.println("Jumlah baris dalam matriks harus > 0");
            nRow = keyboardReader.nextInt();
        }

        // Input col matriks
        System.out.println("Masukkan jumlah kolom matriks");
        nCol = keyboardReader.nextInt();
        while (nCol <= 0) {
            System.out.println("Jumlah kolom dalam matriks harus > 0");
            nCol = keyboardReader.nextInt();
        }

        // Inisialisasi matriks
        matrix = new double[nRow][nCol];

        // Input nilai matriks
        System.out.println("Masukkan nilai matriks (pisahkan kolom dengan spasi dan baris dengan enter/newline)");
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                matrix[i][j] = keyboardReader.nextDouble();
            }
        }

        // Close reader
        keyboardReader.close();
    }

    // Read input matrix from file
    public void readMatrixFile() {
        String tempRow, fileName;

        // Input nama file pada directory test/input
        Scanner keyboardReader = new Scanner(System.in);
        System.out.println("Masukkan nama file pada directory test/input lengkap dengan extensionnya:");
        fileName = keyboardReader.nextLine();
        keyboardReader.close();

        try {
            // Read file
            File fileObj = new File("test/input/" + fileName);
            Scanner fileReaderRowCol = new Scanner(fileObj);

            // Get row and column count
            nRow = 0;
            nCol = 0;
            while (fileReaderRowCol.hasNextLine()) {
                tempRow = fileReaderRowCol.nextLine();
                // Count column
                if (nCol == 0) {
                    nCol = tempRow.split(" ").length;
                }

                // Count row
                nRow += 1;
            }
            fileReaderRowCol.close();

            // Initialize matrix
            matrix = new double[nRow][nCol];

            // Get matrix data
            Scanner fileReaderMatrix = new Scanner(fileObj);
            for (int i = 0; i < nRow; i++) {
                for (int j = 0; j < nCol; j++) {
                    matrix[i][j] = fileReaderMatrix.nextDouble();
                }
            }
            fileReaderMatrix.close();

        } catch (FileNotFoundException e) {
            System.out.println("Terjadi sebuah kesalahan dalam membaca file.");
            e.printStackTrace();
        }

    }

    // Print matrix
    public void printMatrix() {
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                // Each element
                System.out.print(matrix[i][j]);

                // Space
                if (j != nCol - 1) {
                    System.out.print(" ");
                }
            }
            // New line
            System.out.println("");
        }
    }

    // Get number of rows
    public int getRow() {
        return nRow;
    }

    // Get number of columns
    public int getCol() {
        return nCol;
    }

    // Get element
    public double getElmt(int row, int col) {
        return matrix[row][col];
    }

    // Check if matrix is square
    public boolean isSquare() {
        return nRow == nCol;
    }
}
