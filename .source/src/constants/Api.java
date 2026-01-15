package constants;

public class Api {
  public static String JDBC_LOCAL_CLASS_NAME = "org.sqlite.JDBC";
  public static String JDBC_LOCAL_URL = "jdbc:sqlite:src/lib/ecobikedb.db";

  public static String QUERY_INIT = "SELECT * FROM Configs";
  public static String QUERY_GET_BIKES_BY_DOCK_ID = "SELECT Bike.name as name,"
  + "Bike.bike_type as bike_type,"
  + "Bike.license_plate_code as license_plate_code,"
  + "Bike.bike_image as bike_image,"
  + "Bike.bike_barcode as bike_barcode,"
  + "Bike.currency_unit as currency_unit,"
  + "Bike.create_date as create_date,"
  + "BikeStatus.total_rent_time as total_rent_time,"
  + "BikeStatus.current_battery as current_battery,"
  + "BikeStatus.current_status as current_status FROM Bike, BikeStatus, BikeInDock WHERE dock_id=? "
  + "And Bike.bike_barcode = BikeStatus.bike_barcode And "
  + "BikeStatus.bike_barcode = BikeInDock.bike_barcode";
  public static String QUERY_GET_ALL_BIKES = "SELECT Bike.name as name,"
  + "Bike.bike_type as bike_type,"
  + "Bike.license_plate_code as license_plate_code,"
  + "Bike.bike_image as bike_image,"
  + "Bike.bike_barcode as bike_barcode,"
  + "Bike.currency_unit as currency_unit,"
  + "Bike.create_date as create_date,"
  + "BikeStatus.total_rent_time as total_rent_time,"
  + "BikeStatus.current_battery as current_battery,"
  + "BikeStatus.current_status as current_status FROM Bike, BikeStatus WHERE Bike.bike_barcode = BikeStatus.bike_barcode";
  public static String QUERY_GET_DOCK_INFO_BY_ID = "SELECT * FROM Dock WHERE dock_id=?";
  public static String QUERY_GET_ALL_DOCKS = "Select * from Dock";
  public static String QUERY_CHANGE_BIKE_STATUS = "UPDATE BikeStatus SET current_status=? WHERE bike_barcode=?";
  public static String QUERY_REMOVE_BIKE_FROM_DOCKS = "DELETE FROM BikeInDock WHERE bike_barcode=?";
  public static String QUERY_ADD_BIKE_TO_DOCK = "INSERT INTO BikeInDock(dock_id, bike_barcode) VALUES (?,?)";
  public static String QUERY_ADD_START_RENT_BIKE_RECORD = "INSERT INTO RentBike(bike_barcode, start_time)" + "VALUES (?,?)";
  public static String QUERY_ADD_END_RENT_BIKE_RECORD = "UPDATE RentBike SET end_time=? WHERE rent_id=?";
  public static String QUERY_ADD_PERIOD_RENT_BIKE_RECORD = "UPDATE RentBike SET rent_period=? WHERE rent_id=?";
  public static String QUERY_ADD_TRANSACTION = "INSERT INTO EcoBikeTransaction (transaction_amount, transaction_time, transaction_detail, creditcard_number, rent_id) VALUES (?,?,?,?,?)";
  public static String QUERY_GET_BIKE_LOCATION = "SELECT dock_id FROM BikeInDock WHERE bike_barcode=?";
  public static String QUERY_GET_BIKE_DOCK_LOCATION = "SELECT name FROM Dock WHERE dock_id=?";
  public static String QUERY_GET_BIKE_BATTERY = "SELECT current_battery FROM BikeStatus WHERE bike_barcode=?";
  public static String QUERY_SAVE_RENT_PERIOD = "UPDATE RentBike SET rent_period=? WHERE rent_id=?";
  public static String QUERY_GET_BIKE_CURRENT_RENT_PERIOD = "SELECT rent_period FROM RentBike WHERE bike_barcode=? ORDER BY rent_id DESC LIMIT 1";
  public static String QUERY_GET_CURRENT_BIKE_RENTING = "SELECT * FROM RentBike WHERE bike_barcode=? ORDER BY rent_id DESC LIMIT 1"; 
  public static String QUERY_GET_CURRENT_BIKE_RENTING_2 = "SELECT * FROM EcoBikeTransaction WHERE rent_id=?"; 
}
