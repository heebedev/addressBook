package com.androidlec.addressbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidlec.addressbook.adapter_sh.CustomSpinnerAdapter;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner_tags;
    String[] spinnerNames;
    int[] spinnerImages;
    int selected_tag_idx = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner_tags = findViewById(R.id.main_sp_taglist);

        spinnerNames = new String[]{"전체보기", "빨간색", "주황색", "노란색", "초록색", "파란색", "보라색", "회색"};
        spinnerImages = new int[]{R.drawable.ic_tag_black, R.drawable.ic_tag_red, R.drawable.ic_tag_orange, R.drawable.ic_tag_yellow, R.drawable.ic_tag_green, R.drawable.ic_tag_blue, R.drawable.ic_tag_purple, R.drawable.ic_tag_gray};


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(MainActivity.this, spinnerNames, spinnerImages);
        spinner_tags.setAdapter(customSpinnerAdapter);


        spinner_tags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected_tag_idx = spinner_tags.getSelectedItemPosition();
                Toast.makeText(MainActivity.this, spinnerNames[selected_tag_idx], Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}