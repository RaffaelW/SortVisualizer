package com.raffascript.sortvisualizer.data.viszualization

data class Highlight(val index: Int, val highlightOption: HighlightOption)

infix fun Int.highlighted(highlightOption: HighlightOption) = Highlight(this, highlightOption)

fun List<Highlight>.getHighlightWithHighestPriority(index: Int): HighlightOption? {
    return this.filter { it.index == index }.map { it.highlightOption }.takeIf {
        it.contains(HighlightOption.COLOURED_PRIMARY) || it.contains(HighlightOption.COLOURED_SECONDARY)
    }?.minByOrNull { it.priority }
}

fun List<Highlight>.getHighlightsWithOption(option: HighlightOption): List<Highlight> {
    return this.filter { it.highlightOption == option }
}