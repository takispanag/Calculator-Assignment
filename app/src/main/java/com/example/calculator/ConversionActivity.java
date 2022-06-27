package com.example.calculator;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.List;

public class ConversionActivity extends AppCompatActivity {


    Button convertButton;
    ImageButton swapButton;
    EditText amountFrom, amountTo;
    List<IconSpinnerItem> iconSpinnerItems;
    PowerSpinnerView spinnerFrom, spinnerTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversion_activity);

        initializeViewVariables();
        createCurrencyConversionSpinners();

        Intent intent = getIntent();
        String amount = intent.getStringExtra("amount");

        //set amount from main activity intent
        //amountFrom.setText(amount);


        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapCurrency();
            }
        });

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromCurrency = spinnerFrom.getText().toString();
                String toCurrency = spinnerTo.getText().toString();
                String amount = amountFrom.getText().toString();

                getCurrencyConversion(fromCurrency, toCurrency, amount);
            }
        });
    }

    private void swapCurrency() {
        //get item from list using selection
        IconSpinnerItem fromSpinnerSelection = iconSpinnerItems
                .stream()
                .filter(iconSpinnerItem -> iconSpinnerItem.getText().equals(spinnerFrom.getText().toString()))
                .findFirst()
                .get();

        IconSpinnerItem toSpinnerSelection = iconSpinnerItems
                .stream()
                .filter(iconSpinnerItem -> iconSpinnerItem.getText().equals(spinnerTo.getText().toString()))
                .findFirst()
                .get();

        int fromSpinnerIndex = iconSpinnerItems.indexOf(fromSpinnerSelection);
        int toSpinnerIndex = iconSpinnerItems.indexOf(toSpinnerSelection);
        
        //swap currency
        spinnerFrom.selectItemByIndex(toSpinnerIndex);
        spinnerTo.selectItemByIndex(fromSpinnerIndex);


        //swap amount
        String temp = amountFrom.getText().toString();
        amountFrom.setText(amountTo.getText());
        amountTo.setText(temp);
    }

    private void createCurrencyConversionSpinners() {
        iconSpinnerItems = new ArrayList<>();
        iconSpinnerItems.add(new IconSpinnerItem("EUR",getDrawable(R.drawable.europe_flag)));
        iconSpinnerItems.add(new IconSpinnerItem("GBP",getDrawable(R.drawable.english_flag)));
        iconSpinnerItems.add(new IconSpinnerItem("USD",getDrawable(R.drawable.usa_flag)));
        iconSpinnerItems.add(new IconSpinnerItem("JPY",getDrawable(R.drawable.japan_flag)));
        iconSpinnerItems.add(new IconSpinnerItem("AUD",getDrawable(R.drawable.australian_flag)));
        iconSpinnerItems.add(new IconSpinnerItem("CAD",getDrawable(R.drawable.canadian_flag)));
        iconSpinnerItems.add(new IconSpinnerItem("CHF",getDrawable(R.drawable.swiss_flag)));
        iconSpinnerItems.add(new IconSpinnerItem("CNY",getDrawable(R.drawable.china_flag)));
        iconSpinnerItems.add(new IconSpinnerItem("HKD",getDrawable(R.drawable.hong_kong_flag)));
        iconSpinnerItems.add(new IconSpinnerItem("INR",getDrawable(R.drawable.indian_flag)));


        IconSpinnerAdapter iconSpinnerAdapterFrom = new IconSpinnerAdapter(spinnerFrom);
        spinnerFrom.setSpinnerAdapter(iconSpinnerAdapterFrom);
        spinnerFrom.setItems(iconSpinnerItems);
        spinnerFrom.selectItemByIndex(0);


        IconSpinnerAdapter iconSpinnerAdapterTo = new IconSpinnerAdapter(spinnerTo);
        spinnerTo.setSpinnerAdapter(iconSpinnerAdapterTo);
        spinnerTo.setItems(iconSpinnerItems);
        spinnerTo.selectItemByIndex(1);
    }

    private void initializeViewVariables() {
        convertButton = findViewById(R.id.convertButton);
        swapButton = findViewById(R.id.swapButton);
        amountFrom = findViewById(R.id.editTextFrom);
        amountTo = findViewById(R.id.editTextTo);
        spinnerFrom = findViewById(R.id.fromSelection);
        spinnerTo = findViewById(R.id.toSelection);
    }

    //converts from old selection to new selection the amount and sets the textview
    private void getCurrencyConversion(String fromCurrency, String toCurrency, String amount) {
        try {
            //amount has an operand so we dont convert
            /*if(amount.matches(".*[-\\+x÷%].*")){
                Toast.makeText(getApplicationContext(), "Operand found. Conversion can only happen on a number!", Toast.LENGTH_SHORT).show();
                return;
            }*/
            if( amount.isEmpty() || amount.length()==0 )
            {
                Toast.makeText(getApplicationContext(), "Amount to convert is empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            if(Double.parseDouble(amount)<=0){
                Toast.makeText(getApplicationContext(), "Please enter amount greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getApplicationContext(), "Converting..", Toast.LENGTH_SHORT).show();

            String convertedCurrency = new RetrieveCurrencyConversionTask()
                    .execute(fromCurrency, toCurrency, amount)
                    .get();

            //if empty response means conversion failed
            if(convertedCurrency.equals("null") || convertedCurrency.isEmpty()){
                Toast.makeText(getApplicationContext(), "Conversion failed!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getApplicationContext(), "Conversion completed!", Toast.LENGTH_SHORT).show();
            amountTo.setText(convertedCurrency);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Conversion failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
