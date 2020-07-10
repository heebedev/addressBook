package com.androidlec.addressbook;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.androidlec.addressbook.adapter_sh.AddressListAdapter;
import com.androidlec.addressbook.dto_sh.Address;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CSUpdateNetworkTask extends AsyncTask<Integer, String, Void> {

    Context context;
    String mAddr;
    ProgressDialog progressDialog;
    Address result;

    public CSUpdateNetworkTask(Context context, String mAddr) {
        this.context = context;
        this.mAddr = mAddr;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("불러오는 중...");
        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        UpdateActivity.et_name.setText(result.getAname());
        UpdateActivity.et_phone.setText(result.getAphone());
        UpdateActivity.et_email.setText(result.getAemail());
        UpdateActivity.et_comment.setText(result.getAmemo());

        String url = AddressListAdapter.baseurl + result.getAimage();

        //이미지 보여주기
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().circleCrop())
                .placeholder(R.drawable.ic_outline_emptyimage)
                .into(UpdateActivity.ivAddImage);

        progressDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(mAddr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000); // 10 seconds.

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                inputStream = httpURLConnection.getInputStream(); // 이때 데이터 가져오는 겁니다.
                inputStreamReader = new InputStreamReader(inputStream); // 가져온거를 리더에 넣어야겠죠.
                bufferedReader = new BufferedReader(inputStreamReader); // 버퍼드리더에 넣어야합니다.

                while (true) {
                    String strline = bufferedReader.readLine();
                    if (strline == null) break; // 브레이크 만나면 와일문 빠져나간다.
                    stringBuffer.append(strline + "\n");
                } // 와일문 끝나면 다 가져왔다~.

                parser(stringBuffer.toString());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    private void parser(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("address_info"));

            JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
            int aseqno = Integer.parseInt(jsonObject1.getString("aSeqno"));
            String aname = jsonObject1.getString("aName");
            String aimage = jsonObject1.getString("aImage");
            String aphone = jsonObject1.getString("aPNum");
            String aemail = jsonObject1.getString("aEmail");
            String amemo = jsonObject1.getString("aMemo");
            String atag = jsonObject1.getString("aTag");

            result = new Address(aseqno, aname, aimage, aphone, aemail, amemo, atag);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
