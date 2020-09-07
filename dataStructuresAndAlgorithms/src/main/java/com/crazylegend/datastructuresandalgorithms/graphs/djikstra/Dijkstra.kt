package com.crazylegend.datastructuresandalgorithms.graphs.djikstra

import com.crazylegend.datastructuresandalgorithms.graphs.Edge
import com.crazylegend.datastructuresandalgorithms.graphs.Vertex
import com.crazylegend.datastructuresandalgorithms.graphs.adjacency.AdjacencyList
import com.crazylegend.datastructuresandalgorithms.queue.priority.comparator.ComparatorPriorityQueue

/**
 * Created by crazy on 9/7/20 to long live and prosper !
 */
class Dijkstra<T>(private val graph: AdjacencyList<T>) {

    private fun route(destination: Vertex<T>, paths: HashMap<Vertex<T>, Visit<T>>): ArrayList<Edge<T>> {
        var vertex = destination
        val path = arrayListOf<Edge<T>>()

        loop@ while (true) {
            val visit = paths[vertex] ?: break

            when (visit.type) {
                VisitType.START -> break@loop
                VisitType.EDGE -> visit.edge?.let {
                    path.add(it)
                    vertex = it.source
                }
            }
        }

        return path
    }

    private fun distance(destination: Vertex<T>, paths: HashMap<Vertex<T>, Visit<T>>): Double {
        val path = route(destination, paths)
        return path.sumByDouble { it.weight ?: 0.0 }
    }

    fun shortestPath(start: Vertex<T>): HashMap<Vertex<T>, Visit<T>> {
        val paths: HashMap<Vertex<T>, Visit<T>> = HashMap()
        paths[start] = Visit(VisitType.START)

        val distanceComparator = Comparator<Vertex<T>> { first, second ->
            (distance(second, paths) - distance(first, paths)).toInt()
        }

        val priorityQueue = ComparatorPriorityQueue(distanceComparator)
        priorityQueue.enqueue(start)

        while (true) {
            val vertex = priorityQueue.dequeue() ?: break
            val edges = graph.edges(vertex)

            edges.forEach {
                val weight = it.weight ?: return@forEach

                if (paths[it.destination] == null
                        || distance(vertex, paths) + weight < distance(it.destination, paths)) {
                    paths[it.destination] = Visit(VisitType.EDGE, it)
                    priorityQueue.enqueue(it.destination)
                }
            }
        }

        return paths
    }

    fun shortestPath(destination: Vertex<T>, paths: HashMap<Vertex<T>, Visit<T>>): ArrayList<Edge<T>> {
        return route(destination, paths)
    }

    fun getAllShortestPath(source: Vertex<T>): HashMap<Vertex<T>, ArrayList<Edge<T>>> {
        val paths = HashMap<Vertex<T>, ArrayList<Edge<T>>>()
        val pathsFromSource = shortestPath(source)

        graph.allVertices.forEach {
            val path = shortestPath(it, pathsFromSource)
            paths[it] = path
        }

        return paths
    }

}

