package com.funkymuse.datastructuresandalgorithms.graphs.adjacency

import com.funkymuse.datastructuresandalgorithms.graphs.Edge
import com.funkymuse.datastructuresandalgorithms.graphs.EdgeType
import com.funkymuse.datastructuresandalgorithms.graphs.Graph
import com.funkymuse.datastructuresandalgorithms.graphs.Vertex

class AdjacencyList<T> : Graph<T> {
    private val adjacencies: HashMap<Vertex<T>, ArrayList<Edge<T>>> = HashMap()

    override val allVertices: ArrayList<Vertex<T>> get() = ArrayList(adjacencies.keys)

    override fun createVertex(data: T): Vertex<T> {
        val vertex = Vertex(adjacencies.count(), data)
        adjacencies[vertex] = ArrayList()
        return vertex
    }

    override fun addDirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Double?) {
        val edge = Edge(source, destination, weight)
        adjacencies[source]?.add(edge)
    }

    override fun addUndirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Double?) {
        addDirectedEdge(source, destination, weight)
        addDirectedEdge(destination, source, weight)
    }

    override fun add(edge: EdgeType, source: Vertex<T>, destination: Vertex<T>, weight: Double?) {
        when (edge) {
            EdgeType.DIRECTED -> addDirectedEdge(source, destination, weight)
            EdgeType.UNDIRECTED -> addUndirectedEdge(source, destination, weight)
        }
    }

    fun copyVertices(graph: AdjacencyList<T>) {
        graph.allVertices.forEach {
            adjacencies[it] = arrayListOf()
        }
    }

    override fun edges(source: Vertex<T>) = adjacencies[source] ?: arrayListOf()

    override fun weight(source: Vertex<T>, destination: Vertex<T>) = edges(source).firstOrNull { it.destination == destination }?.weight

    override fun toString(): String {
        return buildString {
            adjacencies.asSequence().forEach { (vertex, edges) ->
                val edgeString = edges.joinToString { it.destination.data.toString() }
                append("${vertex.data} ---> [ $edgeString ]\n")
            }
        }
    }

}