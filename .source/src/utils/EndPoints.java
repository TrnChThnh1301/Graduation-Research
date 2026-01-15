package utils;

import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import constants.Api;
import constants.BikeStatuses;
import entities.Bike;
import entities.BikeTracker;
import entities.Dock;
import entities.PaymentTransaction;
import exceptions.ecobike.EcoBikeException;
import exceptions.ecobike.InvalidEcoBikeInformationException;
import utils.Configs.BikeType;

public class EndPoints {
	private static Connection connection;
	
	public static Connection getConnection() throws EcoBikeException {
    if (connection == null) {
			try {
        Class.forName(Api.JDBC_LOCAL_CLASS_NAME);
        connection = DriverManager.getConnection(Api.JDBC_LOCAL_URL);
        init();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
     return connection;
	}
	
	private static void init() throws SQLException {
		PreparedStatement statement = connection.prepareStatement(Api.QUERY_INIT);
		ResultSet result = statement.executeQuery();
		
		while(result.next()) {
			BikeType type = Configs.BikeType.toBikeType(result.getString("bike_type"));
			
			Configs.BikeType.setMultiplier(type, result.getFloat("rent_factor"));
			Configs.BikeType.setTypeMotor(type, result.getInt("motors"));
			Configs.BikeType.setTypePedals(type, result.getInt("pedals"));
			Configs.BikeType.setTypePrice(type, result.getFloat("bike_price"));
			Configs.BikeType.setTypeRearSeat(type, result.getInt("rear_seats"));
			Configs.BikeType.setTypeSaddle(type, result.getInt("saddles"));
		}
	}
	
	public static ArrayList<Bike> getAllBikesByDockId(int dockId) throws SQLException, EcoBikeException {
		PreparedStatement stm = EndPoints.getConnection().prepareStatement(Api.QUERY_GET_BIKES_BY_DOCK_ID);
		stm.setInt(1, dockId);
		
		ResultSet result = stm.executeQuery();
		ArrayList<Bike> list = new ArrayList<Bike>();
		
		while (result.next()) {
			Bike bikeRes = null;

			try {
				bikeRes = BikeFactory.getBikeWithInformation(result);
			} catch (IllegalArgumentException | SecurityException e) {
				e.printStackTrace();
			}
			
			String bikeStatus = result.getString("current_status");
			BikeStatuses.Instances bikeStat;

			if (bikeStatus.equalsIgnoreCase("FREE")) {
				bikeStat = BikeStatuses.Instances.FREE;
			} 
			else if (bikeStatus.equalsIgnoreCase("RENTED")) {
				bikeStat = BikeStatuses.Instances.RENTED;
			} 
			else {
				throw new InvalidEcoBikeInformationException("INVALID_STATUS");
			}

			bikeRes.setCurrentStatus(bikeStat);
			list.add(bikeRes);
		}
		
		return list;
	}
	
	public static ArrayList<Bike> getAllBikes() throws SQLException, EcoBikeException {
		Statement stm = EndPoints.getConnection().createStatement();
		ResultSet result = stm.executeQuery(Api.QUERY_GET_ALL_BIKES);

		ArrayList<Bike> bikes = new ArrayList<Bike>();

		while (result.next()) {
			Bike bikeRes = null;

			try {
				bikeRes = BikeFactory.getBikeWithInformation(result);
			} catch (IllegalArgumentException | SecurityException e) {
				e.printStackTrace();
			}
			
			String bikeStatus = result.getString("current_status");
			BikeStatuses.Instances bikeStat;

			if (bikeStatus.equalsIgnoreCase("FREE")) {
				bikeStat = BikeStatuses.Instances.FREE;
			}
			else if (bikeStatus.equalsIgnoreCase("RENTED")) {
				bikeStat = BikeStatuses.Instances.RENTED;
			} 
			else {
				throw new InvalidEcoBikeInformationException("INVALID STATUS");
			}

			bikeRes.setCurrentStatus(bikeStat);
			bikes.add(bikeRes);
		}

		return bikes;
	}
	
	public static ArrayList<Bike> getAllRentedBikes() throws SQLException, EcoBikeException {
		ArrayList<Bike> allBikes = getAllBikes();
		ArrayList<Bike> result = new ArrayList<Bike>();

		for (Bike bike : allBikes) {
			if (bike.getCurrentStatus() == BikeStatuses.Instances.RENTED) {
				result.add(bike);
			}
		}

		return result;
	}

	public static Dock getDockInformationByID(int dockID) throws SQLException, EcoBikeException {
		PreparedStatement stm = EndPoints.getConnection().prepareStatement(Api.QUERY_GET_DOCK_INFO_BY_ID);
		stm.setInt(1, dockID);
		ResultSet result = stm.executeQuery();

		Dock dock = new Dock(
			result.getString("name"), 
			result.getInt("dock_id"), 
			result.getString("dock_address"), 
			result.getDouble("dock_area"),
			result.getInt("num_dock"),
			result.getString("dock_image")
		);

		ArrayList<Bike> listBike = getAllBikesByDockId(result.getInt("dock_id"));
		
		for (Bike bike: listBike) {
			bike.goToDock(dock);
		}
		return dock;
	}
	
	public static ArrayList<Dock> getAllDocks() throws SQLException, EcoBikeException {
		Statement stm = EndPoints.getConnection().createStatement();
		ResultSet result = stm.executeQuery(Api.QUERY_GET_ALL_DOCKS);
		ArrayList<Dock> docks = new ArrayList<Dock>();

		while (result.next()) {
			Dock dock = new Dock(
				result.getString("name"), 
				result.getInt("dock_id"), 
				result.getString("dock_address"), 
				result.getDouble("dock_area"),
				result.getInt("num_dock"),
				result.getString("dock_image")
			);

			ArrayList<Bike> listBike = getAllBikesByDockId(result.getInt("dock_id"));

			for (Bike bike: listBike) {
				bike.goToDock(dock);
			}

			docks.add(dock);
		}
		
		return docks;
	}
	
	public static void changeBikeStatus(String bikeBarcode, String newStatus) throws SQLException, EcoBikeException {
		PreparedStatement sqlStm = EndPoints.getConnection().prepareStatement(Api.QUERY_CHANGE_BIKE_STATUS);

		sqlStm.setString(1, newStatus);
		sqlStm.setString(2, bikeBarcode);
		sqlStm.executeUpdate();
	}
	
	public static void removeBikeFromDock(String bikeBarcode) throws SQLException, EcoBikeException {
		PreparedStatement sqlStm = EndPoints.getConnection().prepareStatement(Api.QUERY_REMOVE_BIKE_FROM_DOCKS);
		
		sqlStm.setString(1, bikeBarcode);
		sqlStm.execute();
	}
	
	public static void addBikeToDock(String bikeBarcode, int dockID) throws SQLException, EcoBikeException {
		PreparedStatement sqlStm = EndPoints.getConnection().prepareStatement(Api.QUERY_ADD_BIKE_TO_DOCK);

		sqlStm.setInt(1, dockID);
		sqlStm.setString(2, bikeBarcode);
		sqlStm.execute();
	}

	public static int addStartRentBikeRecord(String bikeBarcode) throws SQLException, EcoBikeException {
		PreparedStatement sqlStm = EndPoints.getConnection().prepareStatement(Api.QUERY_ADD_START_RENT_BIKE_RECORD, Statement.RETURN_GENERATED_KEYS);
		
		sqlStm.setString(1, bikeBarcode);
		sqlStm.setString(2, Calendar.getInstance().getTime().toString());
		sqlStm.execute();
		
		ResultSet result = sqlStm.getGeneratedKeys();

		if (result.next()) {
			return result.getInt(1);
		}
		
		return -1;	
	}

	public static void addEndRentBikeRecord(int rentID, int rentPeriod) throws SQLException, EcoBikeException, ParseException {
		PreparedStatement sqlStm = EndPoints.getConnection().prepareStatement(Api.QUERY_ADD_END_RENT_BIKE_RECORD);
		sqlStm.setInt(1, rentID);

		Date end = Calendar.getInstance().getTime();
		sqlStm = EndPoints.getConnection().prepareStatement(Api.QUERY_ADD_END_RENT_BIKE_RECORD);
		sqlStm.setString(1, end.toString());
		sqlStm.setInt(2, rentID);
		sqlStm.execute();

		sqlStm = EndPoints.getConnection().prepareStatement(Api.QUERY_ADD_PERIOD_RENT_BIKE_RECORD);
		sqlStm.setInt(1, rentPeriod);
		sqlStm.setInt(2, rentID);
		sqlStm.execute();
	}

	public static int addTransaction(PaymentTransaction transaction, int rentID) throws SQLException, EcoBikeException {
		PreparedStatement sqlStm = EndPoints.getConnection().prepareStatement(Api.QUERY_ADD_TRANSACTION, Statement.RETURN_GENERATED_KEYS);

		sqlStm.setFloat(1, (float)transaction.getAmount());
		sqlStm.setString(2, transaction.getTransactionTime().toString());
		sqlStm.setString(3, transaction.getContent());
		sqlStm.setString(4, transaction.getCreditCardNumber());
		sqlStm.setInt(5, rentID);
		sqlStm.execute();

		ResultSet result = sqlStm.getGeneratedKeys();

		if (result.next()) {
			int id = result.getInt(1);
			transaction.setTransactionId(id);
			return id;
		}
		
		return -1;		
	}
	
	public static String getBikeLocation(String bikeBarcode) throws SQLException, EcoBikeException {
		PreparedStatement sqlStm = EndPoints.getConnection().prepareStatement(Api.QUERY_GET_BIKE_LOCATION);
		sqlStm.setString(1, bikeBarcode);
		ResultSet result = sqlStm.executeQuery();
		int dockID = result.getInt("dock_id");

		sqlStm = EndPoints.getConnection().prepareStatement(Api.QUERY_GET_BIKE_DOCK_LOCATION);
		sqlStm.setInt(1, dockID);
		result = sqlStm.executeQuery();

		String location = result.getString("name");
		return location;
	}
	
	public static float getBikeBattery(String bikeBarcode) throws SQLException, EcoBikeException {
		PreparedStatement sqlStm = EndPoints.getConnection().prepareStatement(Api.QUERY_GET_BIKE_BATTERY);
		sqlStm.setString(1, bikeBarcode);

		ResultSet result = sqlStm.executeQuery();
		float battery = result.getFloat("current_battery");

		return battery;
	}

	public static void saveRentPeriod(int rentID, int period) throws SQLException, EcoBikeException {
		PreparedStatement sqlStm = EndPoints.getConnection().prepareStatement(Api.QUERY_SAVE_RENT_PERIOD);

		sqlStm.setInt(1, rentID);
		sqlStm.setInt(2, period);
		sqlStm.execute();
	}

	public static int getCurrentRentPeriodOfBike(String bikeBarcode) throws SQLException, EcoBikeException {
		PreparedStatement sqlStm = EndPoints.getConnection().prepareStatement(Api.QUERY_GET_BIKE_CURRENT_RENT_PERIOD);
		sqlStm.setString(1, bikeBarcode);

		ResultSet result = sqlStm.executeQuery();
		return result.getInt("rent_period");
	}

	public static BikeTracker getCurrentBikeRenting(Bike bike) throws SQLException, EcoBikeException {
		PreparedStatement sqlStm = EndPoints.getConnection().prepareStatement(Api.QUERY_GET_CURRENT_BIKE_RENTING);
		sqlStm.setString(1, bike.getBikeBarCode());
		ResultSet resultI = sqlStm.executeQuery();

		int period = resultI.getInt("rent_period");
		int rentID = resultI.getInt("rent_id");

		sqlStm = EndPoints.getConnection().prepareStatement(Api.QUERY_GET_CURRENT_BIKE_RENTING_2);
		sqlStm.setInt(1, rentID);
		ResultSet result = sqlStm.executeQuery();

		BikeTracker tracker = new BikeTracker(bike, rentID);
		tracker.setRentedTime(period);
		tracker.setStartTime(resultI.getString("start_time"));
		tracker.setActive(true);

		while (result.next()) {
			PaymentTransaction trans = new PaymentTransaction();

			trans.setTransactionId(result.getInt("transaction_id"));
			trans.setAmount(result.getFloat("transaction_amount"));
			trans.setTransactionTime(result.getString("transaction_time"));
			trans.setCreditCardNumber(result.getString("creditcard_number"));
			trans.setRentID(result.getInt("rent_id"));
			trans.setContent(result.getString("transaction_detail"));

			tracker.addTransaction(trans);
		}
		
		return tracker;
	}
}

