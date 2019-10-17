let myapp = (function() {

    let getValues = function() {
        let pronos = document.getElementById("pronos").value
        let parsedPronos = pronos.replace(/\n/g,";");

        return {
            "pronos" : parsedPronos,
            "occurences" : getValueFromSelect("occurences"),
            "topXSynthese" : getValueFromSelect("topXSynthese"),
            "nonPartants" : document.getElementById("nonPartants").value
        }
    }

    let getValueFromSelect = function(selectId) {
        let select = document.getElementById(selectId);
        return select.options[select.selectedIndex].value;
    }

    let getResult = function() { return document.getElementById("result") }

    let scrollTo = function(hash) {
        location.hash = "#" + hash;
    }

    let fillResultWith = function(text) {
        getResult().innerHTML = text
    }

    let generate = function(e) {
        let values = getValues();

        var xhr = new XMLHttpRequest();

        // Setup our listener to process completed requests
        xhr.onload = function () {
        	if (xhr.status >= 200 && xhr.status < 300) {
        		console.log("✅ Done!");
        		scrollTo("result");
        		fillResultWith(xhr.responseText)
        	} else { console.log("😥 Oh no! : " + xhr.responseText); }
        };

        fillResultWith("Chargement...");

        xhr.open('POST', `/api/generate?pronos=${values.pronos}&occurences=${values.occurences}&topXSynthese=${values.topXSynthese}&nonPartants=${values.nonPartants}`);
        xhr.send();

        e.preventDefault()
    }

    document.addEventListener("DOMContentLoaded", function(event) {
        let generateButton = document.getElementById('generate');
        generateButton.onclick = generate;
    });

})()


