package com.raffascript.sortvisualizer.visualization.data

data class AlgorithmProgress(
    val list: IntArray,
    val state: AlgorithmState,
    val highlights: List<Highlight>,
    val arrayAccesses: Long,
    val comparisons: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlgorithmProgress

        if (!list.contentEquals(other.list)) return false
        if (state != other.state) return false
        if (highlights != other.highlights) return false
        if (arrayAccesses != other.arrayAccesses) return false
        if (comparisons != other.comparisons) return false

        return true
    }

    override fun hashCode(): Int {
        var result = list.contentHashCode()
        result = 31 * result + state.hashCode()
        result = 31 * result + highlights.hashCode()
        result = 31 * result + arrayAccesses.hashCode()
        result = 31 * result + comparisons.hashCode()
        return result
    }
}