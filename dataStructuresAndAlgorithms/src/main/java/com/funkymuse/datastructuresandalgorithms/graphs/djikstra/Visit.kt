package com.funkymuse.datastructuresandalgorithms.graphs.djikstra

import com.funkymuse.datastructuresandalgorithms.graphs.Edge

class Visit<T>(val type: VisitType, val edge: Edge<T>? = null)