package utils;

public class Configs {
    public enum BikeType{
        NormalBike,
        EBike,
        TwinBike,
        TwinEBike,
        Others;
    	
    	public static BikeType toBikeType(String string) {
    		if (string.equalsIgnoreCase(NormalBike.toString())) {
    			return NormalBike;
    		} 
				else if (string.equalsIgnoreCase(EBike.toString())) {
    			return EBike;
    		} 
				else if (string.equalsIgnoreCase(TwinBike.toString())) {
    			return TwinBike;
    		} 
				else if (string.equalsIgnoreCase(TwinEBike.toString())) {
    			return TwinEBike;
    		}
				else {
    			return Others;
    		}
    	}

    	private static java.util.Map<BikeType, Float> chargeMultiplierDictionary = new java.util.HashMap<BikeType, Float>();
    	private static java.util.Map<BikeType, Float> priceDict = new java.util.HashMap<BikeType, Float>();
    	private static java.util.Map<BikeType, Integer> saddleDict = new java.util.HashMap<BikeType, Integer>();
    	private static java.util.Map<BikeType, Integer> pedalDict = new java.util.HashMap<BikeType, Integer>();
    	private static java.util.Map<BikeType, Integer> rearSeatDict = new java.util.HashMap<BikeType, Integer>();
    	private static java.util.Map<BikeType, Integer> motorDict = new java.util.HashMap<BikeType, Integer>();
    	
    	public static void setMultiplier(Configs.BikeType type, float multiplier) {
    		chargeMultiplierDictionary.put(type, multiplier);
    	}
    	
    	public static float getMultiplier(Configs.BikeType type) {
    		return chargeMultiplierDictionary.get(type);
    	}
    	
    	public static void setTypePrice(Configs.BikeType type, float price) {
    		priceDict.put(type, price);
    	}
    	
    	public static float getTypePrice(Configs.BikeType type) {
    		return priceDict.get(type);
    	}
    	
    	public static void setTypeSaddle(Configs.BikeType type, int num) {
    		saddleDict.put(type, num);
    	}
    	
    	public static int getTypeSadde(Configs.BikeType type) {
    		return saddleDict.get(type);
    	}
    	
    	public static void setTypePedals(Configs.BikeType type, int num) {
    		pedalDict.put(type, num);
    	}
    	
    	public static int getTypePedals(Configs.BikeType type) {
    		return pedalDict.get(type);
    	}
    	
    	public static void setTypeMotor(Configs.BikeType type, int num) {
    		motorDict.put(type, num);
    	}
    	
    	public static int getTypeMotor(Configs.BikeType type) {
    		return motorDict.get(type);
    	}
    	
    	public static void setTypeRearSeat(Configs.BikeType type, int num) {
    		rearSeatDict.put(type, num);
    	}
    	
    	public static int getTypeRearSeat(Configs.BikeType type) {
    		return rearSeatDict.get(type);
    	}
        
    }
	
	public enum TransactionType {
		PAY_DEPOSIT("PAY_DEPOSIT"),
		PAY_RENTAL("PAY_RENTAL"),
		RETURN_DEPOSIT("RETURN_DEPOSIT");

		private String transactionType;
		
		TransactionType(String string) {
			this.transactionType = string;
		}
		
		public String toString() {
			return this.transactionType;
		}
	}
}
