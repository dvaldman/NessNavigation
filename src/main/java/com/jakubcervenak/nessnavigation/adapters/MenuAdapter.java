package com.jakubcervenak.nessnavigation.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jakubcervenak.nessnavigation.R;



public class MenuAdapter extends BaseAdapter {

    private Activity mActivity = null;
    private LayoutInflater mLayoutInflater = null;
    private String[] titles;

    public MenuAdapter(Activity activity) {
        mActivity = activity;
        mLayoutInflater = activity.getLayoutInflater();
        titles = mActivity.getResources().getStringArray(R.array.drawer_menu_titles);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int i) {
        return titles[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View row = mLayoutInflater.inflate(R.layout.main_menu_row, viewGroup, false);
        TextView menuTextView = (TextView) row.findViewById(R.id.main_menu_title);
        menuTextView.setText(titles[position]);

//        Drawable img = mActivity.getResources().getDrawable(R.drawable.);
//        menuTextView.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
        return row;
    }
}
