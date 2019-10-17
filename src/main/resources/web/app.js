let myapp = (function() {

    let getValues = function() {

        let pronos = document.getElementById("pronos");
        let pronos_value = pronos.value;

        let occurences = document.getElementById("occurences");
        let occurences_value = occurences.options[occurences.selectedIndex].value;

        let topXSynthese = document.getElementById("topXSynthese");
        let topXSynthese_value = topXSynthese.options[topXSynthese.selectedIndex].value;

        let nonPartants = document.getElementById("nonPartants");
        let nonPartants_value = nonPartants.value;

        return {
            "pronos" : pronos_value,
            "occurences" : occurences_value,
            "topXSynthese" : topXSynthese_value,
            "nonPartants" : nonPartants_value
        }
    }

    let displayResults = function(e) {
        console.table(getValues())
        e.preventDefault()
    }

    document.addEventListener("DOMContentLoaded", function(event) {
        let generateButton = document.getElementById('generate');
        generateButton.onclick = displayResults;

    });


})()


