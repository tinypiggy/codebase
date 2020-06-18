#ifndef PIGGY_BASE_CONDITION_H
#define PIGGY_BASE_CONDITION_H

#include "Mutex.h"

namespace piggy
{
    
class Condition : noncopyable
{
private:
    pthread_cond_t  pcond_;
    MutexLock&      mutex_;
public:
    Condition(MutexLock& mutex)
      : mutex_(mutex)
    {
        MCHECK(pthread_cond_init(&pcond_, NULL));
    }
    ~Condition()
    {
        MCHECK(pthread_cond_destroy(&pcond_));
    };

    void wait()
    {
        MutexLock::UnassignGuard guard(mutex_);
        MCHECK(pthread_cond_wait(&pcond_, mutex_.getPthreadMutex()));
    }

    void notify()
    {
        MCHECK(pthread_cond_signal(&pcond_));
    }

    void notifyAll()
    {
        MCHECK(pthread_cond_broadcast(&pcond_));
    }

    bool waitForSeconds(double seconds);
};

} // namespace name


#endif // PIGGY_BASE_CONDITION_H