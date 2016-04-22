package com.external.album.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.external.album.utils.ImageManager;
import com.lizhi.library.R;
import com.lizhi.library.widget.LZImageView;

import java.util.ArrayList;

public class GridImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> dataList;
    private DisplayMetrics dm;
    private OnDelImageListener onDelImageListener;

    public GridImageAdapter(Context c, ArrayList<String> dataList) {

        mContext = c;
        this.dataList = dataList;
        dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.image_grid_item, null);
            viewHolder = new ViewHolder();
            viewHolder.delIcon = (ImageView) convertView.findViewById(R.id.del_ic);
            viewHolder.imageView = (LZImageView) convertView.findViewById(R.id.image_view);
            viewHolder.delIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onDelImageListener != null)
                    {
                        onDelImageListener.onDel(position);
                    }
                }
            });
            convertView.setTag(viewHolder);


        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String path = dataList.get(position);
        if(position == dataList.size())
        {
            viewHolder.imageView.setImageResource(R.drawable.pic_add_ic);
        }
        else if(path.contains("http") || path.contains("file:///"))
        {
            viewHolder.imageView.displayImage(path);
        }
        else {
            ImageManager.from(mContext).displayImage(viewHolder.imageView, path, R.drawable.default_im, 100, 100);
        }
        return convertView;
    }

    public int dipToPx(int dip) {
        return (int) (dip * dm.density + 0.5f);
    }

    class ViewHolder
    {
        public ImageView delIcon;
        public LZImageView imageView;
        public ViewHolder() {
        }
    }

    public void setOnDelImageListener(OnDelImageListener onDelImageListener) {
        this.onDelImageListener = onDelImageListener;
    }

    public interface  OnDelImageListener
    {
        public void onDel(int postion);
    }

}
