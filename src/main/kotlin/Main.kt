import java.util.concurrent.locks.ReentrantLock

var countPhilosopher = 0

class Philosopher(val id: String, val leftFork: ReentrantLock, val rightFork: ReentrantLock) : Runnable {
    override fun run() {
        eat()
    }

    private fun eat() {
        var tut = true
        while (tut) {
            when ((1..2).random()) {
                1 -> {
                    if (rightFork.tryLock()) {
                        println("Философ $id обедает левой вилкой.")
                        tut = false
                    } else if (!rightFork.tryLock() && !leftFork.tryLock()) {
                        tut = false
                        println("Философ $id размышляет.")
                    }
                }

                2 -> {
                    if (leftFork.tryLock()) {
                        println("Философ $id обедает правой вилкой.")
                        tut = false
                    }else if (!rightFork.tryLock() && !leftFork.tryLock()) {
                        tut = false
                        println("Философ $id размышляет.")
                    }

                }
            }
        }
    }
}

fun main() {
    println("Сколько философов за круглым столом: ")
    print("Введите целое число: ")
    countPhilosopher = enter().countPhilosopher

    var name = Array(countPhilosopher) {""}
    for (i in 0 until countPhilosopher){
        print("Имя философа ${i+1}: ")
        name[i] = readln()
    }
    println("_________________________")
    val forks = List(countPhilosopher) { ReentrantLock() }

    val philosophers = List(countPhilosopher) { id ->
        Philosopher(
            name[id],
            forks[id],
            forks[(id + 1) % forks.size]
        )
    }
    philosophers.forEach { Thread(it).start() }
}
//}

class enter {
    var countPhilosopher = 0
    init {
        var valid = false
        do {
            try {
                countPhilosopher = readLine()?.toInt() ?: 0
                valid = true
            } catch (e: NumberFormatException) {
                println("Ошибка. Введите целое число.")
            }
        } while (!valid)
    }
}