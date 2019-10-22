package org.jtelabs

typealias CombinaisonFilter = List<Combinaison>.() -> List<Combinaison>

inline class Combinaison(val chevaux: Set<Int>) {
    override fun toString() = chevaux.toString()
}

fun limitOccurencesTo(occurences: Int): CombinaisonFilter {
    return {
        this.groupBy { it }
            .map { (combinaison, combinaisons) -> combinaison to combinaisons.count() }
            .filter { (_, count) -> count <= occurences }
            .map { (combinaison, _)  ->  combinaison }
    }
}

fun filterWithTopPlaceFromSynthese(numberOfPlaces: Int, synthese: Synthese): CombinaisonFilter {
    return {
        this.filter { combinaison ->
            combinaison.chevaux.count { cheval ->
                synthese.isHorseInTopPlaces(cheval = cheval, numberOfPlaces = numberOfPlaces)
            } == 1
        }
    }
}

fun excludeNonPartants(nonPartants: List<Int>): CombinaisonFilter {
    return {
        this.filter { it.chevaux.none { cheval -> cheval in nonPartants } }
    }
}
