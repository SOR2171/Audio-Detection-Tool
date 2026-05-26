package com.github.sor2171.audiodetectiontool.core.entity

data class NoteData<T : Number>(
    val name: String,
    val cent: T
) {
    fun intCent() = NoteData(name, cent.toInt())
    fun floatCent() = NoteData(name, cent.toFloat())
}
