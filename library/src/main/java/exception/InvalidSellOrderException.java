package exception;

public class InvalidSellOrderException extends Exception{

    private static final long serialVersionUID = 1L;

    private String message = null;

    public InvalidSellOrderException() {
        super();
    }

    public InvalidSellOrderException(String message) {
        super(message);
        this.message = message;
    }

    public InvalidSellOrderException(Throwable cause) {
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
