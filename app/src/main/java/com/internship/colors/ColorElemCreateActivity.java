package com.internship.colors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ColorElemCreateActivity extends AppCompatActivity {
    private static final String NUMBER_INDEX_EXTRA = "index";
    private static final String SELECTED_RADIO_POSITION = "selected";

    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_elem_create);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Intent extraDataIntent = getIntent();
        int elemNumber = extraDataIntent.getIntExtra(NUMBER_INDEX_EXTRA, 0);
        getSupportActionBar().setTitle(this.getString(R.string.list_item_text_pattern, elemNumber));

        int selectedPosition = -1;
        if(savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(SELECTED_RADIO_POSITION);
        }


        radioGroup = findViewById(R.id.select_color_radio_group);
        for (ColorListElem.ItemColorState i : ColorListElem.ItemColorState.values()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setBackgroundColor(ContextCompat.getColor(this, i.getColorId()));
            radioButton.setText(getString(i.getColorName()).toUpperCase());
            radioButton.setId(i.getColorId());
            radioGroup.addView(radioButton);
        }

        if(selectedPosition > -1) {
            radioGroup.check(selectedPosition);
        }


        Button button = findViewById(R.id.add_color_elem_btn);
        button.setOnClickListener(v -> {
            Intent intent = new Intent();
            int checkedColor = radioGroup.getCheckedRadioButtonId();
            intent.putExtra("color", checkedColor);
            setResult(1, intent);
            finish();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SELECTED_RADIO_POSITION, radioGroup.getCheckedRadioButtonId());
        super.onSaveInstanceState(outState);
    }
}
