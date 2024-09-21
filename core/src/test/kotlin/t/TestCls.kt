package t

import java.io.Closeable

open class TestCls : Closeable {
    protected var a = 1

    fun method() {
        println("1111");
    }

    override fun close() {
        println(1)
    }

}