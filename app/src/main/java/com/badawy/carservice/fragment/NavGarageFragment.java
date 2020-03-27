package com.badawy.carservice.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;



/**
 * A simple {@link Fragment} subclass.
 */

//CREATED BY AHMED TAREK..............................................................13/3/2020
public class NavGarageFragment extends Fragment {


    public NavGarageFragment() {
        // Required empty public constructor
    }


    private int index=1;
    private int persent;
    private int numofcar;

    private ProgressBar progressBar;
    private EditText numberInput;
    private Button submit;
    private Button submit1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_garage, container, false);



        //intializing our variables AHMED TAREK
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        numberInput = (EditText) view.findViewById(R.id.number_input);//edit text
        submit = (Button) view.findViewById(R.id.submit_button);
        submit1=(Button)view.findViewById(R.id.submit_button1);




//our action by the submit button the first button +
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to rentiate persent

                String receiver=numberInput.getText().toString().trim();
                //if the edit text is not empty do this  or excute these steps
                if (!"".equals(receiver)) {

                    numofcar = Integer.parseInt(receiver);
                    //the numofcars should be less than 100 or 100 <=100
                    persent = 100 / numofcar;


                }


                if ((persent * index) <= 100) {

                    progressBar.setProgress((persent * index));
                    index++;


                    //to make the i=5
                    //if index > numofcar
                    if (index > numofcar) {
                        index = index - 1;
                    }

                }


                // progressBar.setProgress(Integer.parseInt(numberInput.getText().toString()));
            }
        });



        //the second button of -
        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){


                // to reintiate again
                persent=100/numofcar;

                if((persent*index)>persent && (persent*index)<=100) {
                    progressBar.setProgress((persent*index)-persent);
                    index--;
                }
            }

        });





        ImageView navMenuBtn = view.findViewById(R.id.navGarage_navMenuBtn);
        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });
        return view;
    }

}
