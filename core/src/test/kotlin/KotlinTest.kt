import t.TestCls
import kotlin.test.Test

class KotlinTest {
    @Test
    fun test() {
        val instance = TestCls()

        instance.use {

        }

        val result1 = instance.let {
            // 此时it为instance实例
            println(it)
            it.method()
            return@let 123
        }
        println(result1) // 123

        val result2 = instance.also {
            // 此时it为instance实例
            it.method()
            println(it)
            // 相比于let没有返回值, 可以看成return this
        }

        println(result2) // instance本身

        val result3 = instance.apply {
            // 此时this为instance实例
            println(this)
            this.method()
            // 相比于also, 执行对象变了, 也是return this
        }

        println(result3) // instance本身

        instance.run { // 相比于apply, 有返回值
            println(this)
            this.method()
            return@run 123
        }


        with(instance) { // 内容与run一样, 仅仅是调用方式不一样
            println(this)
            this.method()
            return@with 123
        }


    }
}
