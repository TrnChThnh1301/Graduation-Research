package entities.strategies;

import interfaces.DepositProps;

public class DepositFactory {
	public static DepositProps getDepositStrategy() {
		return new DepositStrategy();
	}
}
