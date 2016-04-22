package com.external.wheelview;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.external.wheelview.adapters.ArrayWheelAdapter;
import com.lizhi.library.R;

/**
 * Created by jacob on 15/5/6.
 */
public class WheelPopWindow extends PopupWindow  implements OnWheelChangedListener{

    private final View mainview;
    private final Context mContext;
    private WheelView wheelView;
    private TextView titleView;

    private Button cancelBtn;
    private Button confirmBtn;

    private OnConfirmListener confirmListener;
    private OnDismissLisener onDismissLisener;

    private String[] data;

    private String selectedString;
    private int selectedIndex;
    private ArrayWheelAdapter adapter;

    public WheelPopWindow(Context context,String[] data) {

        this.data = data;
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        mainview = inflater.inflate(R.layout.pop_window, null);
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        this.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
        this.setContentView(mainview);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());

        initView();
    }

    private void initView()
    {
        titleView = (TextView) mainview.findViewById(R.id.title);
        cancelBtn = (Button) mainview.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDismissLisener != null)
                {
                    onDismissLisener.onDismiss();
                }
                dismiss();
            }
        });

        confirmBtn  = (Button) mainview.findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmListener != null) {
                    confirmListener.onConfirm(selectedIndex, selectedString);
                }

                dismiss();
            }
        });

        wheelView = (WheelView) mainview.findViewById(R.id.wheel);
        setWheelViewAtrr();

        setWheelViewData(this.data);
    }

    private  void setWheelViewAtrr()
    {
        wheelView.setVisibleItems(5); // Number of items
        wheelView.setWheelBackground(R.drawable.wheel_bg);
        wheelView.setWheelForeground(R.drawable.wheel_val);
        wheelView.setDrawShadows(false);
        wheelView.setCurrentItem(0);
        wheelView.addChangingListener(this);

        adapter = new ArrayWheelAdapter<String>(mContext, this.data);


        selectedIndex = 0;
        if(data != null)
        selectedString = data[0];

    }

    public void setCurrentIndex(int index)
    {
        wheelView.setCurrentItem(index);
    }

    public void setWheelViewData(String[] data)
    {
        this.data = data;
        if(adapter == null)
        adapter = new ArrayWheelAdapter<String>(mContext, this.data);
//        provinceAdapter.setItemTextResource(R.id.address_item_text);
        wheelView.setViewAdapter(adapter);
    }

    public void setTitle(String title)
    {
        titleView.setText(title);
    }

    public void setTitleColor(int color)
    {
        titleView.setTextColor(color);
    }

    public void setTitleSize(int size)
    {
        titleView.setTextSize(size);
    }

    public void setConfirmListener(OnConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    public void setOnDismissLisener(OnDismissLisener onDismissLisener) {
        this.onDismissLisener = onDismissLisener;
    }

    public void setValueTextSize(int size)
    {
        adapter.setTextSize(size);
    }

    public void setValueTextColor(int color)
    {
        adapter.setTextColor(color);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public String getSelectedString() {
        return selectedString;
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {

        selectedIndex = newValue;
        selectedString = data[newValue];
    }

    public interface  OnConfirmListener
    {
        public void onConfirm(int index, String value);
    }

    public interface OnDismissLisener
    {
        public void onDismiss();
    }
}
