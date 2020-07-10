package com.androidlec.addressbook;

public class LJH_data {

    public static String loginId;
    public static int loginSeqno;

    public LJH_data() {
    }

    public static String getLoginId() {
        return loginId;
    }

    public static void setLoginId(String loginId) {
        LJH_data.loginId = loginId;
    }

    public static int getLoginSeqno() {
        return loginSeqno;
    }

    public static void setLoginSeqno(int loginSeqno) {
        LJH_data.loginSeqno = loginSeqno;
    }
}//--
