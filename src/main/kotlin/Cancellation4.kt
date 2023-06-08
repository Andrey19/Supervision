import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext


fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
           coroutineScope {
                launch {
                    delay(500)
                    throw Exception("something bad happened1") // <--
                }
                launch {
                    throw Exception("something bad happened2")
                }
            }
        } catch (e: Exception) {
            println("------------")
            e.printStackTrace()
        }
    }
    Thread.sleep(1000)
}