#ifndef PIGGY_BASE_NONCOPYABLE_H
#define PIGGY_BASE_NONCOPYABLE_H

namespace piggy
{

class noncopyable
{
protected:
    noncopyable() {};
    ~noncopyable() {};
private:
    noncopyable(const noncopyable&); // C11后也可以 = delete
    noncopyable& operator=(const noncopyable&);
    
};

} // namespace piggy


#endif // PIGGY_BASE_NONCOPYABLE_H