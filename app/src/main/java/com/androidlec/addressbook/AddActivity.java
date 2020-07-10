package com.androidlec.addressbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidlec.addressbook.FTP_JHJ.ConnectFTP;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AddActivity extends AppCompatActivity {

    private TextView tvbtregister, tvbtcancle;
    private ImageView ivAddImage;
    private TextInputEditText et_name, et_phone, et_email, et_comment;

    String[] spinnerReNames;
    TypedArray spinnerImages;

    // 카메라 관련
    private static final int PERMISSION_REQUST_CODE = 100;
    private static final int IMAGE_PICK_CAMERA_CODE = 101;
    private static final int IMAGE_PICK_GALLERY_CODE = 102;

    private Uri image_uri;
    
    private int mWhich;

    // Register
    private int[] iv_tags = {R.id.add_iv_tagRed, R.id.add_iv_tagOrange, R.id.add_iv_tagYellow, R.id.add_iv_tagGreen, R.id.add_iv_tagBlue, R.id.add_iv_tagPurple, R.id.add_iv_tagGray};
    private  ArrayList<String> tagList;

    int tagClick = 0;

    private void init() {
        Resources res = getResources();

        tvbtregister = findViewById(R.id.tvbt_addAddress_update);
        tvbtcancle = findViewById(R.id.tvbt_addAddress_cancel);
        ivAddImage = findViewById(R.id.iv_addAddress_image);


        spinnerReNames = MainActivity.spinnerNames;
        spinnerImages = res.obtainTypedArray(R.array.tag_array);

        spinnerReNames[0] = "태그 없음";

        et_name = findViewById(R.id.et_addAddress_name);
        et_phone = findViewById(R.id.et_addAddress_phone);
        et_email = findViewById(R.id.et_addAddress_email);
        et_comment = findViewById(R.id.et_addAddress_cmt);

        for (int i = 0; i < iv_tags.length; i++) {
            final int finalI = i;
            findViewById(iv_tags[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.isSelected()){
                        v.setSelected(false);
                        tagClick--;
                    } else {
                        if (tagClick < 3) {
                            int pos = finalI + 1;
                            v.setSelected(true);
                            Toast.makeText(AddActivity.this, spinnerReNames[pos], Toast.LENGTH_SHORT).show();
                            tagClick++;
                        } else {
                            Toast.makeText(AddActivity.this, "선택은 3개만 가능합니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }

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
                    if(mWhich == 0){
                        pickFromCamera();
                    } else {
                        pickFromGallery();
                    }
                } else {
                    Toast.makeText(this, "권한 요청을 동의해 주세요.", Toast.LENGTH_SHORT).show();
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
        ivAddImage.setOnClickListener(onClickListener);

    }

    TextView.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_addAddress_image:
                    showImagePicDialog();
                    break;
                case R.id.tvbt_addAddress_update:
                    inputNewData();
                    break;
                case R.id.tvbt_addAddress_cancel :
                    finish();
                    break;
            }
        }
    };

    private void inputNewData() {
        String name = et_name.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String comment = et_comment.getText().toString().trim();
        String userId = LJH_data.getLoginId();
        int userSeq = LJH_data.getLoginSeqno();
        if(tagSelectedOK()){
            String tagListString = tagList.toString();
            tagListString = tagListString.substring(1, tagListString.length()-1); // 앞뒤 [] 제거
            tagListString = tagListString.replace(" ", ""); // 중간 공백 제거

            if(TextUtils.isEmpty(name)){
                Toast.makeText(this, "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            } else if(TextUtils.isEmpty(phone)){
                Toast.makeText(this, "전화번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            } else if(image_uri == null){
                uploadToDB(name, phone, email, comment, "", tagListString, userId, userSeq);
            } else {
                Log.e("Chance", "1");
                ConnectFTP mConnectFTP = new ConnectFTP(AddActivity.this, "192.168.0.82", "host", "qwer1234", 25, image_uri);
                String fileName = "";
                try {
                    fileName = mConnectFTP.execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                uploadToDB(name, phone, email, comment, fileName, tagListString, userId, userSeq);
            }
        }

    }

    private boolean tagSelectedOK() {

        tagList = new ArrayList<>();

        for (int i = 0; i < iv_tags.length; i++) {
            if (findViewById(iv_tags[i]).isSelected()){
                int pos = i + 1;
                tagList.add(String.valueOf(i+1));
            }
        }

        if (tagList.size() == 0){
            tagList.add("0");
            return true;
        } else if(tagList.size() > 3){
            Toast.makeText(this, "태그는 최대 3만 선택 가능합니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void uploadToDB(String name, String phone, String email, String comment, String fileName, String tags, String userId, int userSeq) {
        String urlAddr = "http://192.168.0.79:8080/test/csAddAddressBook.jsp?";

        urlAddr = urlAddr + "name=" + name + "&phone=" + phone + "&email=" + email + "&comment=" + comment + "&fileName=" + fileName + "&tags=" + tags + "&userId=" + userId + "&userSeq=" + userSeq;

        try {
            CSNetworkTask csNetworkTask = new CSNetworkTask(AddActivity.this, urlAddr);
            csNetworkTask.execute().get(); // doInBackground 의 리턴값
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showImagePicDialog() {
        String[] options = {"카메라에서 촬영", "갤러리에서 선택"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
        builder.setTitle("이미지 등록");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(checkPermission()){
                    if(which == 0){
                        pickFromCamera();
                        mWhich = 0;
                    } else {
                        pickFromGallery();
                        mWhich = 1;
                    }
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