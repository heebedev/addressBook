package com.androidlec.addressbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidlec.addressbook.adapter_sh.CustomSpinnerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class AddActivity extends AppCompatActivity {

    private TextView tvbtregister, tvbtcancle;
    private ImageView ivAddImage;

    private Spinner spinner_tags;
    String[] spinnerNames;
    TypedArray spinnerImages;
    int selected_tag_idx = 0;

    // 카메라 관련
    private static final int PERMISSION_REQUST_CODE = 100;
    private static final int IMAGE_PICK_CAMERA_CODE = 101;
    private static final int IMAGE_PICK_GALLERY_CODE = 102;

    private Uri image_uri;



    private void init() {
        Resources res = getResources();

        tvbtregister = findViewById(R.id.tvbt_addAddress_register);
        tvbtcancle = findViewById(R.id.tvbt_addAddress_cancel);
        ivAddImage = findViewById(R.id.iv_addAddress_image);

        spinner_tags = findViewById(R.id.add_sp_taglist);
        spinnerNames = res.getStringArray(R.array.addaddresstaglist);
        spinnerImages = res.obtainTypedArray(R.array.tag_array);

    }

    public boolean checkPermission() {
        String temp = "";
        //카메라 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.CAMERA + " ";
        }
        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }
        if (!TextUtils.isEmpty(temp)) {
            // 권한 요청 다이얼로그
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),PERMISSION_REQUST_CODE);
        } else {
            // 모두 허용 상태
            return true;
        }
        return false;
    }

    //권한에 대한 응답이 있을때 작동하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //권한을 허용 했을 경우
        if (requestCode == PERMISSION_REQUST_CODE) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 동의
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        // 키보드 화면 가림막기
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        init();

        tvbtregister.setOnClickListener(onClickListener);
        tvbtcancle.setOnClickListener(onClickListener);


        spinner_tags = findViewById(R.id.add_sp_taglist);

        ivAddImage.setOnClickListener(onClickListener);


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
                case R.id.iv_addAddress_image:
                    showImagePicDialog();
                    break;
                case R.id.tvbt_addAddress_register :
                    break;
                case R.id.tvbt_addAddress_cancel :
                    finish();
                    break;
            }
        }
    };

    private void showImagePicDialog() {
        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(checkPermission()){
                    if(which == 0){
                        pickFromCamera();
                    } else {
                        pickFromGallery();
                    }
                } else {
                    Toast.makeText(AddActivity.this, "권한 요청을 동의해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                assert data != null;
                image_uri = data.getData();
            }
            Glide.with(this)
                    .load(image_uri.toString())
                    .apply(new RequestOptions().circleCrop())
                    .placeholder(R.drawable.ic_outline_emptyimage)
                    .into(ivAddImage);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

}