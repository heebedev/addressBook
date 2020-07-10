package com.androidlec.addressbook.network_sh;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.androidlec.addressbook.dto_sh.Address;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class NetworkTask extends AsyncTask<Integer, String, Object> {

    Context context;
    String mAddr;
    ProgressDialog progressDialog;
    ArrayList<Address> addlist;

    public NetworkTask(Context context, String mAddr) {
        this.context = context;
        this.mAddr = mAddr;
        this.addlist = new ArrayList<Address>();
        //log.v("status", mAddr);
        //log.v("status", "NetworkTask start");
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Dialog");
        progressDialog.setMessage("Get......");
        progressDialog.show();

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progressDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Object doInBackground(Integer... integers) {

        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        //log.v("status", "doinBackground start");

        try {
            URL url = new URL(mAddr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);

            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                // 잇풋 스트림으로 가져온 것을 인풋스트림 리더로 가져온다.
                //버퍼드 리더가 인풋스트림리더가 포장한 것을 임시보관함에 휙 올린다.
                bufferedReader = new BufferedReader(inputStreamReader);

                while (true) {
                    String strline = bufferedReader.readLine();
                    if(strline == null) break;
                    stringBuffer.append(strline + "\n");
                }

                Parser(stringBuffer.toString());

            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(bufferedReader != null) bufferedReader.close();
                if(inputStreamReader != null) inputStreamReader.close();
                if(inputStream != null) inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return addlist;
    }

    private void Parser(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("address_info"));
            //students_info 에 속해있는 Array를 가져와라.

            for(int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                int aseqno = Integer.parseInt(jsonObject1.getString("aSeqno"));
                String aname = jsonObject1.getString("aName");
                String aimage = jsonObject1.getString("aImage");
                String aphone = jsonObject1.getString("aPNum");
                String aemail = jsonObject1.getString("aEmail");
                String amemo = jsonObject1.getString("aMemo");
                String atag = jsonObject1.getString("aTag");


                Address data = new Address(aseqno, aname, aimage, aphone, aemail, amemo, atag);
                addlist.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }







}
