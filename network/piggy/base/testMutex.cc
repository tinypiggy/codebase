#include "CountDownLatch.h"
#include <pthread.h>
#include <stdio.h>

#define THREAD_NUM 10

using namespace piggy;
CountDownLatch latch(THREAD_NUM);
long sum[THREAD_NUM];

void* threadFunc(void *);

int main(int argc, char const *argv[])
{
    pthread_t tid[THREAD_NUM];
    long total = 0;
    int res = 0;

    for (size_t i = 0; i < THREAD_NUM; i++)
    {
        printf("create a thread\n");
        res = pthread_create(&tid[i], NULL, threadFunc, (void *)i);
        if (res != 0)
        {
            return 1;
        }
        
    }

    latch.wait();
    for (size_t i = 0; i < THREAD_NUM; i++)
    {
        total += sum[i];
    }

    printf("total = %ld\n", total);
    return 0;
}

void * threadFunc(void * arg)
{
    pthread_t tid = pthread_self();
    pthread_detach(tid);

    long start = (long)arg * 100;
    printf("calculate start number = %ld\n", start);
    for (size_t i = start; i < start + 100; i++)
    {
        sum[(long)arg] += i;
    }
    
    latch.countDown();
    printf("calculate thread leave = %d\n", latch.getCount());

    return NULL;
}
