package com.lq.beauty.base.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.os.Environment;

import com.lq.beauty.base.BaseApplication;
import com.lq.beauty.base.utils.FileUtil;
import com.lq.beauty.base.utils.HashUtil;

/**
 * 缓存管理器
 */
public class CacheManager {
	/** 缓存文件路径 */
	public static final String APP_CACHE_PATH = "appdata";

	/** sdcard 最小空间，如果小于10M，不会再向sdcard里面写入任何数据 */
	public static final long SDCARD_MIN_SPACE = 1024 * 1024 * 10;

	private static CacheManager cacheManager;

	private CacheManager() {
	}

	/**
	 * 获取CacheManager实例
	 * 
	 * @return
	 */
	public static synchronized CacheManager getInstance() {
		if (CacheManager.cacheManager == null) {
			CacheManager.cacheManager = new CacheManager();
		}
		return CacheManager.cacheManager;
	}

	public void initCacheDir() {
		// sdcard已经挂载并且空间不小于10M，可以写入文件;小于10M时，清除缓存
		if (FileUtil.getFreeDiskSpace() < SDCARD_MIN_SPACE) {
			// TODO 可以优化这里
			clearAllData();
		} else {
			final File dir = new File(getCachePath());
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
	}

	public String getCachePath() {
		String path = "";
		if (FileUtil.checkSdcardMounted()) {
			path = BaseApplication.context().getExternalCacheDir().getAbsolutePath();
		} else {
			path = BaseApplication.context().getCacheDir().getAbsolutePath();
		}
		return path + File.separator + APP_CACHE_PATH + File.separator;
	}

	/**
	 * 从文件缓存中取出缓存，没有则返回空
	 * 
	 * @param key
	 * @return
	 */
	public Object getCache(final String key) {
		String md5Key = HashUtil.getMD5(key);
		if (contains(md5Key)) {
			final CacheItem item = getFromCache(md5Key);
			if (item != null) {
				return item.getData();
			}
		}
		return null;
	}

	/**
	 * 将CacheItem从磁盘读取出来
	 * 
	 * @param key
	 * @return 缓存数据CachItem
	 */
	synchronized CacheItem getFromCache(final String key) {
		CacheItem cacheItem = null;
		Object findItem = readObject(getCachePath() + key);
		if (findItem != null) {
			cacheItem = (CacheItem) findItem;
		}

		// 缓存不存在
		if (cacheItem == null)
			return null;

		// 缓存过期
		long a = System.currentTimeMillis();
		if (cacheItem.getTimeStamp() > 0 && System.currentTimeMillis() > cacheItem.getTimeStamp()) {
			// 删除缓存
			deleteCache(key);
			return null;
		}

		return cacheItem;
	}

	/**
	 * 将CacheItem缓存到磁盘
	 *
	 * @param key
	 * @param data
	 * @param expiredTime
	 * @return 是否缓存，True：缓存成功，False：不能缓存
	 */

	public synchronized boolean addCache(String key, Object data, int expiredTime) {
		return addCache(new CacheItem(key, data, expiredTime));
	}

	/**
	 * 将CacheItem缓存到磁盘
	 * 
	 * @param item
	 * @return 是否缓存，True：缓存成功，False：不能缓存
	 */

	public synchronized boolean addCache(final CacheItem item) {
		if (FileUtil.getFreeDiskSpace() > SDCARD_MIN_SPACE) {
			saveObject(getCachePath() + item.getKey(), item);
			return true;
		}

		return false;
	}

	public synchronized boolean deleteCache(final String key) {
		// TODO
		return true;
	}

	/**
	 * 查询是否有key对应的缓存文件
	 *
	 * @param key
	 * @return
	 */
	public boolean contains(final String key) {
		final File file = new File(getCachePath() + key);
		return file.exists();
	}

	/**
	 * 清除缓存文件
	 */
	void clearAllData() {
		File file = null;
		File[] files = null;
		if (FileUtil.checkSdcardMounted()) {
			file = new File(getCachePath());
			files = file.listFiles();
			if (files != null) {
				for (final File file2 : files) {
					file2.delete();
				}
			}
		}
	}

	public static final void saveObject(String path, Object saveObject) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		File f = new File(path);
		try {
			fos = new FileOutputStream(f);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(saveObject);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static final Object readObject(String path) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		Object object = null;
		File f = new File(path);
		if (!f.exists()) {
			return null;
		}
		try {
			fis = new FileInputStream(f);
			ois = new ObjectInputStream(fis);
			object = ois.readObject();
			return object;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return object;
	}

}
