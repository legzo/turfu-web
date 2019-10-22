package org.jtelabs

interface FiltreCombinaisons {
    fun filter(input: List<Combinaison>): List<Combinaison>
    val name: String
}

class FiltreDeFrequence(private val maxOccurences: Int) : FiltreCombinaisons {

    override fun filter(input: List<Combinaison>) =
        input.groupBy { it }
            .map { (combinaison, combinaisons) -> combinaison to combinaisons.count() }
            .filter { (_, count) -> count <= maxOccurences }
            .map { (combinaison, _) -> combinaison }

    override val name: String
        get() = "Filtre de frequence (max ${maxOccurences}occurences)"
}

class FiltreTopSynthese(private val numberOfPlaces: Int, private val synthese: Synthese) : FiltreCombinaisons {

    override fun filter(input: List<Combinaison>) =
        input.filter { combinaison ->
            combinaison.chevaux.count { cheval ->
                synthese.isHorseInTopPlaces(cheval = cheval, numberOfPlaces = numberOfPlaces)
            } == 1
        }

    override val name: String
        get() = "Filtre top $numberOfPlaces synthèse"

}

class FiltreNonPartants(private val nonPartants: List<Int>) : FiltreCombinaisons {

    override fun filter(input: List<Combinaison>) =
        input.filter { it.chevaux.none { cheval -> cheval in nonPartants } }

    override val name: String
        get() = "Filtre non partants : $nonPartants"

}
