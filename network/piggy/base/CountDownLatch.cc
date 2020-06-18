#include "CountDownLatch.h"

piggy::CountDownLatch::CountDownLatch(int count)
  : count_(count), 
    mutex_(), 
    cond_(mutex_)
{}

void piggy::CountDownLatch::wait()
{
    MutexGuard lock(mutex_);
    while (count_ > 0)
    {
        cond_.wait();
    }
}

void piggy::CountDownLatch::countDown()
{
    MutexGuard lock(mutex_);
    count_--;
    if (count_ == 0)
    {
        cond_.notifyAll();
    }
}

int piggy::CountDownLatch::getCount() const
{
    MutexGuard lock(mutex_);
    return count_;
}