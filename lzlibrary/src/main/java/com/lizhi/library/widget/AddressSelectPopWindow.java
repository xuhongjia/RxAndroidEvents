package com.lizhi.library.widget;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;


import com.external.wheelview.OnWheelChangedListener;
import com.external.wheelview.WheelView;
import com.external.wheelview.adapters.ArrayWheelAdapter;
import com.lizhi.library.R;
import com.lizhi.library.model.CityModel;
import com.lizhi.library.model.DistrictModel;
import com.lizhi.library.model.ProvinceModel;
import com.lizhi.library.utils.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class AddressSelectPopWindow extends PopupWindow implements OnWheelChangedListener {
    private View mainview;

    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;

    private Button cancelBtn;
    private Button confirmBtn;

    private AdressResultCallBack addressResultCallBack;
    private OnButtonClickListener buttonClickListener;

    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    public String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    public String mCurrentCityName;
    /**
     * 当前区的名称
     */
    public String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    public String mCurrentZipCode = "";

    /**
     * 解析省市区的XML数据
     */

    private Context mContext;

    public AddressSelectPopWindow(Context context, OnClickListener itemclick) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainview = inflater.inflate(R.layout.address_select_popwindow, null);
        mainview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setContentView(mainview);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
//        this.setAnimationStyle(R.style.AnimBottom);

        setUpViews();
        setUpListener();
        setUpData();

    }

    private void setUpViews() {
        mViewProvince = (WheelView) mainview.findViewById(R.id.id_province);
        mViewCity = (WheelView) mainview.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) mainview.findViewById(R.id.id_district);

        mViewProvince.setVisibleItems(5); // Number of items
        mViewProvince.setWheelBackground(R.drawable.wheel_bg);
        mViewProvince.setWheelForeground(R.drawable.wheel_val);
        mViewProvince.setDrawShadows(false);
        mViewProvince.setCurrentItem(4);

        mViewCity.setVisibleItems(5); // Number of items
        mViewCity.setWheelBackground(R.drawable.wheel_bg);
        mViewCity.setWheelForeground(R.drawable.wheel_val);
        mViewCity.setDrawShadows(false);
        mViewCity.setCurrentItem(0);

        mViewDistrict.setVisibleItems(5); // Number of items
        mViewDistrict.setWheelBackground(R.drawable.wheel_bg);
        mViewDistrict.setWheelForeground(R.drawable.wheel_val);
        mViewDistrict.setDrawShadows(false);
        mViewDistrict.setCurrentItem(0);

        cancelBtn = (Button) mainview.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener != null) {
                    buttonClickListener.onCancel();
                    dismiss();
                }
            }
        });

        confirmBtn = (Button) mainview.findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener != null) {
                    buttonClickListener.onConfirm(mCurrentProviceName, mCurrentCityName, mCurrentDistrictName);
                    dismiss();
                }
            }
        });

    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        ArrayWheelAdapter provinceAdapter = new ArrayWheelAdapter<String>(mContext, mProvinceDatas);
//        provinceAdapter.setItemTextResource(R.id.address_item_text);
        provinceAdapter.setTextColor(Color.parseColor("#3a3a3a"));
        mViewProvince.setViewAdapter(provinceAdapter);
        // 设置可见条目数量
        mViewProvince.setVisibleItems(5);
        mViewCity.setVisibleItems(5);
        mViewDistrict.setVisibleItems(5);
        updateCities();
//        updateAreas();

        mViewProvince.setCurrentItem(4);
        mViewCity.setCurrentItem(0);
        mViewDistrict.setCurrentItem(0);
    }

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = mContext.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }

        if (addressResultCallBack != null) {
            addressResultCallBack.onAddressResult(mCurrentProviceName, mCurrentCityName, mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);
        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];


        if (areas == null) {
            areas = new String[]{""};
        }
        ArrayWheelAdapter districtAdaper = new ArrayWheelAdapter<String>(mContext, areas);
        districtAdaper.setTextColor(Color.parseColor("#3a3a3a"));
        mViewDistrict.setViewAdapter(districtAdaper);
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        ArrayWheelAdapter cityAdaper = new ArrayWheelAdapter<String>(mContext, cities);
        cityAdaper.setTextColor(Color.parseColor("#3a3a3a"));
        mViewCity.setViewAdapter(cityAdaper);
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    public void setAddressResultCallBack(AdressResultCallBack addressResultCallBack) {
        this.addressResultCallBack = addressResultCallBack;
    }

    public void setButtonClickListener(OnButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

    public interface AdressResultCallBack {
        void onAddressResult(String province, String city, String area);
    }

    public interface OnButtonClickListener {
        void onCancel();

        void onConfirm(String province, String city, String area);

    }
}
