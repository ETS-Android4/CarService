package com.badawy.carservice.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.adapters.AccountInfoLabelsAdapter;
import com.badawy.carservice.models.UserProfileModel;
import com.badawy.carservice.utils.Constants;
import com.badawy.carservice.utils.MySharedPreferences;
import com.badawy.carservice.utils.MyValidation;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavSettingsFragment extends Fragment implements AccountInfoLabelsAdapter.OnItemClickListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    private RecyclerView accountInfoRV;
    private final String[] accountInfoLabels = {"User Name", "Email Address", "Address", "Phone Number"};
    private CircleImageView profileImageView;
    private ImageView profileEditImageView, changePasswordEditIcon;
    private DatabaseReference dbRef;
    private UserProfileModel userDataObject;
    private List<String> userData;
    private AccountInfoLabelsAdapter accountInfoLabelsAdapter;
    private FirebaseAuth firebaseAuth;

    public NavSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_settings, container, false);
        dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.USERS);

        // Initialize FireBase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        ImageView navMenuBtn = view.findViewById(R.id.settings_navMenuBtn);
        accountInfoRV = view.findViewById(R.id.settings_accountInfoRV);
        profileImageView = view.findViewById(R.id.settings_profile_image);
        profileEditImageView = view.findViewById(R.id.settings_profile_edit);
        changePasswordEditIcon = view.findViewById(R.id.settings_changePasswordEdit);

        // get user data from shared preferences then bind it to the views
        bindUserData();


        // Clicks
        changePasswordEditIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareDialog("Password", 5);
            }
        });

        profileEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });

        return view;
    }


    // Allow the user to choose a picture from his gallery
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {


            // when the user choose an image ... upload to firebase
            Uri imageUri = data.getData();
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {

        final StorageReference fileRef = FirebaseStorage.getInstance().getReference()
                .child(Constants.USERS).child("IMG" + System.currentTimeMillis());

        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageDownloadUri = uri.toString();

                        dbRef.child(userDataObject.getUserId()).child(Constants.USER_PROFILE)
                                .child("profileImageUri").setValue(imageDownloadUri).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                bindUserData();
                            }
                        });
                    }
                });

            }
        });


    }

    private void bindUserData() {
        userData = new ArrayList<>();

        // Get User`s Data From Shared Preferences
        Gson gson = new Gson();
        String userSerializedData = MySharedPreferences.read(MySharedPreferences.USER_DATA, "");

        // If Its Not null Then Add all Fields in the Correct Order to a List
        if (!userSerializedData.equals("")) {
            userDataObject = gson.fromJson(userSerializedData, UserProfileModel.class);

            // Profile image
            if (userDataObject.getProfileImageUri().equals("")) {
                profileImageView.setImageResource(R.drawable.ic_default_profile);
            } else {
                Glide.with(this).load(userDataObject.getProfileImageUri()).into(profileImageView);
            }

            // Data must be added in the same order of the layout  name,email,address,phone
            userData.add(userDataObject.getUserName());
            userData.add(userDataObject.getEmailAddress());
            if (userDataObject.getAddress().equals("")) {

                userData.add(getString(R.string.default_address));
            } else {
                userData.add(userDataObject.getAddress());
            }

            userData.add(userDataObject.getPhoneNumber());


            // create and set the adapter
            accountInfoLabelsAdapter = new AccountInfoLabelsAdapter(getActivity(), accountInfoLabels, userData, this);
            accountInfoRV.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            accountInfoRV.setAdapter(accountInfoLabelsAdapter);
        }


    }

    @Override
    public void onEditClick(int position) {

        prepareDialog(accountInfoLabels[position], position);

    }

    private void prepareDialog(final String userInfo, final int position) {
        final EditText taskEditText = new EditText(getActivity());
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Update your " + userInfo)
                .setMessage("Type in the new " + userInfo.toLowerCase() + " then click ok")
                .setView(taskEditText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String newValue = taskEditText.getText().toString().trim();
                        if (!newValue.equals("")) {

                            if (position == 0) {
                                dbRef.child(userDataObject.getUserId()).child(Constants.USER_PROFILE)
                                        .child("userName").setValue(newValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), userInfo + " Changed Successfully", Toast.LENGTH_SHORT).show();
                                        bindUserData();
                                    }
                                });


                            } else if (position == 2) {
                                dbRef.child(userDataObject.getUserId()).child(Constants.USER_PROFILE)
                                        .child("address").setValue(newValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), userInfo + " Changed Successfully", Toast.LENGTH_SHORT).show();
                                        bindUserData();
                                    }
                                });

                            } else if (position == 3) {
                                dbRef.child(userDataObject.getUserId()).child(Constants.USER_PROFILE)
                                        .child("phoneNumber").setValue(newValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), userInfo + " Changed Successfully", Toast.LENGTH_SHORT).show();
                                        bindUserData();
                                    }
                                });
                                //Change Password
                            } else if (position == 5) {
                                if (MyValidation.isPassword(taskEditText)) {


                                    Objects.requireNonNull(firebaseAuth.getCurrentUser()).updatePassword(newValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Failed ..Please log out then Sign in back again", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }


                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}
