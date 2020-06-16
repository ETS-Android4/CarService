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
                // call the function of the data you want to upload
//                uploadFile(R.drawable.brake3, R.drawable.brake3_manf);
//                createSparePartsOfCar();
                createBooking();
            }
        });

    }


    private void createSparePartsOfCar(){

        String[] sparePartsCategories = {
                "Brake System", "Engine", "Filters", "Oils And Fluids", "Body Parts,Lights,Mirrors", "Air Conditioning"
                , "Tyres", "Shock Absorbers", "Exhaust System"
        };

//        for (String sparePartsCategory : sparePartsCategories) {
//            dbRef.child(Constants.CARS_SPARE_PARTS).child("-M9GME1uGGihgnTNE_IG").child(sparePartsCategory).setValue(0);
//        }


        dbRef.child(Constants.CARS_SPARE_PARTS).child("-M9GME1uGGihgnTNE_IG").child("Brake System").child("402B0359").setValue(0);
        dbRef.child(Constants.CARS_SPARE_PARTS).child("-M9GME1uGGihgnTNE_IG").child("Brake System").child("82B0492").setValue(0);
        dbRef.child(Constants.CARS_SPARE_PARTS).child("-M9GME1uGGihgnTNE_IG").child("Filters").child("K13912").setValue(0);


    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(final int image, int manf) {
        Uri imageUri = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + image);
        final Uri manfUri = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + manf);

        final StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageDownloadUri = uri.toString();
                        final StorageReference manfRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(manfUri));

                        manfRef.putFile(manfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                manfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        manfDownloadUri = uri.toString();

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

    private void createSparePartsData() {


        String[] brake1DetailName = {
                " Fitting Position: "
                , " Diameter [mm]: "
                , " Centering Diameter [mm]: "
                , " Hole Arrangement / Number: "
                , " Bolt Hole Circle Ø [mm]: "
                , " Brake Disc Thickness [mm]:  "
                , " Minimum thickness [mm]:"
                , " Height [mm]: "
                , " Weight [kg]:"
                , " Directional: "
                , " Not tied to running direction: "
                , " Condition: "

        };

        String[] brake1DetailValue = {
                "Front Axle"
                , " 328 "
                , " Externally Vented "
                , " 72 "
                , " 05/05 ", " 127 "
                , " 30 "
                , " 28,5 ", " 53 "
                , " 10,2 "
                , " yes "
                , " yes "
                , " New "
        };

//        SparePartModel brake1 = new SparePartModel(
//                manfDownloadUri
//                , imageDownloadUri
//                , " Brake Disc RIDEX "
//                , "82B0492"
//                , "without wheel hub, without wheel studs, Externally Vented, Front Axle"
//                , "617 EGP"
//                , partDetailsList
//        );

//        String[] brake2DetailName = {
//
//                " Fitting Position: "
//                , "Brake Type: "
//                , "Required quantity: "
//                , "Weight [kg]: "
//                , "Condition: "
//
//
//        };
//
//        String[] brake2DetailValue = {
//                "  Front "
//                , "  Disc Brake "
//                , "  1 "
//                , "  0,34 "
//                , "  New "
//
//        };
//
//        fillList(brake2DetailName, brake2DetailValue);
//        SparePartModel brake2 = new SparePartModel(
//                manfDownloadUri
//                , imageDownloadUri
//                , " Cable, parking brake A.B.S.  "
//                , "K13912"
//                , "Front"
//                , "251 EGP"
//                , partDetailsList
//        );
//        uploadProductObject(brake2);
//
//
        String[] brake3DetailName = {

                  " Quantity Unit: "
                , " Fitting Position:"
                , " Wear Warning Contact: "
                , " Height [mm]:  "
                , " Width [mm]: "
                , " Thickness [mm]: "
                , " WVA Number:    "
                , " Brake System:  "
                , " Condition:  "

        };

        String[] brake3DetailValue = {
                "   Axle Set "
                , " Front Axle "
                , " with acoustic wear warning "
                , " 57 "
                , " 175,8 "
                , " 18,6 "
                , " 24250 "
                , " Akebono "
                , " New "

        };

        fillList(brake3DetailName, brake3DetailValue);

        SparePartModel brake3 = new SparePartModel(
                manfDownloadUri
                , imageDownloadUri
                , "  Brake Pad Set, disc brake RIDEX  "
                , "402B0359"
                , "with acoustic wear warning, Front Axle"
                , "377 EGP"
                , partDetailsList
        );
        uploadProductObject(brake3);


    }

    private void createSparePartsCategory() {

        String[] sparePartsCategories = {
                "Brake System", "Engine", "Filters", "Oils And Fluids", "Body Parts,Lights,Mirrors", "Air Conditioning"
                , "Tyres", "Shock Absorbers", "Exhaust System"
        };

        for (String sparePartsCategory : sparePartsCategories) {
            dbRef.child(Constants.APP_DATA)
                    .child(Constants.SPARE_PARTS)
                    .child(sparePartsCategory)
                    .setValue("0");

        }
    }

    private void uploadProductObject(SparePartModel productObject) {
        dbRef.child(Constants.APP_DATA).child(Constants.SPARE_PARTS).child("Brake System")
                .child(productObject.getProductID())
                .setValue(productObject);
    }

    private void fillList(String[] name, String[] value) {

        partDetailsList = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {

            partDetailsList.add(new SparePartDetailsModel(name[i], value[i]));

        }

    }

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

//        for (int i = 0; i<cycleData.length;i++){
//            dbRef.child(Constants.APP_DATA)
//                    .child(Constants.VEHICLE_INSPECTION)
//                    .child(Constants.VEHICLE_INSPECTION_CYCLE)
//                    .child(cycleData[i])
//                    .setValue(cycleServicePrice[i]);
//
//        }


    }

    private void createBooking(){

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

        dbRef.child(Constants.AVAILABLE_APPOINTMENTS)
                .child(Constants.DELIVERY)
                .child(Constants.SPARE_PARTS_ORDERS)
                .setValue(0);
    }

    private void createCars() {
        //    String carId = dbRef.push().getKey();
//        CarModel car1 = new CarModel();
//        car1.setCarYear("2008");
//        car1.setCarBrand("Mitsubishi");
//        car1.setCarModel("Lancer");
//        car1.setCarID(carId);
        CarModel car2 = new CarModel(dbRef.push().getKey(), "2008", "Hyundai", "Elantra");
        dbRef.child(Constants.APP_DATA)
                .child(Constants.CARS)
                .child(car2.getCarID()).setValue(car2);
        CarModel car3 = new CarModel(dbRef.push().getKey(), "2008", "Jeep", "Grand Cherokee");
        dbRef.child(Constants.APP_DATA)
                .child(Constants.CARS)
                .child(car3.getCarID()).setValue(car3);
    }
}
