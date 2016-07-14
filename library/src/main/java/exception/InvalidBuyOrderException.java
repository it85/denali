package exception;

public class InvalidBuyOrderException extends Exception{

    private static final long serialVersionUID = 7558019994468955312L;

    private String message = null;

    public InvalidBuyOrderException() {
        super();
    }

    public InvalidBuyOrderException(String message) {
        super(message);
        this.message = message;
    }

    public InvalidBuyOrderException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
