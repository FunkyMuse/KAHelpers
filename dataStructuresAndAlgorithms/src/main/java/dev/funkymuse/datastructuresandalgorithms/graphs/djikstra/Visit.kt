package dev.funkymuse.datastructuresandalgorithms.graphs.djikstra

import dev.funkymuse.datastructuresandalgorithms.graphs.Edge

class Visit<T>(val type: VisitType, val edge: Edge<T>? = null)