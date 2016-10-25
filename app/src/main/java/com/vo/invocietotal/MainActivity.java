package com.vo.invocietotal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity
    implements OnEditorActionListener
    {
    // define instance variables
        private EditText subtotalEditText;
        private TextView discountPercentTextView;
        private TextView discountAmountTextView;
        private TextView totalTextView;
        private String subtotalString;
        private SharedPreferences savedValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ger reference to the widget
        subtotalEditText = (EditText) findViewById(R.id.subtotalEditText);
        discountPercentTextView = (TextView) findViewById(R.id.discountPercentTextView);
        discountAmountTextView = (TextView) findViewById(R.id.discountAmountTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);

        // set the listener
        subtotalEditText.setOnEditorActionListener(this);
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            // calling method
            calculateAndDisplay();

            // hides the soft keyboard
            return false;
        }

        private void calculateAndDisplay() {

            // get the subtotal input from user
            subtotalString = subtotalEditText.getText().toString();
            float subtotal;
            if (subtotalString.equals("")) {
                subtotal = 0;

            } else {
                subtotal = Float.parseFloat(subtotalString);
            }

            // get discount percent
            float discountPercent = 0;
            if (subtotal >=200) {
                discountPercent = .2f;
            } else if (subtotal >= 100){
                discountPercent = .1f;
            } else {
                discountPercent = 0;
            }

            // calculate discount
            float discountAmount = subtotal * discountPercent;
            float total = subtotal - discountAmount;

            // display data on the layout
            NumberFormat percent = NumberFormat.getPercentInstance();
            discountPercentTextView.setText(percent.format(discountPercent));

            NumberFormat Currency = NumberFormat.getCurrencyInstance();
            discountAmountTextView.setText(Currency.format(discountAmount));
            totalTextView.setText(Currency.format(total));


        }

        @Override
        protected void onPause() {



            SharedPreferences.Editor editor = savedValues.edit();
            editor.putString("subtotalString", subtotalString);
            editor.commit();

            super.onPause();
        }


        @Override
        protected void onResume() {
            super.onResume();

            // get subtotal
            subtotalString = savedValues.getString("subtotalString", "");
            subtotalEditText.setText(subtotalString);

            calculateAndDisplay();
        }

    }
