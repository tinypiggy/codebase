#ifndef PIGGY_BASE_MUTEX_H
#define PIGGY_BASE_MUTEX_H

#include "noncopyable.h"
#include <pthread.h>
#include <unistd.h>
#include <sys/syscall.h>
#include <assert.h>

// (void)err 是告诉编译器
#define MCHECK(ret) ( { __typeof__(ret)err = (ret); \
                      assert(err == 0); (void)err; } )

// using boost::noncopyable;

namespace piggy {

class MutexLock : noncopyable
{
public:
    MutexLock()
      : holder_(0)
    {
        pthread_mutex_init(&mutex_, NULL);
    }
    ~MutexLock()
    {
        assert(holder_ == 0);
        pthread_mutex_destroy(&mutex_);
    }

    void lock()
    {
        MCHECK(pthread_mutex_lock(&mutex_));
        assignHolder();
    }

    void unlock()
    {
        unassignHolder();
        MCHECK(pthread_mutex_unlock(&mutex_));
    }

    pthread_mutex_t * getPthreadMutex()
    {
        return &mutex_;
    }

private:

    friend class Condition;

    class UnassignGuard : noncopyable 
    {
    public:
        UnassignGuard(MutexLock& owner)
          : owner_(owner)
        {
            owner_.unassignHolder();
        }
        ~UnassignGuard()
        {
            owner_.assignHolder();
        }
    private:
        MutexLock& owner_;
    };

    void unassignHolder()
    {
        holder_ = 0;
    }

    void assignHolder()
    {
        holder_ = syscall(SYS_gettid);
    }

    pthread_mutex_t mutex_;
    pid_t           holder_;
};

class MutexGuard : noncopyable
{
private:
    MutexLock& lock_;
public:
    MutexGuard(MutexLock& lock) 
      : lock_(lock)   // 引用类型的初始化应该不会调用构造函数
    {
        lock.lock();
    }
    ~MutexGuard()
    {
        lock_.unlock();
    }
};


}
#endif // PIGGY_BASE_MUTEX_H 