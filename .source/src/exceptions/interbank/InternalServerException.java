package exceptions.interbank;

@SuppressWarnings("serial")
public class InternalServerException extends InterbankException {
	public InternalServerException(String message) {
		super(message);
	}
}
