#include <iostream>
#include <memory>
#include <utility>
 
struct Foo {
    Foo(const std::string & name): name(name) { std::cout << name << " Foo...\n"; }
    ~Foo() { std::cout << name << " ~Foo...\n"; }
    std::string name;
};
 
struct D {
    void operator() (Foo* p) {
        std::cout << p->name << " Calling delete for Foo object... \n";
        delete p;
    }
};
 
int main()
{
    std::cout << "Creating new Foo...\n";
    std::unique_ptr<Foo, D> up1(new Foo("1"), D());  // up owns the Foo pointer (deleter D)
 
    // std::cout << "Replace owned Foo with a new Foo...\n";
    // up1.reset(new Foo());  // calls deleter for the old one
 
    // std::cout << "Release and delete the owned Foo...\n";
    // up1.reset();      
    std::unique_ptr<Foo, D> up2(new Foo("2"), D());
    up1 = std::move(up2);
    if (!up2)
    {
         std::cout << "up2 is dead\n";
    }
    
    up1 = std::move(up2);
    std::cout << "quit" << std::endl;
}
