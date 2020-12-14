package leti.labs.lab1

fun main() {
    val concurrentLinkedSet = ConcurrentLinkedSet<Int>();

    concurrentLinkedSet.add(1)
    concurrentLinkedSet.add(2)
    concurrentLinkedSet.add(3)
    concurrentLinkedSet.add(4)
    concurrentLinkedSet.add(5)
    println(concurrentLinkedSet.delete(3))
    println(concurrentLinkedSet.contains(3))
}