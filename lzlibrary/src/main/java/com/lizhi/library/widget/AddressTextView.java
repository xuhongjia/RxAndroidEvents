package com.lizhi.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by jacob on 15/6/17.
 */
public class AddressTextView extends TextView implements View.OnClickListener{

    private Context mContext;
    private String address;
    private String province;
    private String city;
    private String district;

    private OnAddressClickListener onAddressClickListener;

    public AddressTextView(Context context) {
        super(context);
        mContext = context;
        setOnClickListener(this);
    }

    public AddressTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOnClickListener(this);
    }

    public AddressTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive())
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if(onAddressClickListener != null)
        {
            onAddressClickListener.onClick();
        }
        AddressSelectPopWindow popupwindow = new AddressSelectPopWindow(mContext, null);
        popupwindow.showAtLocation(this, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupwindow.setButtonClickListener(new AddressSelectPopWindow.OnButtonClickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(String provinceP, String cityP, String areaP) {
                setText(provinceP + cityP + areaP);
                province = provinceP;
                city = cityP;
                district = areaP;
                address = province + city + areaP;

            }
        });

        popupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public interface OnAddressClickListener
    {
        public void onClick();
    }

    public void setOnAddressClickListener(OnAddressClickListener onAddressClickListener) {
        this.onAddressClickListener = onAddressClickListener;
    }
}
