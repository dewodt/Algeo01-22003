package matrix;

public class Error {
    static public class NoInverseMatrixException extends Exception {
        public NoInverseMatrixException(String msg) {
            super(msg);
        }
    }

    static public class InvalidMatrixSizeException extends Exception {
        public InvalidMatrixSizeException(String msg) {
            super(msg);
        }
    }

}
