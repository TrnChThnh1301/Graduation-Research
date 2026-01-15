package exceptions.interbank;

@SuppressWarnings("serial")
public class InterbankException extends RuntimeException {
	public InterbankException(String message) {
		super(message);
	}
}
