class base {
    a = 1
    def method1(x){
        this.a = this.a + x
    }
}

object = new base

object.a = 2
object.method1(2)
object.a