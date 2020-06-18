#include <sys/time.h>
#include <stdlib.h>
#include <memory>
#include <assert.h>

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
        FLAG process(const shared_ptr<my_time> & tv);
        size_t next(size_t index);
        size_t limit_location(); // 最近 LIMIT 个元素位置
        int time_diff(const shared_ptr<my_time> & l, const shared_ptr<my_time> & r);
    private:
        shared_ptr<my_time> queue[SIZE];
        size_t head;
        size_t tail;
};

template <size_t SIZE, int LIMIT, int UNIT>
bucket_queue<SIZE, LIMIT, UNIT>::bucket_queue():head(0), tail(0) {
    assert(SIZE >= LIMIT);
    my_time *mt = static_cast<my_time *> (malloc(sizeof(my_time)));
    ::gettimeofday(mt, NULL);
    shared_ptr<my_time> tv(mt);
    queue[tail] = tv;    
    head = next(head);
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
FLAG bucket_queue<SIZE, LIMIT, UNIT>::process(const shared_ptr<my_time> & tv){
    if (available())
    {
        queue[head] = tv;
        head = next(head);
        return SUCCESS;
    }
    if (time_diff(tv, queue[limit_location()]) > UNIT)
    {
        queue[head] = tv;
        head = next(head);
        tail = head;
        return SUCCESS;
    }
    
    if (SIZE > LIMIT)
    {
        int diff = time_diff(tv, queue[tail]);
        assert(diff >= 0);
        if ((double)LIMIT / UNIT * diff > SIZE)
        {
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

    if (tail < (SIZE - LIMIT)){
        return tail - LIMIT;
    }
    return tail - (SIZE - LIMIT);
}

int main(int argc, char const *argv[])
{
    bucket_queue<100, 60, 1000000> bq;
    return 0;
}
