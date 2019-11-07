package org.jtelabs

interface FiltreCombinaisons {
    fun filter(input: List<Combinaison>): List<Combinaison>
    val name: String
}

class FiltreOccurences(private val occurences: Int) : FiltreCombinaisons {

    override fun filter(input: List<Combinaison>) =
        input.groupBy { it }
            .mapValues { (_, combinaisons) -> combinaisons.count() }
            .filter { (_, count) -> count == occurences }
            .keys.toList()

    override val name: String
        get() = "Filtre de frequence ($occurences occurences)"
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

class ExcludeNonPartants(private val nonPartants: List<Int>) : FiltreCombinaisons {

    override fun filter(input: List<Combinaison>) =
        input.filter { it.chevaux.none { cheval -> cheval in nonPartants } }

    override val name: String
        get() = "Filtre non partants : $nonPartants"
}

class ExcludeTousSimilaires : FiltreCombinaisons {

    private fun Collection<Int>.allEven() = this.all { it % 2 == 0 }
    private fun Collection<Int>.allOdd() = this.all { it % 2 != 0 }

    override fun filter(input: List<Combinaison>) =
        input.filter { !it.chevaux.allEven() && !it.chevaux.allOdd() }

    override val name: String
        get() = "Filtre pas tous pairs/impairs"
}

class FiltreUnParmi(private val chevauxSpecifies: List<Int>) : FiltreCombinaisons {

    override fun filter(input: List<Combinaison>) = when (chevauxSpecifies.size) {
        0 -> input
        else -> input.filter { it.chevaux.any { cheval -> cheval in chevauxSpecifies } }
    }

    override val name: String
        get() = "Filtre au moins un cheval parmi $chevauxSpecifies"
}
