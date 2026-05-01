package sk.ditec.cud.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.utils.StringUtils;

public class CudCacheMap {

	private Map<String, Object> objMap = null;
	private Map<String, Object[]> arrayMap = null;

	public CudCacheMap() {

		this.objMap = new HashMap<String, Object>();
		this.arrayMap = new HashMap<String, Object[]>();
	}

	public void addRecord(String key, Object obj) throws AppException {

		try {
			if (!StringUtils.isValid(key)) {
				return;
			}

			this.objMap.put(key, obj);

		} catch (Throwable t) {
			DBUtils.handleException(t, "addRecord.error");
		}
	}

	public void addArray(String key, Object[] objs) throws AppException {

		try {
			if (!StringUtils.isValid(key)) {
				return;
			}

			this.arrayMap.put(key, objs);

		} catch (Throwable t) {
			DBUtils.handleException(t, "addArray.error");
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Serializable> T getRecord(String key, Class<T> clazz) throws AppException {

		try {
			if (!StringUtils.isValid(key)) {
				return null;
			}

			Object obj = this.objMap.get(key);

			return StringUtils.isValid(obj) ? (T) obj : null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "getRecord.error");
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Serializable> List<T> getList(String key, Class<T> clazz) throws AppException {

		try {
			if (!StringUtils.isValid(key)) {
				return null;
			}

			Object[] pole = this.arrayMap.get(key);
			if (!StringUtils.isValid(pole)) {
				return null;
			}

			List<T> list = new ArrayList<T>();
			for (Object obj : pole) {
				list.add((T) obj);
			}

			return list;

		} catch (Throwable t) {
			DBUtils.handleException(t, "getList.error");
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Serializable> Map<T, T> getMap(String key, Class<T> clazz) throws AppException {

		try {
			if (!StringUtils.isValid(key)) {
				return null;
			}

			Object obj = this.objMap.get(key);

			return StringUtils.isValid(obj) ? (HashMap<T, T>) obj : null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "getMap.error");
			return null;
		}
	}

	public boolean existRecord(String key) throws AppException {

		try {
			return StringUtils.isValid(this.objMap.get(key));

		} catch (Throwable t) {
			DBUtils.handleException(t, "existRecord.error");
			return false;
		}
	}

	public boolean existList(String key) throws AppException {

		try {
			return StringUtils.isValid(arrayMap.get(key));

		} catch (Throwable t) {
			DBUtils.handleException(t, "existList.error");
			return false;
		}
	}

}
