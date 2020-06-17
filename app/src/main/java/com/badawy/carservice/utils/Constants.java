package com.badawy.carservice.utils;

public class Constants {

    // Firebase Roots
    public static String APP_DATA = "AppData" ;
    public static String ABOUT_US = "AboutUs" ;
    public static String CAR_CENTER = "CarCenter" ;
    public static String AVAILABLE_APPOINTMENTS = "AvailableAppointments" ;
    public static String SPARE_PARTS_ORDERS = "SparePartsOrders" ;
    public static String VEHICLE_INSPECTION = "VehicleInspection" ;
    public static String CAR_CARE = "CarCare" ;
    public static String DELIVERY = "Delivery" ;
    public static String SPEED_FIX = "SpeedFix" ;
    public static String SPARE_PARTS = "SpareParts" ;
    public static String SPARE_PARTS_CATEGORIES = "SparePartsCategories" ;
    public static String VEHICLE_INSPECTION_CYCLE = "Cycle" ;
    public static String VEHICLE_INSPECTION_SPECIFIC_FIXES= "SpecificFixes" ;
    public static String USERS= "Users" ;
    public static String USER_PROFILE= "UserProfile" ;
    public static String USER_CARS= "UserCars" ;
    public static String CARS= "Cars" ;
    public static String CARS_SPARE_PARTS= "CarsSpareParts" ;
    public static String BOOKING= "Booking" ;
    public static String APPOINTMENTS= "Appointments" ;
    public static String ORDERS= "Orders" ;
    public static String SHOPPING_CART= "ShoppingCart" ;
    public static String APPOINTMENTS_ORDERS= "AppointmentsAndOrders" ;


    // Car Center
    public static String SERVICE_NAME_BUNDLE_KEY = "ServiceName";
    public static String SERVICE_TYPES_NAME_RESULT = "ServiceTypesResult";
    public static String SERVICE_TYPES_PRICE_RESULT = "ServiceTypesPriceResult";
    public static String SELECTED_CAR = "SelectedCar";


    // SingleTon pattern
    private static Constants constants;
    public static Constants getInstance(){

        if(constants == null){
            constants = new Constants();
        }

        return constants;
    }

}
