package exceptions.interbank;

@SuppressWarnings("serial")
public class InvalidCardException extends InterbankException {
	public InvalidCardException(String string) {
		super(string);
	}
}
