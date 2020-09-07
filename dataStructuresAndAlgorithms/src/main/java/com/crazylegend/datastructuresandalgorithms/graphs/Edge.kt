package com.crazylegend.datastructuresandalgorithms.graphs

/**
 * Created by crazy on 8/31/20 to long live and prosper !
 */
data class Edge<T>(
        val source: Vertex<T>,
        val destination: Vertex<T>,
        val weight: Double? = null
)