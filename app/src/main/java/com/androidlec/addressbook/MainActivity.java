package com.androidlec.addressbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidlec.addressbook.adapter_sh.CustomSpinnerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;


public class MainActivity extends AppCompatActivity {

    private ActionBar actionBar;

    private void init() {
        actionBar = getSupportActionBar();
    }

    private Spinner spinner_tags;
    String[] spinnerNames;
    int[] spinnerImages;
    int selected_tag_idx = 0;

    FloatingActionButton fladdBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        spinner_tags = findViewById(R.id.main_sp_taglist);
        fladdBtn = findViewById(R.id.main_fab_add);

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


        // 초기화
        init();

        // 액션바
        actionBar.setTitle("내 주소록");
        actionBar.setElevation(0);

        //floating Button
        fladdBtn.setOnClickListener(onClickListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(onQueryTextListener);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_optionTag:
                startActivity(new Intent(MainActivity.this, TagOptionDialog.class));
                break;
            case R.id.menu_logout:
                Toast.makeText(this, "menu_logout", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            // submit클릭 후 액션
            Log.v("Chance", "onQueryTextSubmit : " + query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // 텍스트 입력할 때마다 액션
            Log.v("Chance", "onQueryTextChange : " + newText);
            return false;
        }
    };


    FloatingActionButton.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        }
    };

}