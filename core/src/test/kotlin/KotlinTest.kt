import kotlin.test.Test

class KotlinTest {

    @Test
    fun test() {
        TestClass().method()
    }

}

class TestClass {
    val t = 10
}

fun TestClass.method() {
    println(this.t)
}