package com.github.sor2171.audiodetectiontool.core.ultils

import com.github.sor2171.audiodetectiontool.core.entity.NoteNameStyle

object Const {
    val symbolOfNoteStyle = mapOf(
        NoteNameStyle.Scientific to "A4",
        NoteNameStyle.Helmholtz to "a'",
        NoteNameStyle.Solfege to "La4"
    )
}