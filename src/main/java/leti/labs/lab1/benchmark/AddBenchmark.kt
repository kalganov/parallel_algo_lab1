package leti.labs.lab1.benchmark

import leti.labs.lab1.ConcurrentLinkedSet
import leti.labs.lab1.SizedBenchmark
import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
open class AddBenchmark : SizedBenchmark() {

    private var _data: ConcurrentLinkedSet<Int>? = null
    val data: ConcurrentLinkedSet<Int>
        get() = _data!!

    @Setup
    fun setup() {
        val set = ConcurrentLinkedSet<Int>()
        for (n in 1..size)
            set.add(n)
        _data = set
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun add(): Boolean {
        return data.add(Random.nextInt() + size)
    }
}