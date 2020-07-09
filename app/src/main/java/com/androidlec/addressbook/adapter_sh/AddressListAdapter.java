package com.androidlec.addressbook.adapter_sh;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidlec.addressbook.MainActivity;
import com.androidlec.addressbook.R;
import com.androidlec.addressbook.dto_sh.Address;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.net.URL;
import java.util.ArrayList;

public class AddressListAdapter extends BaseAdapter {

    private Context mContext = null;
    private int layout = 0;
    private ArrayList<Address> data = null;
    private LayoutInflater inflater = null;
    

    private String baseurl = "http://192.168.0.82:8080/Hello/imgs/";

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

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
        }

        ImageView ivpfimage = convertView.findViewById(R.id.iv_addresslist_pfimage);
        TextView tvname = convertView.findViewById(R.id.tv_addresslist_name);
        TextView tvphone = convertView.findViewById(R.id.tv_addresslist_email);
        TextView tvemail = convertView.findViewById(R.id.tv_addresslist_phone);
        ImageView ivpftag1 = convertView.findViewById(R.id.iv_addresslist_tag1);
        ImageView ivpftag2 = convertView.findViewById(R.id.iv_addresslist_tag2);
        ImageView ivpftag3 = convertView.findViewById(R.id.iv_addresslist_tag3);


        tvname.setText(data.get(position).getAname());
        tvphone.setText(data.get(position).getAphone());
        tvemail.setText(data.get(position).getAemail());

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
        Log.v("status", Integer.toString(tags.length));
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
