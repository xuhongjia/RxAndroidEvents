package com.external.album.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.external.album.adapter.AlbumGridViewAdapter;
import com.external.album.utils.ImageManager;
import com.lizhi.library.R;
import com.lizhi.library.widget.LZImageView;
import com.lizhi.library.widget.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AlbumActivity extends FragmentActivity {

    private GridView gridView;
    private HashMap<String, ImageView> hashMap = new HashMap<String, ImageView>();
    private ArrayList<String> selectedDataList = new ArrayList<String>();
    private String cameraDir = "DCIM";
    private ProgressBar progressBar;
    private AlbumGridViewAdapter gridImageAdapter;
    private LinearLayout selectedImageLayout;
    private HorizontalScrollView scrollview;
    View mListViewGroup;
    ListView mListView;
    LayoutInflater mInflater;
    private Context mContext;
    private TitleBar titleBar;

    public static DisplayImageOptions optionsImage = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(R.drawable.default_im)
            .showImageForEmptyUri(R.drawable.default_im)
            .showImageOnFail(R.drawable.default_im)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();
    private int mMaxPick = 5;

    public static class ImageInfo implements Serializable {
        public String path;

        public ImageInfo(String path) {
            this.path = path;
        }
    }

    LinkedHashMap<String, ArrayList<ImageInfo>> mFolders = new LinkedHashMap();
    ArrayList<String> mFoldersData = new ArrayList();

    final String allPhotos = "所有图片";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_album);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        selectedDataList = (ArrayList<String>) bundle.getSerializable("dataList");

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        selectedDataList = (ArrayList<String>) bundle.getSerializable("dataList");

    }

    private void init() {

        titleBar = (TitleBar) getSupportFragmentManager().findFragmentById(R.id.title);
        titleBar.setTitle("完成(" + selectedDataList.size() + "/5)");
        titleBar.setRightText("确定");
        titleBar.setLeftText("相册");
        titleBar.setLetfTextViewOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListViewGroup.getVisibility() == View.VISIBLE) {
                    hideFolderList();
                } else {
                    showFolderList();
                }
            }
        });
        titleBar.setRightTextViewOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                // intent.putArrayListExtra("dataList", dataList);
                bundle.putStringArrayList("dataList", selectedDataList);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        gridView = (GridView) findViewById(R.id.myGrid);
        gridView.setAdapter(mPhotoAdapter);
        selectedImageLayout = (LinearLayout) findViewById(R.id.selected_image_layout);
        scrollview = (HorizontalScrollView) findViewById(R.id.scrollview);

        initSelectImage();

        mInflater = getLayoutInflater();
        mListView = (ListView) findViewById(R.id.list_view);
        mListViewGroup = findViewById(R.id.listViewParent);
        mListViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListViewGroup.getVisibility() == View.VISIBLE) {
                    hideFolderList();
                } else {
                    showFolderList();
                }
            }
        });

        initFolder();

    }

    private void initSelectImage() {
        if (selectedDataList == null)
            return;
        for (final String path : selectedDataList) {
            LZImageView imageView = (LZImageView) LayoutInflater.from(AlbumActivity.this).inflate(R.layout.choose_imageview, selectedImageLayout, false);
            selectedImageLayout.addView(imageView);
            hashMap.put(path, imageView);
            if (path.contains("http")) {
                imageView.displayImage(path);
            } else {
                ImageManager.from(AlbumActivity.this).displayImage(imageView, path, R.drawable.pic_add_ic, 100, 100);
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(path, imageView, optionsImage);
//                imageView.displayImage(path);
            }
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    removePath(path);
                    mPhotoAdapter.notifyDataSetChanged();
                }
            });
        }
        titleBar.setTitle("完成(" + selectedDataList.size() + "/5)");
    }

    private boolean removePath(String path) {
        if (hashMap.containsKey(path)) {
            selectedImageLayout.removeView(hashMap.get(path));
            hashMap.remove(path);
            removeOneData(selectedDataList, path);
            titleBar.setTitle("完成(" + selectedDataList.size() + "/5)");
            return true;
        } else {
            return false;
        }
    }

    private void removeOneData(ArrayList<String> arrayList, String s) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equals(s)) {
                arrayList.remove(i);
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        System.gc();
        finish();
//    	super.onBackPressed();
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
//    	ImageManager2.from(AlbumActivity.this).recycle(dataList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void showFolderList() {
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.listview_up);
//        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.listview_fade_in);
//
//        mListView.startAnimation(animation);
//        mListViewGroup.startAnimation(fadeIn);
        mListViewGroup.setVisibility(View.VISIBLE);
    }

    private void hideFolderList() {
        mListViewGroup.setVisibility(View.GONE);
//        Animation animation = AnimationUtils.loadAnimation(AlbumActivity.this, R.anim.listview_down);
//        Animation fadeOut = AnimationUtils.loadAnimation(AlbumActivity.this, R.anim.listview_fade_out);
//        fadeOut.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                mListViewGroup.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//
//        mListView.startAnimation(animation);
//        mListViewGroup.startAnimation(fadeOut);
    }

    private void initFolder() {

        String[] projection = {MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME};
        String selection = "";
        String[] selectionArgs = null;
        Cursor mImageExternalCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, MediaStore.MediaColumns.DATE_ADDED + " DESC");

        ArrayList<ImageInfo> allPhoto = new ArrayList<ImageInfo>();
        mFoldersData.add(allPhotos);

        while (mImageExternalCursor.moveToNext()) {
            String s0 = mImageExternalCursor.getString(0);
            String s1 = mImageExternalCursor.getString(1);
            String s2 = mImageExternalCursor.getString(2);

            String s = String.format("%s,%s,%s", s0, s1, s2);
                s1 = "file://" + s1;
            ImageInfo imageInfo = new ImageInfo(s1);

            ArrayList<ImageInfo> value = mFolders.get(s2);
            if (value == null) {
                value = new ArrayList<ImageInfo>();
                mFolders.put(s2, value);
                mFoldersData.add(s2);
            }
            allPhoto.add(imageInfo);

            value.add(imageInfo);
        }
        mFolders.put(allPhotos, allPhoto);
        mListView.setAdapter(mFoldAdapter);
        mListView.setOnItemClickListener(mOnItemClick);

        mPhotoAdapter.setData(mFolders.get(cameraDir));
        mPhotoAdapter.notifyDataSetChanged();
    }

    ListView.OnItemClickListener mOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String folderName = mFoldersData.get((int) id);
            mPhotoAdapter.setData(mFolders.get(folderName));
            mPhotoAdapter.notifyDataSetChanged();
            mFoldAdapter.notifyDataSetChanged();
            hideFolderList();
        }
    };

    BaseAdapter mFoldAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mFoldersData.size();
        }

        @Override
        public Object getItem(int position) {
            return mFoldersData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.photopick_list_item, parent, false);
                holder = new ViewHolder();
                holder.foldIcon = (LZImageView) convertView.findViewById(R.id.foldIcon);
                holder.foldName = (TextView) convertView.findViewById(R.id.foldName);
                holder.photoCount = (TextView) convertView.findViewById(R.id.photoCount);
                holder.check = convertView.findViewById(R.id.check);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String name = (String) getItem(position);
            ArrayList<ImageInfo> imageInfos = mFolders.get(name);
            String uri = imageInfos.get(0).path;
            int count = imageInfos.size();

            holder.foldName.setText(name);
            holder.photoCount.setText(String.format("%d张", count));
            holder.foldIcon.displayImage(uri);
