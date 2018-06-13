#ifndef _AUTOLOCK_H_
#define _AUTOLOCK_H_
#include <pthread.h>

class CAutoMutex {
public:
	CAutoMutex() {
		pthread_mutexattr_init(&m_mutexAttr);
		pthread_mutexattr_settype(&m_mutexAttr, PTHREAD_MUTEX_RECURSIVE_NP);
		pthread_mutex_init(&m_mutex, &m_mutexAttr);
	}
	~CAutoMutex() {
		pthread_mutex_destroy(&m_mutex);
	}
	void Lock() {
		pthread_mutex_lock(&m_mutex);
	}
	void UnLock() {
		pthread_mutex_unlock(&m_mutex);
	}
private:
	pthread_mutex_t m_mutex;
	pthread_mutexattr_t m_mutexAttr;
};
class CAutoLock {
public:
	CAutoLock(CAutoMutex* pSec) {
		m_pMutex = pSec;
		m_pMutex->Lock();
	}
	~CAutoLock() {
		m_pMutex->UnLock();
	}
private:
	CAutoMutex* m_pMutex;
};

#endif // _AUTOLOCK_H_
