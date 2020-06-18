#include "Condition.h"

#include <errno.h>
#include <stdint.h>

bool piggy::Condition::waitForSeconds(double seconds)
{
    struct timespec ts;
    clock_gettime(CLOCK_REALTIME, &ts);

    const int64_t kNanoSecondsPerSecond = 1000000000;
    int64_t nanoseconds = static_cast<int64_t>(seconds * kNanoSecondsPerSecond);

    ts.tv_sec += static_cast<time_t>((nanoseconds + ts.tv_nsec) / kNanoSecondsPerSecond);
    ts.tv_nsec = static_cast<long> ((nanoseconds + ts.tv_nsec) % kNanoSecondsPerSecond);

    MutexLock::UnassignGuard guard(mutex_);
    return ETIMEDOUT == pthread_cond_timedwait(&pcond_, mutex_.getPthreadMutex(), &ts);
}