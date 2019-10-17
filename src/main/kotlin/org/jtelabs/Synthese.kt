package org.jtelabs

private const val TOP_POINTS = 8

inline class Synthese(val lignes: List<SyntheseLine>) {

    fun isHorseInTopPlaces(cheval: Int, numberOfPlaces: Int) =
        this.lignes
            .take(numberOfPlaces)
            .any { it.cheval == cheval }

}
data class ScoreLine(val cheval: Int, val points: Int)

data class SyntheseLine(val cheval: Int, val points: Int) {
    override fun toString() = "(cheval $cheval -> $points points)"
}

fun List<Pronostic>.toSynthese() = Synthese(
    this
        .flatMap { prono -> scoreLinesFor(prono) }
        .groupBy { it.cheval }
        .map { (cheval, scoreLines) -> SyntheseLine(cheval = cheval, points = scoreLines.sumBy { it.points }) }
        .sortedByDescending { it.points }
)

private fun scoreLinesFor(prono: Pronostic) =
    prono.chevaux.mapIndexed { index, cheval ->
        ScoreLine(cheval, TOP_POINTS - index)
    }
