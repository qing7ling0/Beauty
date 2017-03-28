package com.lq.beauty.base.cache;

import com.lq.beauty.base.utils.HashUtil;

import java.io.Serializable;

public class CacheItem implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 存储的key */
	private final String key;

	/** 过期时间的时间戳 */
	private long timeStamp = 0L;

	private Object data;

	/**
	 *
	 * @param key
	 * @param data
	 * @param expiredTime 单位秒
	 */
	public CacheItem(final String key, Object data, final long expiredTime) {
		this.key = HashUtil.getMD5(key);
		this.timeStamp = System.currentTimeMillis() + expiredTime * 1000;
		this.data = data;
	}

	public String getKey() {
		return key;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getData() {
		return data;
	}

}