package com.androidlec.addressbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.androidlec.addressbook.adapter_sh.AddressListAdapter;
import com.androidlec.addressbook.adapter_sh.CustomSpinnerAdapter;
import com.androidlec.addressbook.dto_sh.Address;
import com.androidlec.addressbook.network_sh.NetworkTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    String TAG = "Log Chk : ";
    LJH_data ljh_data; // 아이디값 불러오는 클래스.

    private ActionBar actionBar;

    //스피너
    private Spinner spinner_tags;
    public static String[] spinnerNames;

    int selected_tag_idx = 0;


    //리스트뷰
    private ArrayList<Address> data = null;
    private AddressListAdapter adapter = null;
    private ListView listView = null;

    //datajsp
    String centIP, urlAddr;


    //플로팅버튼
    FloatingActionButton fladdBtn;

    public static TypedArray tagImages;

    TextView pre_cmt;


    private void init() {
        Resources res = getResources();

        actionBar = getSupportActionBar();

        spinnerNames = res.getStringArray(R.array.maintaglist);
        tagImages = res.obtainTypedArray(R.array.tag_array);
        spinner_tags = findViewById(R.id.main_sp_taglist);

        listView = findViewById(R.id.main_lv_addresslist);

        fladdBtn = findViewById(R.id.main_fab_add);

        centIP = "192.168.0.138";

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 초기화
        init();


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(MainActivity.this, spinnerNames, tagImages);
        spinner_tags.setAdapter(customSpinnerAdapter);

        spinner_tags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_tag_idx = spinner_tags.getSelectedItemPosition();

                if (selected_tag_idx == 0) {
                    urlAddr = "http://" + centIP + ":8080/test/address_list_select.jsp?userid=" + ljh_data.loginId;
                } else {
                    urlAddr = "http://" + centIP + ":8080/test/address_list_selectedspinner.jsp?userid="+ ljh_data.loginId +"&aTag=" + selected_tag_idx;
                }
                connectGetData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //리스트뷰
        data = new ArrayList<>();
        Spinner_List();

        //플로팅버튼
        fladdBtn.setOnClickListener(onClickListener);

        // 액션바
        actionBar.setTitle("내 주소록");
        actionBar.setElevation(0);

        listView.setOnItemClickListener(lvOnItemClickListener);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Spinner_List();
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
                ljh_data.setLoginId("");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void Spinner_List() {
        spinner_tags = findViewById(R.id.main_sp_taglist);
        fladdBtn = findViewById(R.id.main_fab_add);

        // 태그 불러오기.
        onTagList();

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(MainActivity.this, spinnerNames, tagImages);
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

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            urlAddr = "http://" + centIP + ":8080/test/address_list_search.jsp?userid="+ ljh_data.loginId +"&search=" + query;
            //Log.v("status", urlAddr);
            connectGetData();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            //안함
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


    private void connectGetData() {
        try {
            NetworkTask networkTask = new NetworkTask(MainActivity.this, urlAddr);
            Object obj = networkTask.execute().get();
            data = (ArrayList<Address>) obj;
            adapter = new AddressListAdapter(MainActivity.this, R.layout.address_list_layout, data);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }  // connectGetData


    private void onTagList() {
        Log.v(TAG, "onTagList()()");

        urlAddr = "http://192.168.0.178:8080/Test/tagList.jsp?";
        urlAddr = urlAddr + "id=" + ljh_data.getLoginId();

        connectTagListData();
    }


    // 태그 리스트 불러오기.
    private void connectTagListData() {
        Log.v(TAG, "connectTagListData()");

        try {
            LJH_TagNetwork tagListNetworkTask = new LJH_TagNetwork(MainActivity.this, urlAddr);
            Object obj = tagListNetworkTask.execute().get();
            ArrayList<String> tNames = (ArrayList<String>) obj; // cast.

            spinnerNames[1] = tNames.get(0);
            spinnerNames[2] = tNames.get(1);
            spinnerNames[3] = tNames.get(2);
            spinnerNames[4] = tNames.get(3);
            spinnerNames[5] = tNames.get(4);
            spinnerNames[6] = tNames.get(5);
            spinnerNames[7] = tNames.get(6);

            Log.v(TAG, "tag 대체 완료.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }  // connectTagListData

    ListView.OnItemClickListener lvOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView cmt = view.findViewById(R.id.tv_addresslist_cmt);;

            if (pre_cmt == null) {
                pre_cmt = cmt;
            } else {
                pre_cmt.setVisibility(View.GONE);
                pre_cmt = cmt;
            }

            cmt.setVisibility(View.VISIBLE);

        }
    };
}//----

