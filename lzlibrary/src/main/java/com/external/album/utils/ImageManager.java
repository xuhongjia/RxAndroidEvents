package com.external.album.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Í¼Æ¬¼ÓÔØÀà
 *
 * @author ÔÂÔÂÄñ
 */
public class ImageManager {

	private static ImageManager imageManager;
	public LruCache<String, Bitmap> mMemoryCache;
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 20; // 10MB
	private static final String DISK_CACHE_SUBDIR = "thumbnails";
	public DiskLruCache mDiskCache;
	private static Application myapp;

	/** Í¼Æ¬¼ÓÔØ¶ÓÁÐ£¬ºó½øÏÈ³ö */
	private Stack<ImageRef> mImageQueue = new Stack<ImageRef>();

	/** Í¼Æ¬ÇëÇó¶ÓÁÐ£¬ÏÈ½øÏÈ³ö£¬ÓÃÓÚ´æ·ÅÒÑ·¢ËÍµÄÇëÇó¡£ */
	private Queue<ImageRef> mRequestQueue = new LinkedList<ImageRef>();

	/** Í¼Æ¬¼ÓÔØÏß³ÌÏûÏ¢´¦ÀíÆ÷ */
	private Handler mImageLoaderHandler;

	/** Í¼Æ¬¼ÓÔØÏß³ÌÊÇ·ñ¾ÍÐ÷ */
	private boolean mImageLoaderIdle = true;

	/** ÇëÇóÍ¼Æ¬ */
	private static final int MSG_REQUEST = 1;
	/** Í¼Æ¬¼ÓÔØÍê³É */
	private static final int MSG_REPLY = 2;
	/** ÖÐÖ¹Í¼Æ¬¼ÓÔØÏß³Ì */
	private static final int MSG_STOP = 3;
	/** Èç¹ûÍ¼Æ¬ÊÇ´ÓÍøÂç¼ÓÔØ£¬ÔòÓ¦ÓÃ½¥ÏÔ¶¯»­£¬Èç¹û´Ó»º´æ¶Á³öÔò²»Ó¦ÓÃ¶¯»­ */
	private boolean isFromNet = true;

	/**
	 * »ñÈ¡µ¥Àý£¬Ö»ÄÜÔÚUIÏß³ÌÖÐÊ¹ÓÃ¡£
	 *
	 * @param context
	 * @return
	 */
	public static ImageManager from(Context context) {

		// Èç¹û²»ÔÚuiÏß³ÌÖÐ£¬ÔòÅ×³öÒì³£
		if (Looper.myLooper() != Looper.getMainLooper()) {
			throw new RuntimeException("Cannot instantiate outside UI thread.");
		}

		if (myapp == null) {
			myapp = (Application) context.getApplicationContext();
		}

		if (imageManager == null) {
			imageManager = new ImageManager(myapp);
		}

		return imageManager;
	}

