package com.changan.changanproject.adapter;

import java.util.concurrent.locks.ReentrantLock;

public class CycleBuffer<E> {

	protected int mMaxSize;
	protected int mCurSize;
	protected int mFirstElementPos;
	protected Object[] mElements;
	protected final ReentrantLock mLock = new ReentrantLock(true);
	protected int mIntTmp;
	

	public CycleBuffer(int nMaxSize) {
		mFirstElementPos = 0;
		mMaxSize = nMaxSize;
		mCurSize = 0;
		mElements = new Object[mMaxSize];
	}

	@SuppressWarnings("unchecked")
	public E get(int nIndex) {
		mLock.lock();
		try {
			if (nIndex > -1 && nIndex < mCurSize) {
				mIntTmp = mFirstElementPos + nIndex;
				return (E) mElements[mIntTmp < mCurSize ? mIntTmp : mIntTmp
						- mMaxSize];
			}
			return null;
		} finally {
			mLock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public boolean replace(E e) {
		for (int i = 0; i < mCurSize; i++) {
			if (e.equals((E) mElements[i])) {
				mElements[i] = e;
				return true;
			}
		}
		return false;
	}

	public void push(E e) {
		mLock.lock();
		try {
			if (mCurSize < mMaxSize) {
				mElements[mCurSize] = e;
			} else {
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

	public void setSize(int nSizeNew) {
		mLock.lock();
		try {
			mFirstElementPos = 0;
			mMaxSize = nSizeNew;
			mCurSize = 0;
			mElements = new Object[mMaxSize];
		} finally {
			mLock.unlock();
		}
	}

	public void clear() {
		mFirstElementPos = 0;
		mCurSize = 0;
	}

	public int size() {
		return mCurSize;
	}

	public int capacity() {
		return mMaxSize;
	}
}
