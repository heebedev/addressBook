package com.androidlec.addressbook.FTP_JHJ;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectFTP extends AsyncTask<Integer, String, String> {

    public FTPClient mFTPClient = null;

    Context context;
    String host;
    String username;
    String password;
    int port;
    Uri file;

    ProgressDialog progressDialog;

    public ConnectFTP(Context context, String host, String username, String password, int port, Uri file) {
        this.context = context;
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        this.file = file;
        mFTPClient = new FTPClient();
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
    }

    @Override
    protected String doInBackground(Integer... integers) {
        String formatDate = "";

        // FTP 접속 체크
        boolean status;
        // FTP 접속 시
        if (status = ftpConnect(host, username, password, port)) {
            String currentPath = ftpGetDirectory() + "imgs";

            // 현재시간을 msec 으로 구한다.
            long now = System.currentTimeMillis();
            // 현재시간을 date 변수에 저장한다.
            Date date = new Date(now);
            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            // nowDate 변수에 값을 저장한다.
            formatDate = sdfNow.format(date);

            // 파일 업로드시
            if (ftpUploadFile(file, formatDate+".jpg", currentPath)) {
                Log.v("Chance", "Success");
            }
        }

        if (status) {
            ftpDisconnect();
        }

        return formatDate+".jpg";
    }

    public boolean ftpConnect(String host, String username, String password, int port) {
        boolean result = false;

        try {
            mFTPClient.connect(host, port);
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                result = mFTPClient.login(username, password);
                mFTPClient.enterLocalPassiveMode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean ftpDisconnect() {
        boolean result = false;

        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String ftpGetDirectory() {
        String directory = null;
        try {
            directory = mFTPClient.printWorkingDirectory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory;
    }

    public boolean ftpChangeDirectory(String directory) {
        try {
            mFTPClient.changeWorkingDirectory(directory);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //                                     저장할 파일 이름        저장할 FTP 폴더 경
    public boolean ftpUploadFile(Uri file, String desFileName, String desDriectroy) {
        boolean result = false;
        try {
            InputStream fis = context.getContentResolver().openInputStream(file);
            if (ftpChangeDirectory(desDriectroy)) {
                // FTP File 타입 설정
                mFTPClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                result = mFTPClient.storeFile(desFileName, fis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

