package com.androidlec.addressbook;


import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidlec.addressbook.adapter_sh.AddressListAdapter;
import com.androidlec.addressbook.adapter_sh.CustomSpinnerAdapter;
import com.androidlec.addressbook.dto_sh.Address;
import com.androidlec.addressbook.network_sh.NetworkTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    LJH_data ljh_data; // 아이디값 불러오는 클래스.
    ArrayList<String> tNames;

    private ActionBar actionBar;

    //스피너
    private Spinner spinner_tags;
    public static String[] spinnerNames;

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

    // 길게 눌러서 클릭할때 같은 아이템 칸에서 클릭하면 둘다 이벤트가 발동되는것을 막기위함.
    int click = 0;




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

        //리스트뷰
        data = new ArrayList<>();
        Spinner_List();

        //플로팅버튼
        fladdBtn.setOnClickListener(onClickListener);

        // 액션바
        actionBar.setTitle("내 주소록");
        actionBar.setElevation(0);

        // listview 클릭 이벤트
        listView.setOnItemClickListener(lvOnItemClickListener);
        listView.setOnItemLongClickListener(lvOnItemLongClickListener);


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
//            case R.id.menu_instruction:
//                new AlertDialog.Builder(MainActivity.this)
//                        .setTitle("사용방법")
//                        .setMessage("")
//                        .setCancelable(false)
//                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                EditText et_1 = linearLayout.findViewById(R.id.et_1);
//                                EditText et_2 = linearLayout.findViewById(R.id.et_2);
//
//                                int num1 = Integer.parseInt(et_1.getText().toString());
//                                int num2 = Integer.parseInt(et_2.getText().toString());
//                                int result = num1 + num2;
//
//                                textView.setText(num1 + " + " + num2 + " = " +result);
//                            }
//                        })
//                        .setNegativeButton("취소", null)
//                        .show();
//                break;
            case R.id.menu_optionTag:
                startActivity(new Intent(MainActivity.this, TagOptionDialog.class));
                break;
            case R.id.menu_setting:
                startActivity(new Intent(MainActivity.this, LJH_SettingActivity.class));
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

        spinnerNames[0] = "전체보기";

        // 태그 불러오기.
        onTagList();

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(MainActivity.this, spinnerNames, tagImages);
        spinner_tags.setAdapter(customSpinnerAdapter);


        spinner_tags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, spinnerNames[position], Toast.LENGTH_SHORT).show();


                if (position == 0) {
                    urlAddr = "http://" + centIP + ":8080/test/address_list_select.jsp?userid=" + ljh_data.loginId;
                } else {

                    urlAddr = "http://" + centIP + ":8080/test/address_list_selectedspinner.jsp?userid=" + ljh_data.loginId + "&aTag=" + position;

                }
                connectGetData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            urlAddr = "http://" + centIP + ":8080/test/address_list_search.jsp?userid=" + ljh_data.loginId + "&search=" + query;
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
        urlAddr = "http://192.168.0.178:8080/Test/tagList.jsp?";
        urlAddr = urlAddr + "id=" + ljh_data.getLoginId();

        connectTagListData();
    }


    // 태그 리스트 불러오기.
    private void connectTagListData() {
        try {
            LJH_TagNetwork tagListNetworkTask = new LJH_TagNetwork(MainActivity.this, urlAddr);
            Object obj = tagListNetworkTask.execute().get();
            tNames = new ArrayList<String>();
            tNames.clear();
            tNames = (ArrayList<String>) obj; // cast.

            spinnerNames[1] = tNames.get(0);
            spinnerNames[2] = tNames.get(1);
            spinnerNames[3] = tNames.get(2);
            spinnerNames[4] = tNames.get(3);
            spinnerNames[5] = tNames.get(4);
            spinnerNames[6] = tNames.get(5);
            spinnerNames[7] = tNames.get(6);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }  // connectTagListData

    ListView.OnItemClickListener lvOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (click == 0 ) {
                TextView cmt = view.findViewById(R.id.tv_addresslist_cmt);

                if (pre_cmt == null) {
                    pre_cmt = cmt;
                } else {
                    pre_cmt.setVisibility(View.GONE);
                    pre_cmt = cmt;
                }

                cmt.setVisibility(View.VISIBLE);
            }
        }
    };

    ListView.OnItemLongClickListener lvOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            click = 1;

            //                                              inflate XML 를 부르는 메소드
            final LinearLayout linear = (LinearLayout) View.inflate(MainActivity.this, R.layout.activity_add , null);

            // -------------------------------------------------------------------------
            // CustomDialogLayou 안에있는 텍스트뷰 값 넣어주기. -------------------------------
            TextView dialogName = linear.findViewById(R.id.et_addAddress_name);
            TextView dialogPhone = linear.findViewById(R.id.et_addAddress_phone);
            TextView dialogEmail = linear.findViewById(R.id.et_addAddress_email);
            TextView dialogCmt = linear.findViewById(R.id.et_addAddress_cmt);

            // -------------------------------------------------------------------------
            // -------------------------------------------------------------------------

            // -------------------------------------------------------------------------
            // ---------- Dialog 만들기 --------------------------------------------------
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("데이터를 삭제하시겠습니까?")
                    .setIcon(R.mipmap.ic_launcher)
                    .setView(linear)
                    .setCancelable(false)   // 배경 눌러도 안닫힘
                    .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            click = 0;
                        }
                    })
                    .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            click = 0;
                        }
                    })
                    .show();
            // -------------------------------------------------------------------------
            // ---------- Dialog 만들기 끝 ------------------------------------------------

            return true;
        }
    };

}//----