	/**
	 * Ë½ÓÐ¹¹Ôìº¯Êý£¬±£Ö¤µ¥ÀýÄ£Ê½
	 *
	 * @param context
	 */
	private ImageManager(Context context) {
		int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		memClass = memClass > 32 ? 32 : memClass;
		// Ê¹ÓÃ¿ÉÓÃÄÚ´æµÄ1/8×÷ÎªÍ¼Æ¬»º´æ
		final int cacheSize = 1024 * 1024 * memClass / 8;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}

		};

		File cacheDir = DiskLruCache
				.getDiskCacheDir(context, DISK_CACHE_SUBDIR);
		mDiskCache = DiskLruCache.openCache(context, cacheDir, DISK_CACHE_SIZE);

	}

	/**
	 * ´æ·ÅÍ¼Æ¬ÐÅÏ¢
	 */
	class ImageRef {

		/** Í¼Æ¬¶ÔÓ¦ImageView¿Ø¼þ */
		ImageView imageView;
		/** Í¼Æ¬URLµØÖ· */
		String url;
		/** Í¼Æ¬»º´æÂ·¾¶ */
		String filePath;
		/** Ä¬ÈÏÍ¼×ÊÔ´ID */
		int resId;
		int width = 0;
		int height = 0;

		/**
		 * ¹¹Ôìº¯Êý
		 *
		 * @param imageView
		 * @param url
		 * @param resId
		 * @param filePath
		 */
		ImageRef(ImageView imageView, String url, String filePath, int resId) {
			this.imageView = imageView;
			this.url = url;
			this.filePath = filePath;
			this.resId = resId;
		}

		ImageRef(ImageView imageView, String url, String filePath, int resId,
				int width, int height) {
			this.imageView = imageView;
			this.url = url;
			this.filePath = filePath;
			this.resId = resId;
			this.width = width;
			this.height = height;
		}

	}

	/**
	 * ÏÔÊ¾Í¼Æ¬
	 *
	 * @param imageView
	 * @param url
	 * @param resId
	 */
	public void displayImage(ImageView imageView, String url, int resId) {
		if (imageView == null) {
			return;
		}
		if (imageView.getTag() != null
				&& imageView.getTag().toString().equals(url)) {
			return;
		}
		if (resId >= 0) {
			if (imageView.getBackground() == null) {
				imageView.setBackgroundResource(resId);
			}
			imageView.setImageDrawable(null);

		}
		if (url == null || url.equals("")) {
			return;
		}

		// Ìí¼Óurl tag
		imageView.setTag(url);

		// ¶ÁÈ¡map»º´æ
		Bitmap bitmap = mMemoryCache.get(url);
		if (bitmap != null) {
			setImageBitmap(imageView, bitmap, false);
			return;
		}

		// Éú³ÉÎÄ¼þÃû
		String filePath = urlToFilePath(url);
		if (filePath == null) {
			return;
		}

		queueImage(new ImageRef(imageView, url, filePath, resId));
	}

	/**
	 * ÏÔÊ¾Í¼Æ¬¹Ì¶¨´óÐ¡Í¼Æ¬µÄËõÂÔÍ¼£¬Ò»°ãÓÃÓÚÏÔÊ¾ÁÐ±íµÄÍ¼Æ¬£¬¿ÉÒÔ´ó´ó¼õÐ¡ÄÚ´æÊ¹ÓÃ
	 *
	 * @param imageView ¼ÓÔØÍ¼Æ¬µÄ¿Ø¼þ
	 * @param url ¼ÓÔØµØÖ·
	 * @param resId Ä¬ÈÏÍ¼Æ¬
	 * @param width Ö¸¶¨¿í¶È
	 * @param height Ö¸¶¨¸ß¶È
	 */
	public void displayImage(ImageView imageView, String url, int resId,
			int width, int height) {
		if (imageView == null) {
			return;
		}
		if (resId >= 0) {

			if (imageView.getBackground() == null) {
				imageView.setBackgroundResource(resId);
			}
			imageView.setImageDrawable(null);

		}
		if (url == null || url.equals("")) {
			return;
		}

		// Ìí¼Óurl tag
		imageView.setTag(url);
		// ¶ÁÈ¡map»º´æ
		Bitmap bitmap = mMemoryCache.get(url + width + height);
		if (bitmap != null) {
			setImageBitmap(imageView, bitmap, false);
			return;
		}

		// Éú³ÉÎÄ¼þÃû
		String filePath = urlToFilePath(url);
		if (filePath == null) {
			return;
		}

		queueImage(new ImageRef(imageView, url, filePath, resId, width, height));
	}

	/**
	 * Èë¶Ó£¬ºó½øÏÈ³ö
	 *
	 * @param imageRef
	 */
	public void queueImage(ImageRef imageRef) {

		// É¾³ýÒÑÓÐImageView
		Iterator<ImageRef> iterator = mImageQueue.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().imageView == imageRef.imageView) {
				iterator.remove();
			}
		}

		// Ìí¼ÓÇëÇó
		mImageQueue.push(imageRef);
		sendRequest();
	}

	/**
	 * ·¢ËÍÇëÇó
	 */
	private void sendRequest() {

		// ¿ªÆôÍ¼Æ¬¼ÓÔØÏß³Ì
		if (mImageLoaderHandler == null) {
			HandlerThread imageLoader = new HandlerThread("image_loader");
			imageLoader.start();
			mImageLoaderHandler = new ImageLoaderHandler(
					imageLoader.getLooper());
		}

		// ·¢ËÍÇëÇó
		if (mImageLoaderIdle && mImageQueue.size() > 0) {
			ImageRef imageRef = mImageQueue.pop();
			Message message = mImageLoaderHandler.obtainMessage(MSG_REQUEST,
					imageRef);
			mImageLoaderHandler.sendMessage(message);
			mImageLoaderIdle = false;
			mRequestQueue.add(imageRef);
		}
	}

	/**
	 * Í¼Æ¬¼ÓÔØÏß³Ì
	 */
	class ImageLoaderHandler extends Handler {

		public ImageLoaderHandler(Looper looper) {
			super(looper);
		}

		public void handleMessage(Message msg) {
			if (msg == null)
				return;

			switch (msg.what) {

			case MSG_REQUEST: // ÊÕµ½ÇëÇó
				Bitmap bitmap = null;
				Bitmap tBitmap = null;
				if (msg.obj != null && msg.obj instanceof ImageRef) {

					ImageRef imageRef = (ImageRef) msg.obj;
					String url = imageRef.url;
					if (url == null)
						return;
					// Èç¹û±¾µØurl¼´¶ÁÈ¡sdÏà²áÍ¼Æ¬£¬ÔòÖ±½Ó¶ÁÈ¡£¬²»ÓÃ¾­¹ýDiskCache
					if (url.toLowerCase().contains("dcim")) {

						tBitmap = null;
						BitmapFactory.Options opt = new BitmapFactory.Options();
						opt.inSampleSize = 1;
						opt.inJustDecodeBounds = true;
						BitmapFactory.decodeFile(url, opt);
						int bitmapSize = opt.outHeight * opt.outWidth * 4;
						opt.inSampleSize = bitmapSize / (1000 * 2000);
						opt.inJustDecodeBounds = false;
						tBitmap = BitmapFactory.decodeFile(url, opt);
						if (imageRef.width != 0 && imageRef.height != 0) {
							bitmap = ThumbnailUtils.extractThumbnail(tBitmap,
									imageRef.width, imageRef.height,
									ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
							isFromNet = true;
						} else {
							bitmap = tBitmap;
							tBitmap = null;
						}

					} else
						bitmap = mDiskCache.get(url);

					if (bitmap != null) {
						// ToolUtil.log("´Ódisk»º´æ¶ÁÈ¡");
						// Ð´Èëmap»º´æ
						if (imageRef.width != 0 && imageRef.height != 0) {
							if (mMemoryCache.get(url + imageRef.width
									+ imageRef.height) == null)
								mMemoryCache.put(url + imageRef.width
										+ imageRef.height, bitmap);
						} else {
							if (mMemoryCache.get(url) == null)
								mMemoryCache.put(url, bitmap);
						}

					} else {
						try {
							byte[] data = loadByteArrayFromNetwork(url);

							if (data != null) {

								BitmapFactory.Options opt = new BitmapFactory.Options();
								opt.inSampleSize = 1;

								opt.inJustDecodeBounds = true;
								BitmapFactory.decodeByteArray(data, 0,
										data.length, opt);
								int bitmapSize = opt.outHeight * opt.outWidth
										* 4;// pixels*3 if it's RGB and pixels*4
											// if it's ARGB
								if (bitmapSize > 1000 * 1200)
									opt.inSampleSize = 2;
								opt.inJustDecodeBounds = false;
								tBitmap = BitmapFactory.decodeByteArray(data,
										0, data.length, opt);
								if (imageRef.width != 0 && imageRef.height != 0) {
									bitmap = ThumbnailUtils
											.extractThumbnail(
													tBitmap,
													imageRef.width,
													imageRef.height,
													ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
								} else {
									bitmap = tBitmap;
									tBitmap = null;
								}

								if (bitmap != null && url != null) {
									// Ð´ÈëSD¿¨
									if (imageRef.width != 0
											&& imageRef.height != 0) {
										mDiskCache.put(url + imageRef.width
												+ imageRef.height, bitmap);
										mMemoryCache.put(url + imageRef.width
												+ imageRef.height, bitmap);
									} else {
										mDiskCache.put(url, bitmap);
										mMemoryCache.put(url, bitmap);
									}
									isFromNet = true;
								}
							}
						} catch (OutOfMemoryError e) {
						}

					}

				}

				if (mImageManagerHandler != null) {
					Message message = mImageManagerHandler.obtainMessage(
							MSG_REPLY, bitmap);
					mImageManagerHandler.sendMessage(message);
				}
				break;

			case MSG_STOP: // ÊÕµ½ÖÕÖ¹Ö¸Áî
				Looper.myLooper().quit();
				break;

			}
		}
	}

	/** UIÏß³ÌÏûÏ¢´¦ÀíÆ÷ */
	private Handler mImageManagerHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg != null) {
				switch (msg.what) {

				case MSG_REPLY: // ÊÕµ½Ó¦´ð

					do {
						ImageRef imageRef = mRequestQueue.remove();

						if (imageRef == null)
							break;

						if (imageRef.imageView == null
								|| imageRef.imageView.getTag() == null
								|| imageRef.url == null)
							break;

						if (!(msg.obj instanceof Bitmap) || msg.obj == null) {
							break;
						}
						Bitmap bitmap = (Bitmap) msg.obj;

						// ·ÇÍ¬Ò»ImageView
						if (!(imageRef.url).equals((String) imageRef.imageView
								.getTag())) {
							break;
						}

						setImageBitmap(imageRef.imageView, bitmap, isFromNet);
						isFromNet = false;

					} while (false);

					break;
				}
			}
			// ÉèÖÃÏÐÖÃ±êÖ¾
			mImageLoaderIdle = true;

			// Èô·þÎñÎ´¹Ø±Õ£¬Ôò·¢ËÍÏÂÒ»¸öÇëÇó¡£
			if (mImageLoaderHandler != null) {
				sendRequest();
			}
		}
	};

	/**
	 * Ìí¼ÓÍ¼Æ¬ÏÔÊ¾½¥ÏÖ¶¯»­
	 *
	 */
	private void setImageBitmap(ImageView imageView, Bitmap bitmap,
			boolean isTran) {
		if (isTran) {
			final TransitionDrawable td = new TransitionDrawable(
					new Drawable[] {
							new ColorDrawable(android.R.color.transparent),
							new BitmapDrawable(bitmap) });
			td.setCrossFadeEnabled(true);
			imageView.setImageDrawable(td);
			td.startTransition(300);
		} else {
			imageView.setImageBitmap(bitmap);
		}
	}

	/**
	 * ´ÓÍøÂç»ñÈ¡Í¼Æ¬×Ö½ÚÊý×é
	 *
	 * @param url
	 * @return
	 */
	private byte[] loadByteArrayFromNetwork(String url) {

//		try {
//
//			HttpGet method = new HttpGet(url);
//			HttpResponse response = myapp.getHttpClient().execute(method);
//			HttpEntity entity = response.getEntity();
//			return EntityUtils.toByteArray(entity);
//
//		} catch (Exception e) {
//			return null;
//		}

		return null;

	}

	/**
	 * ¸ù¾ÝurlÉú³É»º´æÎÄ¼þÍêÕûÂ·¾¶Ãû
	 *
	 * @param url
	 * @return
	 */
	public String urlToFilePath(String url) {

		// À©Õ¹ÃûÎ»ÖÃ
		int index = url.lastIndexOf('.');
		if (index == -1) {
			return null;
		}

		StringBuilder filePath = new StringBuilder();

		// Í¼Æ¬´æÈ¡Â·¾¶
		filePath.append(myapp.getCacheDir().toString()).append('/');

		// Í¼Æ¬ÎÄ¼þÃû
		filePath.append(MD5.Md5(url)).append(url.substring(index));

		return filePath.toString();
	}

	/**
	 * Activity#onStopºó£¬ListView²»»áÓÐ²ÐÓàÇëÇó¡£
	 */
	public void stop() {

		// Çå¿ÕÇëÇó¶ÓÁÐ
		mImageQueue.clear();

	}

}
