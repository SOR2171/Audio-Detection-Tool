package com.github.sor2171.audiodetectiontool.core.ultils

import com.github.sor2171.audiodetectiontool.core.entity.NoteData
import com.github.sor2171.audiodetectiontool.core.entity.NoteNameStyle
import kotlin.math.log2
import kotlin.math.max
import kotlin.math.roundToInt

object NoteNameFormatter {
    val noteNamesSharp = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
    val noteNamesFlat = arrayOf("C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B")
    val solfegeNames =
        arrayOf("Do", "Do#", "Re", "Re#", "Mi", "Fa", "Fa#", "Sol", "Sol#", "La", "La#", "Si")

    fun getName(frequency: Float, base: Float, style: NoteNameStyle): NoteData {
        return when (style) {
            NoteNameStyle.Scientific -> convertToScientific(frequency, base)
            NoteNameStyle.Helmholtz -> convertToHelmholtz(frequency, base)
            NoteNameStyle.Solfege -> convertToSolfege(frequency, base)
        }
    }

    private fun getCalculatedData(frequency: Float, base: Float): Triple<Int, Int, Int> {
        val semitonesFromA4 = 12 * log2(frequency / base)
        val midiNoteFloat = semitonesFromA4 + 69
        val midiNote = midiNoteFloat.roundToInt()
        val cent = ((midiNoteFloat - midiNote) * 100).roundToInt()

        val noteIndex = (midiNote % 12).let { if (it < 0) it + 12 else it }
        val octave = (midiNote / 12) - 1
        return Triple(noteIndex, octave, cent)
    }

    private fun convertToScientific(frequency: Float, base: Float): NoteData {
        val (noteIndex, octave, cent) = getCalculatedData(frequency, base)
        return NoteData("${noteNamesSharp[noteIndex]}$octave", cent)
    }

    private fun convertToHelmholtz(frequency: Float, base: Float): NoteData {
        val (noteIndex, octave, cent) = getCalculatedData(frequency, base)
        val noteName = noteNamesSharp[noteIndex]

        val name = when {
            octave < 3 -> {
                val commas = ",".repeat(max(0, 2 - octave))
                "${noteName.uppercase()}$commas"
            }

            octave == 3 -> noteName.lowercase()
            else -> {
                val primes = "'".repeat(octave - 3)
                "${noteName.lowercase()}$primes"
            }
        }
        return NoteData(name, cent)
    }

    private fun convertToSolfege(frequency: Float, base: Float): NoteData {
        val (noteIndex, _, cent) = getCalculatedData(frequency, base)
        return NoteData(solfegeNames[noteIndex], cent)
    }
}