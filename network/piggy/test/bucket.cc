#include <sys/time.h>
#include <stdlib.h>
#include <assert.h>
#include <sys/fcntl.h>
#include <unistd.h>
#include <string.h>
#include <stdio.h>

using namespace std;

enum FLAG {
    SUCCESS,
    FAIL
};

template <size_t SIZE, int LIMIT, int UNIT>
class bucket_queue {
    typedef struct timeval my_time;
    public:
        bucket_queue();
        inline size_t available();
        FLAG process();
        inline size_t next(size_t index);
        inline size_t limit_location(); // 当前元素前 LIMIT 个元素的位置
        inline int64_t time_diff(const my_time & l, const my_time & r);
        void batch_process(int count);
    private:
        my_time queue[SIZE];
        size_t head;
        size_t tail;
        my_time start;
};

template <size_t SIZE, int LIMIT, int UNIT>
bucket_queue<SIZE, LIMIT, UNIT>::bucket_queue():head(0), tail(0) {
    assert(SIZE >= LIMIT);
    memset(queue, 0, sizeof(queue));
    ::gettimeofday(&start, NULL);
}

template <size_t SIZE, int LIMIT, int UNIT>
size_t bucket_queue<SIZE, LIMIT, UNIT>::available(){
    if (head > tail)
    {
        return SIZE - (head - tail);
    }
    return tail - head;
}
template <size_t SIZE, int LIMIT, int UNIT>
int64_t bucket_queue<SIZE, LIMIT, UNIT>::time_diff(const my_time & l, const my_time & r){
    return (l.tv_sec - r.tv_sec) * 1000000 + (l.tv_usec - r.tv_usec) ;
}

template <size_t SIZE, int LIMIT, int UNIT>
FLAG bucket_queue<SIZE, LIMIT, UNIT>::process(){
    my_time tv;
    ::gettimeofday(&tv, NULL);
    printf("%lu ==> ", time_diff(tv, start));
    // if (available())
    // {
    //     queue[head] = tv;
    //     head = next(head);
    //     return SUCCESS;
    // }
    // 获取当前元素前 LIMIT 个元素的的时间和当前时间的时间差
    int64_t diff = time_diff(tv, queue[limit_location()]);
    if (diff > UNIT) // 时间差 大于 UNIT （每 UNIT 时间 可以通过 LIMIT 个数据）
    {                // 这个元素进入插入队列头，替换调队尾元素 -- 环状队列
        printf("tail: %ld, location: %ld, diff: %ld => ", tail, limit_location(), diff);
        queue[head] = tv;
        head = next(head);
        tail = head;
        return SUCCESS;
    }
    
    if (SIZE > LIMIT) // 桶大小 和 流量现在不一致
    {
        int64_t diff = time_diff(tv, queue[tail]);
        printf("tail : %ld , diff: %ld => ", tail, diff);
        assert(diff >= 0);
        if ((double)LIMIT / UNIT * diff > SIZE) // 当前时间和队尾元素（最早的元素）的时间 乘以单位时间通过的元素
        {                                       // 如果大于桶大小，说明 这个时间差内还可以生成 令牌
            queue[head] = tv;
            head = next(head);
            tail = head;
            return SUCCESS;
        }
    }
    return FAIL;
}

template <size_t SIZE, int LIMIT, int UNIT>
size_t bucket_queue<SIZE, LIMIT, UNIT>::next(size_t index){
    if (index == SIZE - 1){
        return 0;
    }
    return index + 1;
}

template <size_t SIZE, int LIMIT, int UNIT>
size_t bucket_queue<SIZE, LIMIT, UNIT>::limit_location(){

    if (tail < LIMIT){
        return SIZE + tail -LIMIT;
    }
    return tail - LIMIT;
}

template <size_t SIZE, int LIMIT, int UNIT>
void bucket_queue<SIZE, LIMIT, UNIT>::batch_process(int count)
{
    for (int i = 0; i < count; i++)
    {
        if (process() == SUCCESS)
        {
            ::printf("success\n");
        }else{
            ::printf("fail\n");
        }
    }
}

int main(int argc, char const *argv[])
{
    bucket_queue<5, 3, 1000000> bq;
    int fd1 = ::open("success.log", O_RDWR|O_CREAT);
    int fd2 = ::open("fail.log", O_RDWR|O_CREAT);

    bq.batch_process(10);
    sleep(1);
    bq.batch_process(10);
    sleep(2);
    bq.batch_process(10);
    return 0;
}
