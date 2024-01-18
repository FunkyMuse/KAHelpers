package com.funkymuse.datastructuresandalgorithms.graphs

import com.funkymuse.datastructuresandalgorithms.queue.StackQueue
import com.funkymuse.datastructuresandalgorithms.stack.Stack

interface Graph<T> {
    val allVertices: ArrayList<Vertex<T>>

    fun createVertex(data: T): Vertex<T>

    fun addDirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Double?)

    fun addUndirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Double?) {
        addDirectedEdge(source, destination, weight)
        addDirectedEdge(destination, source, weight)
    }

    fun add(edge: EdgeType, source: Vertex<T>, destination: Vertex<T>, weight: Double?) {
        when (edge) {
            EdgeType.DIRECTED -> addDirectedEdge(source, destination, weight)
            EdgeType.UNDIRECTED -> addUndirectedEdge(source, destination, weight)
        }
    }

    fun edges(source: Vertex<T>): ArrayList<Edge<T>>

    fun weight(source: Vertex<T>, destination: Vertex<T>): Double?

    fun numberOfPaths(source: Vertex<T>, destination: Vertex<T>): Int {
        val numberOfPaths = NumberOfPaths(0)
        val visited: MutableSet<Vertex<T>> = mutableSetOf()
        paths(source, destination, visited, numberOfPaths)
        return numberOfPaths.value
    }

    fun breadthFirstSearch(source: Vertex<T>): ArrayList<Vertex<T>> {
        val queue = StackQueue<Vertex<T>>()
        val enqueued = ArrayList<Vertex<T>>()
        val visited = ArrayList<Vertex<T>>()
        queue.enqueue(source)
        enqueued.add(source)
        while (true) {
            val vertex = queue.dequeue() ?: break
            visited.add(vertex)
            val neighborEdges = edges(vertex)
            neighborEdges.forEach {
                if (!enqueued.contains(it.destination)) {
                    queue.enqueue(it.destination)
                    enqueued.add(it.destination)
                }
            }
        }
        return visited
    }

    private fun bfs(queue: StackQueue<Vertex<T>>, enqueued: ArrayList<Vertex<T>>, visited: ArrayList<Vertex<T>>) {
        val vertex = queue.dequeue() ?: return

        visited.add(vertex)

        val neighborEdges = edges(vertex)
        neighborEdges.forEach {
            if (!enqueued.contains(it.destination)) {
                queue.enqueue(it.destination)
                enqueued.add(it.destination)
            }
        }

        bfs(queue, enqueued, visited)
    }

    fun bfs(source: Vertex<T>): ArrayList<Vertex<T>> {
        val queue = StackQueue<Vertex<T>>()
        val enqueued = arrayListOf<Vertex<T>>()
        val visited = arrayListOf<Vertex<T>>()

        queue.enqueue(source)
        enqueued.add(source)

        bfs(queue, enqueued, visited)

        return visited
    }

    fun isDisconnected(): Boolean {
        val firstVertex = allVertices.firstOrNull() ?: return false

        val visited = breadthFirstSearch(firstVertex)
        allVertices.forEach {
            if (!visited.contains(it)) return true
        }

        return false
    }

    fun paths(source: Vertex<T>, destination: Vertex<T>, visited: MutableSet<Vertex<T>>, pathCount: NumberOfPaths<Int>) {
        visited.add(source)
        if (source == destination) {
            pathCount.value += 1
        } else {
            val neighbors = edges(source)
            neighbors.forEach { edge ->
                if (edge.destination !in visited) {
                    paths(edge.destination, destination, visited, pathCount)
                }
            }
        }
        visited.remove(source)
    }


    fun depthFirstSearch(source: Vertex<T>): ArrayList<Vertex<T>> {
        val stack = Stack<Vertex<T>>()
        val visited = arrayListOf<Vertex<T>>()
        val pushed = mutableSetOf<Vertex<T>>()

        stack.push(source)
        pushed.add(source)
        visited.add(source)

        outer@ while (true) {
            if (stack.isEmpty) break

            val vertex = stack.peek()!!
            val neighbors = edges(vertex)

            if (neighbors.isEmpty()) {
                stack.pop()
                continue
            }

            for (i in 0 until neighbors.size) {
                val destination = neighbors[i].destination
                if (destination !in pushed) {
                    stack.push(destination)
                    pushed.add(destination)
                    visited.add(destination)
                    continue@outer
                }
            }
            stack.pop()
        }

        return visited
    }

    fun depthFirstSearchRecursive(start: Vertex<T>): ArrayList<Vertex<T>> {
        val visited = arrayListOf<Vertex<T>>()
        val pushed = mutableSetOf<Vertex<T>>()

        depthFirstSearch(start, visited, pushed)

        return visited
    }

    fun depthFirstSearch(
            source: Vertex<T>,
            visited: ArrayList<Vertex<T>>,
            pushed: MutableSet<Vertex<T>>
    ) {
        pushed.add(source)
        visited.add(source)

        val neighbors = edges(source)
        neighbors.forEach {
            if (it.destination !in pushed) {
                depthFirstSearch(it.destination, visited, pushed)
            }
        }
    }

    fun hasCycle(source: Vertex<T>): Boolean {
        val pushed = mutableSetOf<Vertex<T>>()
        return hasCycle(source, pushed)
    }

    fun hasCycle(source: Vertex<T>, pushed: MutableSet<Vertex<T>>): Boolean {
        pushed.add(source)

        val neighbors = edges(source)
        neighbors.forEach {
            if (it.destination !in pushed && hasCycle(it.destination, pushed)) {
                return true
            } else if (it.destination in pushed) {
                return true
            }
        }

        pushed.remove(source)
        return false
    }

}