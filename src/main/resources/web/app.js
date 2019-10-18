(function() {

    let getValues = function() {
        let pronos = getPronosField().value;
        let parsedPronos = pronos.replace(/\n/g,";");

        return {
            "pronos" : parsedPronos,
            "occurences" : getValueFromSelect("occurences"),
            "topXSynthese" : getValueFromSelect("topXSynthese"),
            "nonPartants" : document.getElementById("nonPartants").value
        }
    };

    function getValueFromSelect(selectId) {
        let select = document.getElementById(selectId);
        return select.options[select.selectedIndex].value;
    }

    function getGenerateButton() { return document.getElementById('generate') }
    function getLogo() { return document.getElementById('logo') }
    function getPronosField() { return document.getElementById('pronos') }
    function getResultTextarea() { return document.getElementById("result") }

    function fillResultWith(text) { getResultTextarea().innerHTML = text }

    let generate = function(e) {
        let values = getValues();

        const xhr = new XMLHttpRequest();

        // Setup our listener to process completed requests
        xhr.onload = function () {
            if (xhr.status >= 200 && xhr.status < 300) {
                console.log("✅ Done!");
                fillResultWith(xhr.responseText);
                getResultTextarea().scrollIntoView({behavior: 'smooth'});
            } else { console.log("😥 Oh no! : " + xhr.responseText); }
        };

        fillResultWith("Chargement...");

        xhr.open('POST', `/api/generate?pronos=${values.pronos}&occurences=${values.occurences}&topXSynthese=${values.topXSynthese}&nonPartants=${values.nonPartants}`);
        xhr.send();

        e.preventDefault()
    };

    function fillPronos() {
        getPronosField().value = `P1 1 5 8 6 4 9 15 2
P2 1 5 8 11 6 4 9 15
P3 1 9 5 8 4 15 7 6
P4 5 9 1 8 6 4 15 10
P5 5 1 6 4 8 9 3 2
P6 5 6 1 8 11 4 14 15
P7 1 5 6 8 2 3 9 10
P8 1 5 8 6 4 9 15 2
P9 5 1 8 6 4 9 15 2
P10 5 1 8 15 6 4 9 2
P11 1 8 5 9 6 4 15 2
P12 4 13 6 9 5 1 15 2
P13 4 9 8 5 1 6 15 2
P14 6 5 1 8 4 9 15 2
P15 8 4 1 6 9 15 2 5
P16 1 8 4 5 9 6 15 10
P17 9 5 8 4 1 6 15 2
P18 5 2 1 8 7 10 6 4
P19 5 9 8 1 12 4 7 15
P20 1 8 6 4 5 11 9 15
P21 4 9 10 1 5 8 6 15`;
    }

    document.addEventListener("DOMContentLoaded", function() {
        getGenerateButton().onclick = generate;
        getLogo().onclick = fillPronos
    });

})();


