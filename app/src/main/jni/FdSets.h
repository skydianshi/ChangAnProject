#ifndef _FDSET_H_
#define _FDSET_H_
#include "ComDef.h"

class CFdSet {
public:
	CFdSet(int nSize = MAX_CONNECT_COUNT) {
		m_nSize = nSize;
		m_nCount = 0;
		m_pFdSet = new int[m_nSize];
		memset(m_pFdSet, 0, m_nSize * sizeof(int));
	}
	CFdSet(const CFdSet& other) {
		m_nSize = other.m_nSize;
		m_nCount = other.m_nCount;
		m_pFdSet = new int[m_nSize];
		memcpy(m_pFdSet, other.m_pFdSet, m_nSize * sizeof(int));
	}
	CFdSet& operator=(const CFdSet& other) {
		if (this == &other) {
			return *this;
		}
		SAFE_DELETE_ARRAY(m_pFdSet);
		m_nSize = other.m_nSize;
		m_nCount = other.m_nCount;
		m_pFdSet = new int[m_nSize];
		memcpy(m_pFdSet, other.m_pFdSet, m_nSize * sizeof(int));
		return *this;
	}
	~CFdSet() {
		SAFE_DELETE_ARRAY(m_pFdSet)
	}
	void CloseSockets() {
		for (int i = m_nCount - 1; i >= 0; --i) {
			close(m_pFdSet[i]);
		}
	}
	void FillSet(fd_set* pDst1, fd_set* pDst2) {
		FD_ZERO(pDst1);
		FD_ZERO(pDst2);
		for (int i = 0; i < m_nCount; ++i) {
			if (m_pFdSet[i]) {
				FD_SET(m_pFdSet[i], pDst1);
				FD_SET(m_pFdSet[i], pDst2);
			}
		}
	}
	bool Add(int nFd) {
		for (int i = 0; i < m_nSize; ++i) {
			if (0 == m_pFdSet[i]) {
				m_pFdSet[i] = nFd;
				++m_nCount;
				return true;
			}
		}
		return false;
	}
	int GetAt(int nPos) {
		return nPos < m_nSize ? m_pFdSet[nPos] : -1;
	}
	void Remove(int nPos) {
		if (nPos < m_nCount) {
			m_pFdSet[nPos] = 0;
			--m_nCount;
		}
	}
	int GetMaxSocket() {
		int nMaxSocket = 0;
		for (int i = 0; i < m_nCount; ++i) {
			if (m_pFdSet[i] > nMaxSocket) {
				nMaxSocket = m_pFdSet[i];
			}
		}
		return nMaxSocket;
	}
	int capacity() const {
		return m_nSize;
	}
	int size() const {
		return m_nCount;
	}
private:
	int* m_pFdSet;
	int m_nSize;
	int m_nCount;
};

#endif //_FDSET_H_
