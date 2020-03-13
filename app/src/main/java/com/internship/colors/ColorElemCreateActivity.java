package com.internship.colors;

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

        RadioGroup radioGroup = findViewById(R.id.select_color_radio_group);
        for (ColorListElem.ItemColorState i : ColorListElem.ItemColorState.values()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setBackgroundColor(ContextCompat.getColor(this, i.getColorId()));
            radioButton.setText(i.toString());
            radioGroup.addView(radioButton);
        }

        Button button = findViewById(R.id.add_color_elem_btn);
        button.setOnClickListener(v -> {
            Intent intent = new Intent();
            int checkedColor = ColorListElem.ItemColorState.getColorByPosition((int) radioGroup.getCheckedRadioButtonId() - 1).getColorId();
            intent.putExtra("color", checkedColor);
            setResult(1, intent);
            finish();
        });


    }

}
