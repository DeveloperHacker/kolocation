package ru.spbstu.icc.kspt

class IdGenerator {
    private var current = 0

    fun next() = "Thread${Thread.currentThread().id}@${current++}"
}