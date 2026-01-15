package entities.strategies;

import interfaces.DepositProps;

public class DepositStrategy implements DepositProps {
	private static float factor = 0.4f;

	public DepositStrategy() {}
	
	@Override
	public float getDepositPrice(float bikeCost) {
		return bikeCost * factor;
	}
	
	public float getFactor() {
		return factor;
	}
	
}
