package com.example.electricmeter;


import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {


    private EditText etConsumer, etConsuption;
    private Button btnCalculate;
    private TextView tvCost;
    private DatabseHelper mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etConsumer = findViewById(R.id.et_cNumber);//et_cNumber is defined in activity main
        etConsuption = findViewById(R.id.et_consuption);
        tvCost = findViewById(R.id.tv_cost);
        btnCalculate = findViewById(R.id.btnCalculate);

        //click listener for calculate button
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCost();
            }
        });

        mydb = new DatabseHelper(this);

    }

    private void calculateCost() {

        Double unitCost;
        Double totalCost;
        Double lastConsumed = 0.0;
        Double newlyConsumed;
        int id=0;


        //getting consumer number and consuption
        String cNumber, cosuption;
        cNumber = etConsumer.getText().toString();//etConsumer is declared as id in activity_main
        cosuption = etConsuption.getText().toString();

        if (!cNumber.equals("")) {


            //if values are null or not,if null->set 0,else get the value
            int cNumberValue = Integer.parseInt(cNumber);
            //condtional operator a?b:c(if condtion true then b else c)
            Double consumedValue = cosuption.equals("") ? 0.0 : Double.parseDouble(cosuption);


            //get lastconsumed data for cunsumerNumber from db
            Cursor cursor = mydb.getLastConsumedValue(cNumberValue);
            if (cursor.getCount() == 0) {
                mydb.insertdata(cNumberValue, consumedValue);
            } else {
                while (cursor.moveToNext()) {
                    lastConsumed = cursor.getDouble(cursor.getColumnIndex("lastConsumed"));
                    id=cursor.getInt(cursor.getColumnIndex("id"));
                }
                mydb.updateLastConsumerValue(id, consumedValue);
            }

            ///to avoid minus value
            newlyConsumed = consumedValue != 0 ? consumedValue - lastConsumed : 0;

            ///calculating the unit cost
            if (newlyConsumed < 0) {
                unitCost = 0.0;
            } else if (newlyConsumed <= 89) {
                unitCost = 0.3;
            } else if (newlyConsumed <= 200) {
                unitCost = 0.5;
            } else if (newlyConsumed <= 500) {
                unitCost = 0.7;
            } else {
                unitCost = 1.0;
            }

            totalCost = unitCost * newlyConsumed;
            totalCost=(double)Math.round((totalCost*100)/100);
           // tvCost.setText("aed:");
           String valueToSet=new DecimalFormat("##0.00").format(totalCost);
           tvCost.setText(new DecimalFormat("##0.00").format(totalCost));
          tvCost.setText("aed : " + valueToSet);
            //tvCost.setText("aed : " + totalCostt);
        } else {
            Toast.makeText(this, "Please enter consumer number", Toast.LENGTH_SHORT).show();
        }



    }
}
