package leti.labs.lab1

import java.util.concurrent.locks.ReentrantLock


class ConcurrentLinkedSet<E> {

    private inner class Node : Comparable<Node?> {
        var item: E? = null
        var hash = 0

        @kotlin.jvm.Volatile
        var marked = false

        @kotlin.jvm.Volatile
        var next: Node? = null

        @kotlin.jvm.Volatile
        var replaceNode: Node? = null

        var lock: ReentrantLock

        constructor() {
            lock = ReentrantLock()
        }

        constructor(item: E, replaced: Node? = null) {
            this.item = item
            hash = item!!.hashCode()
            marked = false
            next = null
            replaceNode = replaced
            lock = ReentrantLock()
        }

        fun lock() {
            lock.lock()
        }

        fun unlock() {
            lock.unlock()
        }

        override fun compareTo(other: Node?): Int {
            if (other === null) return 1
            if (this === other) return 0
            if (this === head || other === tail) return -1
            return if (other === head || this === tail) 1 else hash.compareTo(other.hash)
        }

        override fun toString(): String {
            if (this === head) return "[$next"
            return if (this === tail) "]" else item.toString() + ", " + next
        }
    }

    private val head: Node
    private val tail: Node

    fun add(item: E): Boolean {
        val key = item!!.hashCode()
        while (true) {

            val windows: List<Node> = find(key)
            val pred: Node = windows[0]
            val curr: Node = windows[1]

            pred.lock()
            try {
                curr.lock()
                try {
                    if (validate(pred, curr)) {
                        return if (curr !== tail && curr.hash == key) {
                            false
                        } else {
                            val node = Node(item)
                            node.next = curr
                            pred.next = node
                            true
                        }
                    }
                } finally {
                    curr.unlock()
                }
            } finally {
                pred.unlock()
            }
        }
    }

    fun delete(item: E): Boolean {
        val key = item!!.hashCode()
        while (true) {
            val windows: List<Node> = find(key)
            val pred: Node = windows[0]
            val curr: Node = windows[1]

            pred.lock()
            try {
                curr.lock()
                try {
                    if (validate(pred, curr)) {
                        return if (curr === tail || curr.hash != key) {
                            false
                        } else {
                            curr.marked = true
                            pred.next = curr.next
                            true
                        }
                    }
                } finally {
                    curr.unlock()
                }
            } finally {
                pred.unlock()
            }
        }
    }

    operator fun contains(item: E): Boolean {
        val key = item!!.hashCode()
        var curr: Node? = head
        while (curr != null && curr.hash < key) {
            curr = curr.next
        }
        if (curr == null) {
            return false
        }
        return curr.hash == key && !curr.marked && (curr.replaceNode == null || curr.replaceNode!!.marked)
    }

    private fun validate(pred: Node, curr: Node): Boolean {
        return !pred.marked && !curr.marked && pred.next === curr
    }

    private fun find(key: Int): List<Node> {
        var pred: Node = head
        var curr: Node? = head.next
        while (curr != null && curr !== tail && curr.hash < key) {
            pred = curr
            curr = curr.next
        }
        return if (curr != null) listOf(pred, curr) else listOf(pred)
    }

    override fun toString(): String {
        return head.toString()
    }

    init {
        head = Node()
        tail = Node()
        head.next = tail
    }
}