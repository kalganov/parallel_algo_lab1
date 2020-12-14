package leti.labs.lab1

import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State

@State(Scope.Thread)
open class SizedBenchmark {
    @Param("10", "1000", "10000", "100000") var size: Int = 0
}