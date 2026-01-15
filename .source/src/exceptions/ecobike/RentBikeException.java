package exceptions.ecobike;

@SuppressWarnings("serial")
public class RentBikeException extends EcoBikeException {
	public enum RENT_BIKE_ERROR_CODE {
		ERROR_BIKE_BEING_RENTED,
		ERROR_BIKE_NOT_BEING_RENTED
	}

	@SuppressWarnings("unused")
	private RENT_BIKE_ERROR_CODE errCode;
	
	public RentBikeException(String string) {
		super(string);
	}

	public RentBikeException(String string, RentBikeException.RENT_BIKE_ERROR_CODE err) {
		super(string);
		this.errCode = err;
	}
}
