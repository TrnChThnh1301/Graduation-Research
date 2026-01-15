package entities.strategies;

import interfaces.RentalProps;

public class RentalFactory {
	private static final int TWENTY_FOUR_HOURS = 1440;

	public static RentalProps getRentalStrategy(int time) {
		if (time < TWENTY_FOUR_HOURS) {
			return new Under24HoursRentalStrategy();
		} 
		else {
			return new Over24HoursRentalStrategy();
		}
	}
}