//            ImageLoader imageLoader = ImageLoader.getInstance();
//
// (uri, holder.foldIcon, optionsImage);
            if (titleBar.getLeftText().equals(name)) {
                holder.check.setVisibility(View.VISIBLE);
            } else {
                holder.check.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }

    };

    static class ViewHolder {
        LZImageView foldIcon;
        TextView foldName;
        TextView photoCount;
        View check;
    }


    GridAdapter mPhotoAdapter = new GridAdapter();

    static class GridViewCheckTag {
        View iconFore;
        String path = "";

        GridViewCheckTag(View iconFore) {
            this.iconFore = iconFore;
        }
    }

    static class GridViewHolder {
        LZImageView icon;
        ImageView iconFore;
        CheckBox check;
    }

    class GridAdapter extends BaseAdapter {

        ArrayList<ImageInfo> mData = new ArrayList<ImageInfo>();
        private ImageInfo data;

        public void setData(ArrayList<ImageInfo> data) {
            mData = data;
        }

        public ArrayList<ImageInfo> getData() {
            return mData;
        }

        @Override
        public int getCount() {
            if(mData != null)
            return mData.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        private final int TYPE_CAMERA = 0;
        private final int TYPE_PHOTO = 1;

        @Override
        public int getItemViewType(int position) {
            return TYPE_PHOTO;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            float sScale = getResources().getDisplayMetrics().density;
            int sWidthPix = getResources().getDisplayMetrics().widthPixels;
            int sWidthDp = (int) (sWidthPix / sScale);
            int width = sWidthPix / 3;
            GridViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.photopick_gridlist_item, parent, false);
                convertView.getLayoutParams().height = width;

                holder = new GridViewHolder();
                holder.icon = (LZImageView) convertView.findViewById(R.id.icon);
                holder.iconFore = (ImageView) convertView.findViewById(R.id.iconFore);
                holder.check = (CheckBox) convertView.findViewById(R.id.check);
                GridViewCheckTag checkTag = new GridViewCheckTag(holder.iconFore);
                holder.check.setTag(checkTag);
                holder.check.setOnClickListener(mClickItem);
                convertView.setTag(holder);
            } else {
                holder = (GridViewHolder) convertView.getTag();
            }

            data = (ImageInfo) getItem(position);
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(data.path, holder.icon, optionsImage);
//            holder.icon.displayImage(data.path);

            ((GridViewCheckTag) holder.check.getTag()).path = data.path;

            boolean picked = isPicked(data.path);
            holder.check.setChecked(picked);
            holder.iconFore.setVisibility(picked ? View.VISIBLE : View.INVISIBLE);

            return convertView;

        }

        View.OnClickListener mClickItem = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridViewCheckTag tag = (GridViewCheckTag) v.getTag();
                if (((CheckBox) v).isChecked()) {
                    if (selectedDataList.size() >= mMaxPick) {
                        ((CheckBox) v).setChecked(false);
                        String s = String.format("最多只能选择%d张", mMaxPick);
                        Toast.makeText(AlbumActivity.this, s, Toast.LENGTH_LONG).show();
                        return;
                    }

                    addPicked(tag.path);
                    tag.iconFore.setVisibility(View.VISIBLE);
                } else {
                    removePicked(tag.path);
                    tag.iconFore.setVisibility(View.INVISIBLE);
                }
                mFoldAdapter.notifyDataSetChanged();

