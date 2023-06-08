import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext

fun main() = runBlocking {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
        launch {
            delay(500)
            println("ok1") // <--
        }
        launch {
            delay(500)
            println("ok2")
        }
    }
    delay(100)
    job.cancelAndJoin()
}