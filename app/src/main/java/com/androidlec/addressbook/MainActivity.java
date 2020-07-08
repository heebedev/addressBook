package com.androidlec.addressbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private ActionBar actionBar;

    private void init() {
        actionBar = getSupportActionBar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 초기화
        init();

        // 액션바
        actionBar.setTitle("내 주소록");

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
        switch (item.getItemId()){
            case R.id.menu_search:
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_optionTag:
                Toast.makeText(this, "menu_optionTag", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_logout:
                Toast.makeText(this, "menu_logout", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
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

}