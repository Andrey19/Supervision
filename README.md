# Домашнее задание к занятию «3.2. Coroutines: Scopes, Cancellation, Supervision»

Выполненное задание прикрепите ссылкой на ваши GitHub-проекты в личном кабинете студента на сайте [netology.ru](https://netology.ru).

## Вопросы: Cancellation

### Вопрос №1

Отработает ли в этом коде строка `<--`? Поясните, почему да или нет.

```kotlin
fun main() = runBlocking {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
        launch {
            delay(500)
            println("ok") // <--
        }
        launch {
            delay(500)
            println("ok")
        }
    }
    delay(100)
    job.cancelAndJoin()
}
```
Ответ
Строка `<--` не отработает поскольку
- при отмене родительского корутина отменяются и дочерние корутины
- время на которое приостановлен дочерний корутин больше времени на которое приостановлен родительский корутин
### Вопрос №2

Отработает ли в этом коде строка `<--`. Поясните, почему да или нет.

```kotlin
fun main() = runBlocking {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
        val child = launch {
            delay(500)
            println("ok") // <--
        }
        launch {
            delay(500)
            println("ok")
        }
        delay(100)
        child.cancel()
    }
    delay(100)
    job.join()
}
```
Ответ:

Строка `<--` не отработает поскольку время на которое приостановлен дочерний корутин (child)
больше времени через которое он будет отменен (child.cancel())
## Вопросы: Exception Handling

### Вопрос №1

Отработает ли в этом коде строка `<--`. Поясните, почему да или нет.

```kotlin
fun main() {
    with(CoroutineScope(EmptyCoroutineContext)) {
        try {
            launch {
                throw Exception("something bad happened")
            }
        } catch (e: Exception) {
            e.printStackTrace() // <--
        }
    }
    Thread.sleep(1000)
}
```
Ответ:

Строка `<--` не отработает поскольку
корутина запускается внутри оператора try-catch
и в этом случае исключение выброшенное в дочерней корутине в родительской перехвачено не будет. При этом поскольку
дочерняя корутина завершается с искючением то завершается и родительская корутина.

### Вопрос №2

Отработает ли в этом коде строка `<--`. Поясните, почему да или нет.

```kotlin
fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
            coroutineScope {
                throw Exception("something bad happened")
            }
        } catch (e: Exception) {
            e.printStackTrace() // <--
        }
    }
    Thread.sleep(1000)
}
```
Ответ:

Строка `<--` отработает поскольку в данном случае нет дочерних корутинов запущенных внутри блока
try-catch и таким образом исключения обрабатываются родительской корутиной
### Вопрос №3

Отработает ли в этом коде строка `<--`. Поясните, почему да или нет.

```kotlin
fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
            supervisorScope {
                throw Exception("something bad happened")
            }
        } catch (e: Exception) {
            e.printStackTrace() // <--
        }
    }
    Thread.sleep(1000)
}
```
Ответ:

Строка `<--` отработает поскольку в данном случае нет дочерних корутинов запущенных внутри блока
try-catch и таким образом исключения обрабатываются родительской корутиной

### Вопрос №4

Отработает ли в этом коде строка `<--`. Поясните, почему да или нет.

```kotlin
fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
            coroutineScope {
                launch {
                    delay(500)
                    throw Exception("something bad happened") // <--
                }
                launch {
                    throw Exception("something bad happened")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    Thread.sleep(1000)
}
```
Ответ:

Строка `<--` не отработает поскольку все дочерние корутины внутри coroutineScope
останавливаются в случае если в одной из них произошло исключение.
В данном случае вторая дочерняя корутина выбрасывает исключение что приводит к остановке и первой,
которая в этот момент находиться в режиме ожидания

### Вопрос №5

Отработает ли в этом коде строка `<--`. Поясните, почему да или нет.

```kotlin
fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
            supervisorScope {
                launch {
                    delay(500)
                    throw Exception("something bad happened") // <--
                }
                launch {
                    throw Exception("something bad happened")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace() // <--
        }
    }
    Thread.sleep(1000)
}
```
Ответ:

Строка `<--` отработает поскольку исключение произошедшее в одной дочерней корутине не останавливает работу
других дочерних корутин находящихся внутри supervisorScope
### Вопрос №6

Отработает ли в этом коде строка `<--`. Поясните, почему да или нет.

```kotlin
fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        CoroutineScope(EmptyCoroutineContext).launch {
            launch {
                delay(1000)
                println("ok") // <--
            }
            launch {
                delay(500)
                println("ok")
            }
            throw Exception("something bad happened")
        }
    }
    Thread.sleep(1000)
}
```
Ответ:

Строка `<--` не отработает поскольку исключение произошедшее в родительской корутине останавливает родительскую
корутину что в свою очередь приводит к остановке и дочерние корутины которые в этот момент приостановлены

### Вопрос №7

Отработает ли в этом коде строка `<--`. Поясните, почему да или нет.

```kotlin
fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        CoroutineScope(EmptyCoroutineContext + SupervisorJob()).launch {
            launch {
                delay(1000)
                println("ok") // <--
            }
            launch {
                delay(500)
                println("ok")
            }
            throw Exception("something bad happened")
        }
    }
    Thread.sleep(1000)
}
```
Ответ:
Строка `<--` не отработает поскольку исключение произошедшее в родительской корутине останавливает родительскую
корутину что в свою очередь приводит к остановке и дочерние корутины которые в этот момент приостановлены
