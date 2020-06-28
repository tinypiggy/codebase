class base {
    a = 1
    def method1(x){
        this.a = this.a + x
    }
}

class derived extends base {

}

object = new derived

object.a = 2
object.method1(2)
object.a