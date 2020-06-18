#ifndef PIGGY_BASE_COUNTDOWNLATCH_H
#define PIGGY_BASE_COUNTDOWNLATCH_H

#include <Condition.h>

namespace piggy
{

class CountDownLatch : noncopyable
{
private:
    mutable MutexLock   mutex_;
    Condition           cond_;
    int                 count_;
public:
    explicit CountDownLatch(int count);

    void wait();
    void countDown();
    int getCount() const;
};

} // namespace piggy


#endif // PIGGY_BASE_COUNTDOWNLATCH_H