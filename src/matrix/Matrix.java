package matrix;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

public class Matrix {
    // Atribut Row, Col, and Matrix
    int nRow, nCol;
    double[][] matrix;

    // Konstruktor
    // Inisialisasi kosong. Matriks baru dibentuk dimensinya saat readMatrix.
    public Matrix() {
        nRow = 0;
        nCol = 0;
    }

    // Baca input matriks melalui keyboard
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

    // Baca input matriks melalui file di directory test/input/[namafile]
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

    // Cetak matriks
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

    // Selektor baris
    public int getRow() {
        return nRow;
    }

    // Selektor kolom
    public int getCol() {
        return nCol;
    }

    // Selektor get elemen ke row & col
    public double getElmt(int row, int col) {
        return matrix[row][col];
    }

    // Selektor set element
    public void setElmt(int row, int col, double value) {
        matrix[row][col] = value;
    }

    // Reset matrix ubah ukuran dan reset isinya
    public void initialize(int row, int col) {
        nRow = row;
        nCol = col;
        matrix = new double[row][col];
    }

    // Copy matrix value to current object
    public void copy(Matrix mIn) {
        nRow = mIn.nRow;
        nCol = mIn.nCol;
        matrix = mIn.matrix;
    }

    // Return matrix tranpose
    public void transformToTranpose() {
        int tempNRow;
        double[][] tempMatrix = new double[nCol][nRow];

        // Fill temp matrix
        for (int i = 0; i < getRow(); i++) {
            for (int j = 0; j < getCol(); j++) {
                tempMatrix[j][i] = matrix[i][j];
            }
        }

        // Swap col & row
        tempNRow = nRow;
        nRow = nCol;
        nCol = tempNRow;

        // Update matrix
        matrix = tempMatrix;
    }

    // Transform to adjoin
    public void transformToAdjoin() {

    }

    // Cek apakah ukuran matriks objek ini sama dengan ukuran matriks objek input
    public boolean isSizeEqual(Matrix mIn) {
        return getRow() == mIn.getRow() && getCol() == mIn.getCol();
    }

    // Cek apakah matriks persegi
    public boolean isSquare() {
        return nRow == nCol;
    }

    // Cek apakah matriks kosong
    public boolean isEmpty() {
        return nRow == 0 && nCol == 0;
    }

