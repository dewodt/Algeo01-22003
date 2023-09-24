package matrix;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import errors.Errors;

public class Matrix {
    // Atribut Row, Col, and Matrix
    int nRow, nCol;
    double[][] matrix;

    // Konstruktor
    // Inisialisasi kosong, diisi baru saat readMatrix.
    public Matrix() {
        this.nRow = 0;
        this.nCol = 0;
        this.matrix = new double[0][0];
    }

    // Inisialisasi matriks dengan row dan col langsung.
    public Matrix(int row, int col) {
        this.nRow = row;
        this.nCol = col;
        this.matrix = new double[row][col];
    }

    // Baca input matriks melalui keyboard
    public void readMatrixKeyboard() {
        Scanner keyboardReader = new Scanner(System.in);

        // Input row matriks
        System.out.println("Masukkan jumlah baris matriks");
        this.nRow = keyboardReader.nextInt();
        while (this.nRow <= 0) {
            System.out.println("Jumlah baris dalam matriks harus > 0");
            this.nRow = keyboardReader.nextInt();
        }

        // Input col matriks
        System.out.println("Masukkan jumlah kolom matriks");
        this.nCol = keyboardReader.nextInt();
        while (this.nCol <= 0) {
            System.out.println("Jumlah kolom dalam matriks harus > 0");
            this.nCol = keyboardReader.nextInt();
        }

        // Inisialisasi matriks
        this.matrix = new double[this.nRow][this.nCol];

        // Input nilai matriks
        System.out.println("Masukkan nilai matriks (pisahkan kolom dengan spasi dan baris dengan enter/newline)");
        for (int i = 0; i < this.nRow; i++) {
            for (int j = 0; j < this.nCol; j++) {
                this.matrix[i][j] = keyboardReader.nextDouble();
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
            this.nRow = 0;
            this.nCol = 0;
            while (fileReaderRowCol.hasNextLine()) {
                tempRow = fileReaderRowCol.nextLine();
                // Count column
                if (this.nCol == 0) {
                    this.nCol = tempRow.split(" ").length;
                }

                // Count row
                this.nRow += 1;
            }
            fileReaderRowCol.close();

            // Initialize matrix
            this.matrix = new double[this.nRow][this.nCol];

            // Get matrix data
            Scanner fileReaderMatrix = new Scanner(fileObj);
            for (int i = 0; i < this.nRow; i++) {
                for (int j = 0; j < this.nCol; j++) {
                    this.matrix[i][j] = fileReaderMatrix.nextDouble();
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
        for (int i = 0; i < this.nRow; i++) {
            for (int j = 0; j < this.nCol; j++) {
                // Each element
                System.out.print(this.matrix[i][j]);

                // Space
                if (j != this.nCol - 1) {
                    System.out.print(" ");
                }
            }
            // New line
            System.out.println("");
        }
    }

    // Selektor baris
    public int getRow() {
        return this.nRow;
    }

    // Selektor kolom
    public int getCol() {
        return this.nCol;
    }

    // Selektor get elemen ke row & col
    public double getElmt(int row, int col) {
        return this.matrix[row][col];
    }

    // Selektor set element
    public void setElmt(int row, int col, double value) {
        this.matrix[row][col] = value;
    }

    // Copy matrix value to current object
    public void copy(Matrix mIn) {
        this.nRow = mIn.nRow;
        this.nCol = mIn.nCol;
        this.matrix = new double[mIn.nRow][mIn.nCol];
        for (int i = 0; i < mIn.getRow(); i++) {
            for (int j = 0; j < mIn.getCol(); j++) {
                this.matrix[i][j] = mIn.matrix[i][j];
            }
        }
    }

    // Return matrix tranpose
    public Matrix transpose() {
        Matrix result = new Matrix(this.getCol(), this.getRow());

        // Fill temp matrix
        for (int i = 0; i < this.getRow(); i++) {
            for (int j = 0; j < this.getCol(); j++) {
                result.setElmt(j, i, this.matrix[i][j]);
            }
        }

        return result;
    }

    // Transformasi matriks ini ke bentuk identitas
    public static Matrix createIdentity(int n) {
        // Inisialisasi
        Matrix result = new Matrix(n, n);

        // Jika persegi, transformasi
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    result.setElmt(i, j, 1.0);
                } else {
                    result.setElmt(i, j, 0);
                }
            }
        }

        return result;
    }

    // Return matrix addition of m1 + m2
    public static Matrix addMatrix(Matrix m1, Matrix m2) throws Errors.InvalidMatrixSizeException {
        // Throw error when size not equal
        if (!m1.isSizeEqual(m2)) {
            throw new Errors.InvalidMatrixSizeException("Ukuran matriks untuk operasi pertambahan harus sama.");
        }

        // If size is equal
        Matrix result = new Matrix(m1.getRow(), m1.getCol());
        for (int i = 0; i < m1.getRow(); i++) {
            for (int j = 0; j < m1.getCol(); j++) {
                double newValue = m1.getElmt(i, j) + m2.getElmt(i, j);
                result.setElmt(i, j, newValue);
            }
        }

        return result;
    }

    // Return matrix substraction of m1 - m2
    public static Matrix substractMatrix(Matrix m1, Matrix m2) throws Errors.InvalidMatrixSizeException {
        // Throw error when size not equal
        if (!m1.isSizeEqual(m2)) {
            throw new Errors.InvalidMatrixSizeException("Ukuran matriks untuk operasi pertambahan harus sama.");
        }

        // If size is equal
        Matrix result = new Matrix(m1.getRow(), m1.getCol());
        for (int i = 0; i < m1.getRow(); i++) {
            for (int j = 0; j < m1.getCol(); j++) {
                double newValue = m1.getElmt(i, j) - m2.getElmt(i, j);
                result.setElmt(i, j, newValue);
            }
        }

        return result;
    }

    // Return matrix multiplication
    public static Matrix multiplyMatrix(Matrix m1, Matrix m2) throws Errors.InvalidMatrixSizeException {
        // Throw error when not multiplicable
        if (m1.getCol() != m2.getRow()) {
            throw new Errors.InvalidMatrixSizeException(
                    "Ukuran kolom matriks 1 harus sama dengan ukuran kolom matriks 2");
        }

        // If multiplable
        Matrix result = new Matrix(m1.getRow(), m2.getCol());
        for (int i = 0; i < m1.getRow(); i++) {
            for (int j = 0; j < m2.getCol(); j++) {
                double sum = 0.0;
                for (int k = 0; k < m1.getCol(); k++) {
                    sum += m1.getElmt(i, k) * m2.getElmt(k, j);
                }
                result.setElmt(i, j, sum);
            }
        }

        return result;
    }

    // Cek apakah ukuran matriks objek ini sama dengan ukuran matriks objek input
    public boolean isSizeEqual(Matrix mIn) {
        return this.getRow() == mIn.getRow() && this.getCol() == mIn.getCol();
    }

    // Cek apakah matriks persegi
    public boolean isSquare() {
        return this.getRow() == this.getCol();
    }

    // Cek apakah matriks kosong
    public boolean isEmpty() {
        return this.getRow() == 0 && this.getCol() == 0;
    }

    // Cek apakah matriks identitas
    public boolean isIdentity() {
        // Cek matriks persegi atau bukan
        if (!isSquare()) {
            System.out.println("Matriks identitas harus berbentuk persegi");
            return false;
        }

        for (int i = 0; i < this.getRow(); i++) {
            for (int j = 0; j < this.getCol(); j++) {
                double elmt = this.getElmt(i, j);
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
        double[] temp = this.matrix[row1];

        // Swap
        this.matrix[row1] = this.matrix[row2];
        this.matrix[row2] = temp;
    }

    // Kali sebuah baris row dengan k
    public void multiplyRowConstant(int row, double k) {
        for (int j = 0; j < this.getCol(); j++) {
            this.matrix[row][j] *= k;
        }
    }

    // Kali matriks dengan k
    public void multiplyConstant(double k) {
        for (int i = 0; i < this.getRow(); i++) {
            multiplyRowConstant(i, k);
        }
    }

    // Tambahkan baris 1 dengan k * baris 2
    public void addRow1WithKRow2(int row1, int row2, double k) {
        for (int j = 0; j < this.getCol(); j++) {
            this.matrix[row1][j] += k * this.matrix[row2][j];
        }
    }

    // Transformasi ke bentuk echelon form
    public void transformToEchelonForm() {
        // Loop setiap baris
        for (int i = 0; i < this.getRow(); i++) {
            // Cari nonZeroIndex baris lainnya pada baris bawahnya yang paling kecil dan
            // Simpan index row pada rowIndex
            int rowIndex = i, nonZeroMinimumIndex = 999999999;
            for (int j = i; j < this.getRow(); j++) {
                // Hitung kemunculan 0 pertama2 pada suatu baris
                int countZero = 0;
                while (this.getElmt(j, countZero) == 0) {
                    countZero += 1;
                    if (countZero == this.getCol()) {
                        break;
                    }
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
                this.swapRow(i, rowIndex);
            }

            // Kasus Baris tidak 0 semua
            if (nonZeroMinimumIndex < this.getCol()) {
                // Dapatkan elemen pembagi
                double divider = this.getElmt(i, nonZeroMinimumIndex);

                // Loop kolom indeks nonZeroMinimumIndeks untuk meng-0-kan bagian bawah yang
                // sedang ditinjau
                for (int j = i + 1; j < this.getRow(); j++) {
                    double k = -1 * this.getElmt(j, nonZeroMinimumIndex) / divider;
                    this.addRow1WithKRow2(j, i, k);
                }

                // Normalisasi baris ke i agar menjadi leading one
                this.multiplyRowConstant(i, 1 / divider);
            }
        }
    }

    // Ubah ke bentuk echelon form
    public void transformToReducedEchelonForm() {
        // Loop setiap baris
        for (int i = 0; i < this.getRow(); i++) {
            // Cari nonZeroIndex baris lainnya pada baris bawahnya yang paling kecil dan
            // Simpan index row pada rowIndex
            int rowIndex = i, nonZeroMinimumIndex = 999999999;
            for (int j = i; j < this.getRow(); j++) {
                // Hitung kemunculan 0 pertama2 pada suatu baris
                int countZero = 0;
                while (this.getElmt(j, countZero) == 0) {
                    countZero += 1;
                    if (countZero == this.getCol()) {
                        break;
                    }
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
                this.swapRow(i, rowIndex);
            }

            // Kasus Baris tidak 0 semua
            if (nonZeroMinimumIndex < this.getCol()) {
                // Dapatkan elemen pembagi
                double divider = this.getElmt(i, nonZeroMinimumIndex);

                // Loop kolom indeks nonZeroMinimumIndeks untuk meng-0-kan bagian bawah yang
                // sedang ditinjau
                for (int j = 0; j < this.getRow(); j++) {
                    if (i != j) {
                        double k = -1 * this.getElmt(j, nonZeroMinimumIndex) / divider;
                        this.addRow1WithKRow2(j, i, k);
                    }
                }

                // Normalisasi baris ke i agar menjadi leading one
                this.multiplyRowConstant(i, 1 / divider);
            }
        }
    }

    // Determinan Reduksi Baris (operasi baris elementer)
    public double determinantByERO() throws Errors.InvalidMatrixSizeException {
        double det = 1.0;
        Matrix temp = new Matrix();
        temp.copy(this);

        // Error
        if (!temp.isSquare()) {
            throw new Errors.InvalidMatrixSizeException("Determinan matriks hanya terdefinisi untuk matriks persegi.");
        }

        // Ubah matriks temp ke bentuk row eselon.
        // Loop setiap baris
        for (int i = 0; i < temp.getRow(); i++) {
            // Cari nonZeroIndex baris lainnya pada baris bawahnya yang paling kecil dan
            // Simpan index row pada rowIndex
            int rowIndex = i, nonZeroMinimumIndex = 999999999;
            for (int j = i; j < temp.getRow(); j++) {
                // Hitung kemunculan 0 pertama pada suatu baris
                int countZero = 0;
                while (temp.getElmt(j, countZero) == 0) {
                    countZero += 1;
                    if (countZero == temp.getCol()) {
                        break;
                    }
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

            // Loop kolom indeks nonZeroMinimumIndeks untuk meng-0-kan bagian bawah yang
            // sedang ditinjau
            if (nonZeroMinimumIndex < temp.getCol()) {
                // Baris tidak semua bernilai 0

                // Dapatkan pembagi
                double diagonal = temp.getElmt(i, nonZeroMinimumIndex);
                for (int j = i + 1; j < temp.getRow(); j++) {
                    double k = -1 * temp.getElmt(j, nonZeroMinimumIndex) / diagonal;
                    temp.addRow1WithKRow2(j, i, k);
                }
                // Kali nilai diagonal
                det *= diagonal;
            } else {
                // Baris bernilai 0 semua
                det *= 0;
            }
        }

        return det;
    }

    // Inverse matriks eselon baris
    public Matrix inverseByERO() throws Errors.NoInverseMatrixException {
        // Ide:
        // 1. Gabungkan matriks menajdi berukuran n x 2n
        // 2. Remudian reduksi eselon form
        // 3. Pisah matriks menjadi 2. Cek apakah yang kiri identitas atau bukan

        // Validasi square
        if (!this.isSquare()) {
            throw new Errors.NoInverseMatrixException(
                    "Matriks ini tidak memiliki inverse karena bukan matriks persegi!");
        }

        // Inisialisasi
        Matrix identity = new Matrix();
        identity = createIdentity(this.getRow());
        Matrix combined = new Matrix(this.getRow(), 2 * this.getCol());

        for (int i = 0; i < this.getRow(); i++) {
            for (int j = 0; j < 2 * this.getCol(); j++) {
                if (j < this.getCol()) {
                    // Matriks kiri merupakan matriks yang mau dicari inversenya
                    double elmt = this.getElmt(i, j);
                    combined.setElmt(i, j, elmt);
                } else {
                    // Matriks kanan merupakan matriks identitas
                    double elmt = identity.getElmt(i, j - this.getCol());
                    combined.setElmt(i, j, elmt);
                }
            }
        }

        // Reduksi matriks
        combined.transformToReducedEchelonForm();

        // Pisahkan kedua matriks
        Matrix augmentedLeft = new Matrix(this.getRow(), this.getCol());
        Matrix augmentedRight = new Matrix(this.getRow(), this.getCol());

        for (int i = 0; i < this.getRow(); i++) {
            for (int j = 0; j < this.getCol(); j++) {
                // Matriks augmented bagian kiri
                double leftElmt = combined.getElmt(i, j);
                augmentedLeft.setElmt(i, j, leftElmt);

                // Matriks augmented bagian kanan
                double rightElmt = combined.getElmt(i, j + this.getCol());
                augmentedRight.setElmt(i, j, rightElmt);
            }
        }

        // Cek apakah matriks augmented bagian kiri matriks identitas
        if (!augmentedLeft.isIdentity()) {
            throw new Errors.NoInverseMatrixException(
                    "Matriks tidak memiliki inverse karena augmented bagian kiri tidak dapat mencapai matriks identitas");
        }

        // Jika memiliki matriks
        return augmentedRight;
    }

    //
}
