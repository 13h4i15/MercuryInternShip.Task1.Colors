package com.internship.colors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ColorElemCreateActivity extends AppCompatActivity {
    private static final String SELECTED_RADIO_POSITION = "selected";


    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_element_create);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Intent extraDataIntent = getIntent();
        int elementNumber = extraDataIntent.getIntExtra(Constants.NUMBER_INDEX_EXTRA, 0);
        getSupportActionBar().setTitle(getString(R.string.list_item_text_pattern, elementNumber));

        radioGroup = findViewById(R.id.select_color_radio_group);
        for (ColorListElement.ElementColorState i : ColorListElement.ElementColorState.values()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setBackgroundColor(ContextCompat.getColor(this, i.getColorId()));
            radioButton.setText(getString(i.getColorName()).toUpperCase());
            radioButton.setId(i.getColorId());
            radioGroup.addView(radioButton);
        }

        Button button = findViewById(R.id.add_color_element_button);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> button.setVisibility(View.VISIBLE));

        if (savedInstanceState != null) {
            int selectedPosition = savedInstanceState.getInt(SELECTED_RADIO_POSITION);
            if (selectedPosition > -1) {
                radioGroup.check(selectedPosition);
            }
        }

        button.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(Constants.SELECTED_COLOR_EXTRA, radioGroup.getCheckedRadioButtonId());
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SELECTED_RADIO_POSITION, radioGroup.getCheckedRadioButtonId());
        super.onSaveInstanceState(outState);
    }
}
