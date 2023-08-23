package com.example.swasthya_2o;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DietPlan extends AppCompatActivity {

    private TextView breakfast, lunch, dinner, snacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan);

        breakfast = findViewById(R.id.breakfast);
        lunch = findViewById(R.id.lunch);
        dinner = findViewById(R.id.dinner);
        snacks = findViewById(R.id.snacks);

        breakfast.setText("Cucumber Detox Water(1 glass) \nMixed Nuts(25grams)");
        lunch.setText("Skimmed Milk Panner(100gram)\n Mixed Vegetable Salad \n Dal(1 bowl) ,Chapatti");
        snacks.setText("Cut Fruits(1 cup) Buttermilk(1 glass)\n Tea with less Sugar and Milk(1 teacup)");
        dinner.setText("Mixed Vegetable Salad(1 bowl) ,\n Dal(1 bowl) , chapati\n Oats Porridge in Skimmed Milk");

    }
}