//                changeBottom(data.path);

                updatePickCount();
            }
        };
    }

    private void updatePickCount() {

        String format = "完成(%d/%d)";
        titleBar.setTitle(String.format(format, selectedDataList.size(), mMaxPick));

    }

    private void changeBottom(final String path) {
        if (!hashMap.containsKey(path)) {
            LZImageView imageView = (LZImageView) LayoutInflater.from(AlbumActivity.this).inflate(R.layout.choose_imageview, selectedImageLayout, false);
            selectedImageLayout.addView(imageView);
            imageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int off = selectedImageLayout.getMeasuredWidth() - scrollview.getWidth();
                    if (off > 0) {
                        scrollview.smoothScrollTo(off, 0);
                    }
                }
            }, 100);
            hashMap.put(path, imageView);
            selectedDataList.add(path);
//            ImageLoader imageLoader = ImageLoader.getInstance();
//            imageLoader.displayImage(path, imageView, optionsImage);
            imageView.displayImage(path);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removePath(path);
                    mPhotoAdapter.notifyDataSetChanged();
                }
            });
            titleBar.setTitle("完成(" + selectedDataList.size() + "/5)");
        } else {
//            removePath(path);
        }
    }

    private void addPicked(String path) {
        if (!isPicked(path)) {
            selectedDataList.add(path);
        }
    }

    private boolean isPicked(String path) {
        for (String item : selectedDataList) {
            if (item.equals(path)) {
                return true;
            }
        }

        return false;
    }

    private void removePicked(String path) {
        for (int i = 0; i < selectedDataList.size(); ++i) {
            if (selectedDataList.get(i).equals(path)) {
                selectedDataList.remove(i);
                return;
            }
        }
    }
}
