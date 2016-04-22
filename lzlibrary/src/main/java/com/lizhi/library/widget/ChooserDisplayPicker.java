package com.lizhi.library.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.external.wheelview.WheelPopWindow;
import com.lizhi.library.R;

public class ChooserDisplayPicker extends TextView {

    private Context mContext;
    private WheelPopWindow.OnConfirmListener confirmListener;
    private WheelPopWindow.OnDismissLisener onDismissLisener;
    private OnShowListener onShowListener;
    private int hour;
    private int minute;
    private String[] data;
    private String title;
    private View maskView;
    private int popGravity = Gravity.BOTTOM;
    /**
     * 当前选择项的索引
     */
    private int index;
    private WheelPopWindow popupwindow;

    public ChooserDisplayPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public ChooserDisplayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setAttributes();
    }

    public ChooserDisplayPicker(Context context) {
        super(context);
        mContext = context;
        setAttributes();
    }

    private void setAttributes() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onShowListener != null) {
                    onShowListener.onShow();
                }
                showPopWindow(v, title, data, maskView);
            }
        });
    }

    public void setDialogTitle(String title) {
        this.title = title;
    }

    public void setData(String[] data) {
        this.data = data;
//        setText(data[0]);
    }

    public void setMaskView(View maskView) {
        this.maskView = maskView;
    }

    public void setPopGravity(int popGravity) {
        this.popGravity = popGravity;
    }

    public void showPopWindow(View parentView, String title, String[] data, final View maskView) {
        this.maskView = maskView;
        if (maskView != null)
            maskView.setVisibility(View.VISIBLE);
        popupwindow = new WheelPopWindow(mContext, data);
        popupwindow.setTitle(title);
        popupwindow.setTitleColor(getResources().getColor(R.color.font_black));
        popupwindow.setTitleSize(18);
        popupwindow.setValueTextSize(16);
        popupwindow.setValueTextColor(getResources().getColor(R.color.font_black));

        popupwindow.setOnDismissLisener(new WheelPopWindow.OnDismissLisener() {

            @Override
            public void onDismiss() {
                if (onDismissLisener != null) {
                    onDismissLisener.onDismiss();
                }
            }
        });

        popupwindow.setConfirmListener(new WheelPopWindow.OnConfirmListener() {
            @Override
            public void onConfirm(int index, String value) {
                setText(value);
                if (maskView != null)
                    maskView.setVisibility(View.GONE);

                if (confirmListener != null) {
                    confirmListener.onConfirm(index, value);
                }
            }
        });
//        popupwindow.setAnimationStyle(R.style.AnimationPopup);
        popupwindow.showAtLocation(((Activity) getContext()).getWindow().getDecorView(), popGravity | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void setConfirmListener(WheelPopWindow.OnConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    public void setOnDismissLisener(WheelPopWindow.OnDismissLisener onDismissLisener) {
        this.onDismissLisener = onDismissLisener;
    }

    public void setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
    }

    public interface OnShowListener {
        public void onShow();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        popupwindow.setCurrentIndex(index);
    }
}