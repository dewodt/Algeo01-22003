package errors;

public class Errors {
    static public class NoInverseMatrixException extends Exception {
        public NoInverseMatrixException(String msg) {
            super(msg);
        }
    }

    static public class DeterminanZeroException extends Exception {
        public DeterminanZeroException(String msg) {
            super(msg);
        }
    }

    static public class InvalidMatrixSizeException extends Exception {
        public InvalidMatrixSizeException(String msg) {
            super(msg);
        }
    }

    static public class SPLUnsolvable extends Exception {
        public SPLUnsolvable(String msg) {
            super(msg);
        }
    }
}
