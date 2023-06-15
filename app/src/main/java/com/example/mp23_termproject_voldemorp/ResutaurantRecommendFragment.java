package com.example.mp23_termproject_voldemorp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class ResutaurantRecommendFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    static public String restaurantName;

    private int portNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_recommend, container, false);

        TextView peoplePortNum = view.findViewById(R.id.textView241);
        TextView myPortNum = view.findViewById(R.id.textView24);

        // Generate a random value for peoplePortNum
        Random random = new Random();
        int randomValue = random.nextInt(100); // Generate a random integer between 0 and 99
        peoplePortNum.setText(String.valueOf(randomValue));

        // [Server] Retrieve my visit count from the data

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Get the user's userId and retrieve the portNum from the database
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference reference = database.getReference("users").child(userId).child("restaurant").child(restaurantName);

        // Check if the data exists
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // If the restaurant exists in the userDB
                if (dataSnapshot.exists()) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userId).child("restaurant").child(restaurantName).child("portNum");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            portNum = dataSnapshot.getValue(Integer.class);
                            String strPortNum = String.valueOf(portNum);
                            myPortNum.setText(strPortNum);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle the cancellation
                            myPortNum.setText("0");
                        }
                    });
                }
                // If the restaurant doesn't exist in the userDB
                else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });

        return view;
    }
}