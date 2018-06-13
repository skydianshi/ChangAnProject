package com.changan.changanproject.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.zlg.www.ItemData;


public class FrmCycleBuffer extends CycleBuffer<ItemData> {

	public enum Type {
		TYPE_SORT, TYPE_ALL
	}

	protected Type mType;
	protected Map<Long, Integer> mIndexMap;

	public FrmCycleBuffer(int nMaxSize, Type nType) {
		super(nMaxSize);
		// TODO Auto-generated constructor stub
		mIndexMap = new HashMap<Long, Integer>();
	}

	public void replace(int nIndex, ItemData e) {
		ItemData tmp;
		mLock.lock();
		try {
			tmp = get(nIndex);
			e.setStatisticsInfo(tmp);
			tmp.copyOf(e);
		} finally {
			mLock.unlock();
		}
	}

	public Integer find(Long nID) {
		mLock.lock();
		try {
			return mIndexMap.get(nID);
		} finally {
			mLock.unlock();
		}
	}

	@Override
	public void push(ItemData e) {
		// TODO Auto-generated method stub
		mLock.lock();
		try {
			if (mCurSize < mMaxSize) {
				mElements[mCurSize] = e;
				mIndexMap.put(e.nID, mCurSize);
			} else {
				resetMap(((ItemData) mElements[mFirstElementPos]).nID, e.nID,
						mCurSize - 1);
				mElements[mFirstElementPos] = e;
				if (++mFirstElementPos == mMaxSize) {
					mFirstElementPos = 0;
				}
			}
			if (mCurSize != mMaxSize) {
				++mCurSize;
			}
		} finally {
			mLock.unlock();
		}
	}

	private void resetMap(Long nKeyOld, Long nKeyNew, Integer nValue) {
		mIndexMap.remove(nKeyOld);
		Iterator<Entry<Long, Integer>> iterator = mIndexMap.entrySet().iterator();
		HashMap.Entry<Long, Integer> entry;
		while (iterator.hasNext()) {
			entry = (HashMap.Entry<Long, Integer>) iterator.next();
			entry.setValue(entry.getValue() - 1);
		}
		mIndexMap.put(nKeyNew, nValue);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		super.clear();
		mIndexMap.clear();
	}
}
