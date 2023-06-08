import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext


fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        CoroutineScope(EmptyCoroutineContext + SupervisorJob()).launch {
            launch {
                delay(1000)
                println("ok1") // <--
            }
            launch {
                delay(500)
                println("ok2")
            }
            println("----------------")
            throw Exception("something bad happened")
        }
    }
    Thread.sleep(1000)
}