import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext


fun main() = runBlocking {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
        val child = launch {
            delay(500)
            println("ok1") // <--
        }
        val child2 = launch {
            delay(500)
            println("ok2")
        }
        delay(100)
        child.cancel()
    }
    delay(100)
    job.join()
}