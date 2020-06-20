package com.badawy.carservice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;

import com.badawy.carservice.R;
import com.badawy.carservice.models.AboutUsDataModel;
import com.badawy.carservice.models.CarModel;
import com.badawy.carservice.models.SparePartDetailsModel;
import com.badawy.carservice.models.SparePartModel;
import com.badawy.carservice.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class FirebaseUploadActivity extends AppCompatActivity {

    private DatabaseReference dbRef;
    private StorageReference storageRef;
    private UploadTask uploadTask;
    private String imageDownloadUri;
    private String manfDownloadUri;
    private ArrayList<SparePartDetailsModel> partDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_upload);

        dbRef = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference(Constants.SPARE_PARTS);
        final Button upload = findViewById(R.id.uploadBtn);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Uncomment each method and run it alone

                // Run them in this order

//                createAboutUsData();

//                createCarCareData();

//                createSpeedFixData();

//                createVehicleInspectionData();

//                createAvailableAppointments();

//                createBooking();

//                createSparePartsCategory();

                // Must Read instructions before calling this method.. Scroll Down to the method
//                uploadSparePartFile(R.drawable.a, R.drawable.aa);

            }
        });

    }


    // #1   Upload Cars

    private void createCars() {
        // Create new cars with the same pattern
        CarModel car1 = new CarModel(dbRef.push().getKey(), "2008", "Mitsubishi", "Lancer");
        uploadCar(R.drawable.mistubishi, car1);

        CarModel car2 = new CarModel(dbRef.push().getKey(), "2008", "Hyundai", "Elantra");
        uploadCar(R.drawable.hyundai, car2);

        CarModel car3 = new CarModel(dbRef.push().getKey(), "2008", "Jeep", "Grand Cherokee");
        uploadCar(R.drawable.jeep, car3);

    }

    private void uploadCar(int image, final CarModel car) {
        final StorageReference stRef = FirebaseStorage.getInstance().getReference().child(Constants.CARS).child(car.getCarBrand().toLowerCase().trim());
        Uri imageUri = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + image);

        stRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                stRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        car.setCarBrandLogoUri(uri.toString());

                        dbRef.child(Constants.APP_DATA)
                                .child(Constants.CARS)
                                .child(car.getCarID()).setValue(car);

                    }
                });

            }
        });
    }


    // #2 Upload App Data

    private void createSpeedFixData() {


        dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.APP_DATA)
                .child(Constants.SPEED_FIX);

        String[] speedFixData = {"Change Engine Oil", "Change Brake Oil", "Change Power Oil", "Change Gearbox Oil"
                , "Radiator Water", "Brake Pad", "Change Belts", "Cooling Cycle"};
        int[] speedFixPrice = {80, 60, 60, 60, 60, 70, 60, 80};


        for (int i = 0; i < speedFixData.length; i++) {
            dbRef.child(speedFixData[i])
                    .setValue(speedFixPrice[i]);

        }

    }

    private void createCarCareData() {

        String[] carCareData = {"Interior Car Wash", "Exterior Car Wash", "Interior Car Polishing", "Exterior Car Polishing", "Car Alarm", "Thermal Insulation"
                , "Nano Ceramic Coating", "Warning Indicators and Lights Symbols", "Car Lighter Replacement", "Seat Belt Replacement", "Car Seat Restoration"};
        int[] carCarePrice = {80, 110, 50, 60, 50, 150, 250, 40, 30, 60, 100};


        for (int i = 0; i < carCareData.length; i++) {
            dbRef.child(Constants.APP_DATA)
                    .child(Constants.CAR_CARE)
                    .child(carCareData[i])
                    .setValue(carCarePrice[i]);
        }
    }

    private void createAboutUsData() {
        // create object
        final AboutUsDataModel obj = new AboutUsDataModel();
        obj.setOverView("Car Service App Provides for you a lot of service such as setting a date to check your car, provide quick emergency assistance on the road and possibility of buying spare parts of your car online.");
        obj.setOpenHours("12 PM - 9 PM");
        obj.setEmailAddress("CarServiceShared8@gmail.com");
        obj.setPhoneNumber("01090488408");
        obj.setLat(29.9263114);
        obj.setLng(30.9045351);

        // upload to firebase
        dbRef.child(Constants.APP_DATA).child(Constants.ABOUT_US).setValue(obj);


    }

    private void createVehicleInspectionData() {

        String[] cycleData = {"Change Engine Oil", "Change Brake Oil", "Change Power Oil", "Change Gearbox Oil"
                , "Radiator Water", "Brake Pad", "Change Belts", "Cooling Cycle"};
        int[] cycleServicePrice = {50, 30, 30, 40, 30, 40, 30, 40};
        String[] specificFixesData = {"Wheels", "Engine Hood", "Fender", "Front Grille", "Front Bumper"
                , "Back Bumper", "Trunk", "Doors", "Mirror", "Windscreen Wiper", "Glass", "Tableau", "Air Conditioning", "Alternator"
                , "Battery", "Brakes", "Change Radiator", "Clutch", "Engine Overhaul", "Exhaust Pipe", "Fuel Tank", "Gaskets Set", "Gear Box"
                , "Gear Stick", "Hub", "Oil Filter", "Oil Pump", "Rings Set", "Shock Absorbers", "Spark Plug", "Steering Wheel", "Complete Overhaul"};
        int[] specificFixesPrice = {200, 200, 150, 70, 70, 100, 60, 50, 30, 100, 40, 150, 200, 100, 30, 40, 55, 60, 222, 120, 300, 320, 230, 100, 40, 50, 20, 100, 60, 30, 40, 200};

        for (int i = 0; i < cycleData.length; i++) {
            dbRef.child(Constants.APP_DATA)
                    .child(Constants.VEHICLE_INSPECTION)
                    .child(Constants.VEHICLE_INSPECTION_CYCLE)
                    .child(cycleData[i])
                    .setValue(cycleServicePrice[i]);

        }

        for (int i = 0; i < specificFixesData.length; i++) {
            dbRef.child(Constants.APP_DATA)
                    .child(Constants.VEHICLE_INSPECTION)
                    .child(Constants.VEHICLE_INSPECTION_SPECIFIC_FIXES)
                    .child(specificFixesData[i])
                    .setValue(specificFixesPrice[i]);

        }


    }


    // #3 Upload Available Appointments and Booking

    private void createBooking() {

        dbRef.child(Constants.BOOKING).child(Constants.APPOINTMENTS).setValue(0);
        dbRef.child(Constants.BOOKING).child(Constants.ORDERS).setValue(0);
    }

    private void createAvailableAppointments() {

        // AvailableAppointments >> Car Center Root
        dbRef.child(Constants.AVAILABLE_APPOINTMENTS)
                .child(Constants.CAR_CENTER)
                .child(Constants.VEHICLE_INSPECTION).setValue(0);

        dbRef.child(Constants.AVAILABLE_APPOINTMENTS)
                .child(Constants.CAR_CENTER)
                .child(Constants.CAR_CARE).setValue(0);


        //  AvailableAppointments >> Delivery Root
        dbRef.child(Constants.AVAILABLE_APPOINTMENTS)
                .child(Constants.DELIVERY)
                .child(Constants.SPEED_FIX).setValue(0);

    }


    // #4 Upload Spare Parts Categories

    private void createSparePartsCategory() {

        String[] sparePartsCategories = {
                "Brake System", "Engine", "Filters", "Oils And Fluids", "Body Parts,Lights,Mirrors", "Air Conditioning"
                , "Tyres", "Shock Absorbers", "Exhaust System"
        };

        for (String sparePartsCategory : sparePartsCategories) {
            dbRef.child(Constants.APP_DATA)
                    .child(Constants.SPARE_PARTS_CATEGORIES)
                    .child(sparePartsCategory)
                    .setValue("0");

        }
    }


    // #5 Upload a Spare Part to the Database
    // First step:
    // Rename product Image to [a] and manufacturer image to [aa]  ,, without the []
    // copy paste product image and manufacturer logo image inside the Drawable folder

    // Second Step:
    // Adjust uploadProductObject Method .. change car id for the car u want to add products To ..
    // change category name to the category u want to add the part To...

    // Third Step:
    // Copy Paste the part Details in createSparePartsData Method From Word File

    // Last Step;
    // UnComment the uploadSparePartFile method in Button onclick
    private void uploadSparePartFile(final int productImageResourceID, int manufacturerLogoResourceID) {

        // Make the Uri of each image
        Uri productImageUri = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + productImageResourceID);
        final Uri manufacturerLogoUri = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + manufacturerLogoResourceID);

        // Firebase Product Image Storage Reference
        final StorageReference productImageRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(productImageUri));

        // Upload the images first ... Get the download Url of each image
        // .. pass the links Global Variables  imageDownloadUri and manufacturerDownloadUri
        // .. then Create the data of the spare part and Upload it to Database
        productImageRef.putFile(productImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                productImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        imageDownloadUri = uri.toString();

                        // Firebase Manufacturer Logo Storage Reference
                        final StorageReference manufacturerLogoRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(manufacturerLogoUri));

                        manufacturerLogoRef.putFile(manufacturerLogoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                manufacturerLogoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        manfDownloadUri = uri.toString();

                                        // Upload the object
                                        createSparePartsData();
                                    }
                                });
                            }
                        });
                    }
                });

            }
        });


    }

    // Called From UploadSparePartFile
    private void createSparePartsData() {

    }


    // Called From createSparePartsData
    private void uploadProductObject(SparePartModel productObject) {
        String jeep = "-MA-bnII7omY75aLpB_w";
        String hyundai = "-MA-bnIFC0VcaLH97CzP";

        String categoryName = "Body Parts,Lights,Mirrors";
        dbRef.child(Constants.APP_DATA).child(Constants.SPARE_PARTS).child(categoryName)
                .child(productObject.getProductID())
                .setValue(productObject);

        createSparePartsOfCar(categoryName, jeep, productObject.getProductID());
    }


    // 5 Upload the Link between cars and spare parts .. what car has Which spareParts
    private void createSparePartsOfCar(String categoryName, String carID, String sparePartID) {

        String[] sparePartsCategories = {
                "Brake System", "Engine", "Filters", "Oils And Fluids", "Body Parts,Lights,Mirrors", "Air Conditioning"
                , "Tyres", "Shock Absorbers", "Exhaust System"
        };

//        for (String sparePartsCategory : sparePartsCategories) {
//            dbRef.child(Constants.CARS_SPARE_PARTS).child("-M9GME1uGGihgnTNE_IG").child(sparePartsCategory).setValue(0);
//        }


        dbRef.child(Constants.CARS_SPARE_PARTS).child(carID).child(categoryName).child(sparePartID).setValue(0);

    }


    // Regular Methods
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void fillList(String[] name, String[] value) {

        partDetailsList = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {

            partDetailsList.add(new SparePartDetailsModel(name[i], value[i]));

        }

    }


}