    // Cek apakah matriks identitas
    public boolean isIdentity() {
        // Cek matriks persegi atau bukan
        if (!isSquare()) {
            System.out.println("Matriks identitas harus berbentuk persegi");
            return false;
        }

        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                double elmt = getElmt(i, j);
                if ((i == j && elmt != 1) || (i != j && elmt != 0)) {
                    return false;
                }
            }
        }

        return true;
    }

    // Tukar baris row1 dengan row2
    public void swapRow(int row1, int row2) {
        // Temporary array
        double[] temp = matrix[row1];

        // Swap
        matrix[row1] = matrix[row2];
        matrix[row2] = temp;
    }

    // Kali sebuah baris row dengan k
    public void multiplyRowConstant(int row, double k) {
        for (int j = 0; j < getCol(); j++) {
            matrix[row][j] *= k;
        }
    }

    // Kali matriks dengan k
    public void multiplyConstant(double k) {
        for (int i = 0; i < getRow(); i++) {
            multiplyRowConstant(i, k);
        }
    }

    // Tambahkan baris 1 dengan k * baris 2
    public void addRow1WithKRow2(int row1, int row2, double k) {
        for (int j = 0; j < getCol(); j++) {
            matrix[row1][j] += k * matrix[row2][j];
        }
    }

    // Transformasi matriks ini ke bentuk identitas
    public void transformToIdentity() {
        // Handle kasus matriks bukan kotak
        if (!isSquare()) {
            System.out.println("Matriks identitas hanya untuk matriks persegi.");
            return;
        }

        // Jika persegi, transformasi
        for (int i = 0; i < getRow(); i++) {
            for (int j = 0; j < getCol(); j++) {
                if (i == j) {
                    setElmt(i, j, 1.0);
                } else {
                    setElmt(i, j, 0);
                }
            }
        }
    }

    // Transformasi ke bentuk echelon form
    public void transformToEchelonForm() {
        // Loop setiap baris
        for (int i = 0; i < getRow(); i++) {
            // Cari nonZeroIndex baris lainnya pada baris bawahnya yang paling kecil dan
            // Simpan index row pada rowIndex
            int rowIndex = i, nonZeroMinimumIndex = 999999999;
            for (int j = i; j < getRow(); j++) {
                // Hitung kemunculan 0 pertama2 pada suatu baris
                int countZero = 0;
                while (getElmt(j, countZero) == 0 && countZero < getCol() - 1) {
                    countZero += 1;
                }

                if (j == i) {
                    // Iterasi pertama, inisialisasi nonZeroMMinimumIndex
                    rowIndex = j;
                    nonZeroMinimumIndex = countZero;
                } else if (countZero < nonZeroMinimumIndex) {
                    // Iterasi selanjutnya, cari nilai minimum
                    rowIndex = j;
                    nonZeroMinimumIndex = countZero;
                }
            }

            // Swap jika indeks row yang sedang ditinjau bukan nol terkecil.
            if (i != rowIndex) {
                swapRow(i, rowIndex);
            }

            // Normalisasi dengan elemen bukan 0 pertama pada baris ini.
            double divider = getElmt(i, nonZeroMinimumIndex);
            if (divider != 0) {
                // Kasus bukan 0 0 0 0 0 0 0
                multiplyRowConstant(i, 1 / divider);
            }

            // Loop kolom indeks nonZeroMinimumIndeks untuk meng-0-kan bagian bawah yang
            // sedang ditinjau
            for (int j = i + 1; j < getRow(); j++) {
                addRow1WithKRow2(j, i, -1 * getElmt(j, nonZeroMinimumIndex));
            }
        }
    }

    // Ubah ke bentuk echelon form
    public void transformToReducedEchelonForm() {
        // Loop setiap baris
        for (int i = 0; i < getRow(); i++) {
            // Cari nonZeroIndex baris lainnya pada baris bawahnya yang paling kecil dan
            // Simpan index row pada rowIndex
            int rowIndex = i, nonZeroMinimumIndex = 999999999;
            for (int j = i; j < getRow(); j++) {
                // Hitung kemunculan 0 pertama2 pada suatu baris
                int countZero = 0;
                while (getElmt(j, countZero) == 0 && countZero < getCol() - 1) {
                    countZero += 1;
                }

                if (j == i) {
                    // Iterasi pertama, inisialisasi nonZeroMMinimumIndex
                    rowIndex = j;
                    nonZeroMinimumIndex = countZero;
                } else if (countZero < nonZeroMinimumIndex) {
                    // Iterasi selanjutnya, cari nilai minimum
                    rowIndex = j;
                    nonZeroMinimumIndex = countZero;
                }
            }

            // Swap jika indeks row yang sedang ditinjau bukan nol terkecil.
            if (i != rowIndex) {
                swapRow(i, rowIndex);
            }

            // Normalisasi dengan elemen bukan 0 pertama pada baris ini.
            double divider = getElmt(i, nonZeroMinimumIndex);
            if (divider != 0) {
                // Kasus bukan 0 0 0 0 0 0 0
                multiplyRowConstant(i, 1 / divider);
            }

            // Loop kolom indeks nonZeroMinimumIndeks untuk meng-0-kan bagian bawah yang
            // sedang ditinjau
            for (int j = 0; j < getRow(); j++) {
                if (i != j) {
                    addRow1WithKRow2(j, i, -1 * getElmt(j, nonZeroMinimumIndex));
                }
            }
        }
    }

    // Determinan Reduksi Baris (operasi baris elementer)
    public double determinantByERO() {
        double det = 1.0;
        Matrix temp = new Matrix();
        temp.copy(this);

        // Ubah matriks temp ke bentuk row eselon.
        // Loop setiap baris
        for (int i = 0; i < temp.getRow(); i++) {
            // Cari nonZeroIndex baris lainnya pada baris bawahnya yang paling kecil dan
            // Simpan index row pada rowIndex
            int rowIndex = i, nonZeroMinimumIndex = 999999999;
            for (int j = i; j < temp.getRow(); j++) {
                // Hitung kemunculan 0 pertama2 pada suatu baris
                int countZero = 0;
                while (temp.getElmt(j, countZero) == 0 && countZero < temp.getCol() - 1) {
                    countZero += 1;
                }

                if (j == i) {
                    // Iterasi pertama, inisialisasi nonZeroMMinimumIndex
                    rowIndex = j;
                    nonZeroMinimumIndex = countZero;
                } else if (countZero < nonZeroMinimumIndex) {
                    // Iterasi selanjutnya, cari nilai minimum
                    rowIndex = j;
                    nonZeroMinimumIndex = countZero;
                }
            }

            // Swap jika indeks row yang sedang ditinjau bukan nol terkecil.
            if (i != rowIndex) {
                temp.swapRow(i, rowIndex);
                det *= -1.0;
            }

            // Normalisasi dengan elemen bukan 0 pertama pada baris ini.
            double divider = temp.getElmt(i, nonZeroMinimumIndex);
            if (divider != 0) {
                // Kasus bukan 0 0 0 0 0 0 0
                temp.multiplyRowConstant(i, 1 / divider);
                det *= divider;
            }

            // Loop kolom indeks nonZeroMinimumIndeks untuk meng-0-kan bagian bawah yang
            // sedang ditinjau
            for (int j = i + 1; j < temp.getRow(); j++) {
                temp.addRow1WithKRow2(j, i, -1 * temp.getElmt(j, nonZeroMinimumIndex));
            }
        }

        return det;
    }

    // Inverse matriks eselon baris
    public void inverseByERO() {
        // Ide:
        // 1. Gabungkan matriks menajdi berukuran n x 2n
        // 2. Remudian reduksi eselon form
        // 3. Pisah matriks menjadi 2. Cek apakah yang kiri identitas atau bukan

        Matrix identity = new Matrix();
        Matrix combined = new Matrix();

        // Bikin matriks identitaas ukuran yang sama dengan matriks sekarang
        identity.initialize(nRow, nCol);
        identity.transformToIdentity();

        // Bikin matriks kombinasi
        combined.initialize(nRow, 2 * nCol);
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < 2 * nCol; j++) {
                if (j < nCol) {
                    // Matriks kiri merupakan matriks yang mau dicari inversenya
                    double elmt = getElmt(i, j);
                    combined.setElmt(i, j, elmt);
                } else {
                    // Matriks kanan merupakan matriks identitas
                    double elmt = identity.getElmt(i, j - nCol);
                    combined.setElmt(i, j, elmt);
                }
            }
        }

        // Reduksi matriks
        combined.transformToReducedEchelonForm();

        // Pisahkan kedua matriks
        Matrix augmentedLeft = new Matrix();
        Matrix augmentedRight = new Matrix();
        augmentedLeft.initialize(nRow, nCol);
        augmentedRight.initialize(nRow, nCol);

        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                // Matriks augmented bagian kiri
                double leftElmt = combined.getElmt(i, j);
                augmentedLeft.setElmt(i, j, leftElmt);

                // Matriks augmented bagian kanan
                double rightElmt = combined.getElmt(i, j + nCol);
                augmentedRight.setElmt(i, j, rightElmt);
            }
        }

        // Cek apakah matriks augmented bagian kiri matriks identitas
        if (!augmentedLeft.isIdentity()) {
            System.out.println(
                    "Matriks tidak memiliki inverse karena augmented bagian kiri tidak dapat mencapai matriks identitas");
            return;
        }

        // Jika memiliki matriks
        copy(augmentedRight);
    }

}
