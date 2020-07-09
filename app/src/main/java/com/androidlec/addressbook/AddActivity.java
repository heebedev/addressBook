package com.androidlec.addressbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidlec.addressbook.adapter_sh.CustomSpinnerAdapter;

public class AddActivity extends AppCompatActivity {

    TextView tvbtregister, tvbtcancle, tvbtaddimage;

    private Spinner spinner_tags;
    String[] spinnerNames;
    TypedArray spinnerImages;
    int selected_tag_idx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Resources res = getResources();

        tvbtregister = findViewById(R.id.tvbt_addAddress_register);
        tvbtcancle = findViewById(R.id.tvbt_addAddress_cancle);
        tvbtaddimage = findViewById(R.id.tvbt_addAddress_addimage);

        tvbtaddimage.setOnClickListener(onClickListener);
        tvbtregister.setOnClickListener(onClickListener);
        tvbtcancle.setOnClickListener(onClickListener);

        spinner_tags = findViewById(R.id.add_sp_taglist);

        spinnerNames = res.getStringArray(R.array.addaddresstaglist);
        spinnerImages = res.obtainTypedArray(R.array.tag_array);


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(AddActivity.this, spinnerNames, spinnerImages);
        spinner_tags.setAdapter(customSpinnerAdapter);


        spinner_tags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected_tag_idx = spinner_tags.getSelectedItemPosition();
                Toast.makeText(AddActivity.this, spinnerNames[selected_tag_idx], Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }




    TextView.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvbt_addAddress_addimage :
                    break;
                case R.id.tvbt_addAddress_register :
                    break;
                case R.id.tvbt_addAddress_cancle :
                    finish();
                    break;
            }
        }
    };

}