package exceptions.interbank;

@SuppressWarnings("serial")
public class InterbankUndefinedException extends InterbankException {
	public InterbankUndefinedException(String message) {
		super(message);
	}
}
