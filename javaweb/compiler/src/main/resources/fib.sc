def fib(x){
    if x < 2 {
        x
    }else {
        fib(x-1) + fib(x-2)
    }
}

start = currentTime()
fib(36)
end = currentTime()
last = end - start

// 未优化时 计算 fib(36) 需要 7 - 8s
// 优化后需要 6s 好像变化不大， 需要集训看看