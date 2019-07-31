package com.minjo.jangboo;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public int check = 1;
    public static long sin = 1;
    public static long total = 0;


    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    TextView TextView_min, TextView_jo, TextView_half, TextView_sol, TextView_dept;
    TextInputEditText TextInputEditText_charge;
    Button Button_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView_min = findViewById(R.id.TextView_min);
        TextView_jo = findViewById(R.id.TextView_jo);
        TextView_half = findViewById(R.id.TextView_half);
        TextView_sol = findViewById(R.id.TextView_sol);
        TextView_dept = findViewById(R.id.TextView_dept);
        TextInputEditText_charge = findViewById(R.id.TextInputEditText_charge);
        Button_update = findViewById(R.id.Button_update);


        TextView_half.setClickable(true);
        TextView_half.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 1;
                colorChange(TextView_half, TextView_sol);
            }
        });

        TextView_sol.setClickable(true);
        TextView_sol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 0;
                colorChange(TextView_sol, TextView_half);
            }
        });

        TextView_min.setClickable(true);
        TextView_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sin = 1;
                colorChange(TextView_min, TextView_jo);
                String a = String.valueOf(sin);
                Log.d("Test", a);
            }
        });

        TextView_jo.setClickable(true);
        TextView_jo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sin = -1;
                colorChange(TextView_jo, TextView_min);
                String a = String.valueOf(sin);
                Log.d("Test", a);
            }
        });

        Button_update.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // 버튼 누르면 수행 할 명령
                String temp = TextInputEditText_charge.getText().toString();
                if (!isLong(temp)) {
                    Toast.makeText(MainActivity.this, "빡대가리~", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    long charge = Long.parseLong(temp);
                    if (check == 1){
                        databaseReference.child("total").setValue(total + charge/2 * sin);
                    }
                    else {
                        databaseReference.child("total").setValue(total + charge * sin);
                    }
                }
            }
        });

        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                total = (long) dataSnapshot.child("total").getValue();
                if (total>=0) {
                    String dept = String.valueOf(total);
                    if(total==0){
                        TextView_dept.setText("평화상태");
                    }
                    else{
                        TextView_dept.setText("민에게\r\n" + dept + "원");
                    }
                }
                else {
                    long temp = -1*total;
                    String dept = String.valueOf(temp);
                    TextView_dept.setText("조에게\r\n" + dept + "원");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void colorChange(TextView textView_blue, TextView textView_gray){
        textView_blue.setBackground(getResources().getDrawable(R.drawable.btn_blue));
        textView_gray.setBackground(getResources().getDrawable(R.drawable.btn_gray));
    }

    public boolean isLong( String input ) {
        try {
            Long.parseLong( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

}
