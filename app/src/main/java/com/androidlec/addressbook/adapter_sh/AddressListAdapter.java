package com.androidlec.addressbook.adapter_sh;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.androidlec.addressbook.AddActivity;
import com.androidlec.addressbook.MainActivity;
import com.androidlec.addressbook.R;
import com.androidlec.addressbook.dto_sh.Address;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class AddressListAdapter extends BaseAdapter {

    private Context mContext = null;
    private int layout = 0;
    private ArrayList<Address> data = null;
    private LayoutInflater inflater = null;

    public ImageView ivpfimage;
    public TextView tvname;
    public TextView tvphone;
    public TextView tvemail;
    public ImageView ivpftag1;
    public ImageView ivpftag2;
    public ImageView ivpftag3;
    public TextView tvcmt;
    

    public static String baseurl = "http://192.168.0.82:8080/Hello/imgs/";

    public AddressListAdapter(Context mContext, int layout, ArrayList<Address> data) {
        this.mContext = mContext;
        this.layout = layout;
        this.data = data;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).getAseqno();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void getCmtVisible(int position) {

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);

        }

        ivpfimage = convertView.findViewById(R.id.iv_addresslist_pfimage);
        tvname = convertView.findViewById(R.id.tv_addresslist_name);
        tvphone = convertView.findViewById(R.id.tv_addresslist_email);
        tvemail = convertView.findViewById(R.id.tv_addresslist_phone);
        ivpftag1 = convertView.findViewById(R.id.iv_addresslist_tag1);
        ImageView ivpftag2 = convertView.findViewById(R.id.iv_addresslist_tag2);
        ImageView ivpftag3 = convertView.findViewById(R.id.iv_addresslist_tag3);
        TextView tvcmt = convertView.findViewById(R.id.tv_addresslist_cmt);


        tvname.setText(data.get(position).getAname());
        tvphone.setText(data.get(position).getAphone());
        tvemail.setText(data.get(position).getAemail());
        tvcmt.setText(data.get(position).getAmemo());

        //URL url = new URL(baseurl + data.get(position).getAimage());
        String url = baseurl + data.get(position).getAimage();

        //이미지 보여주기
        Glide.with(mContext)
                .load(url)
                .apply(new RequestOptions().circleCrop())
                .placeholder(R.drawable.ic_outline_emptyimage)
                .into(ivpfimage);

        //Tag color 보여주기
        Resources res = null;
        String[] tags = data.get(position).getAtag().split(",");
        TypedArray tagimages = MainActivity.tagImages;

        switch(tags.length) {
            case 0 :
                ivpftag1.setImageResource(tagimages.getResourceId(0, 0));
                ivpftag2.setImageResource(tagimages.getResourceId(0, 0));
                ivpftag3.setImageResource(tagimages.getResourceId(0, 0));
                break;
            case 1 :
                ivpftag1.setImageResource(tagimages.getResourceId(Integer.parseInt(tags[0]), 0));
                ivpftag2.setImageResource(tagimages.getResourceId(0, 0));
                ivpftag3.setImageResource(tagimages.getResourceId(0, 0));
                break;
            case 2 :
                ivpftag1.setImageResource(tagimages.getResourceId(Integer.parseInt(tags[0]), 0));
                ivpftag2.setImageResource(tagimages.getResourceId(Integer.parseInt(tags[1]), 0));
                ivpftag3.setImageResource(tagimages.getResourceId(0, 0));
                break;
            case 3 :
                ivpftag1.setImageResource(tagimages.getResourceId(Integer.parseInt(tags[0]), 0));
                ivpftag2.setImageResource(tagimages.getResourceId(Integer.parseInt(tags[1]), 0));
                ivpftag3.setImageResource(tagimages.getResourceId(Integer.parseInt(tags[2]), 0));
                break;
        }

        return convertView;
    }

}
