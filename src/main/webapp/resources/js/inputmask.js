function nitDuiMask(inputField) {
    var separator = '-';
    var nitPattern;
    if (inputField.value.length == 9) {
        nitPattern = new Array(8, 1);
    } else {
        nitPattern = new Array(4, 6, 3, 1);
    }
    mask(inputField, separator, nitPattern, true);
}

function mask(inputField, separator, pattern, nums) {
    if (inputField.valant != inputField.value) {
        val = inputField.value
        largo = val.length
        val = val.split(separator)
        val2 = ''
        for (r = 0; r < val.length; r++) {
            val2 += val[r]
        }
        if (nums) {
            for (z = 0; z < val2.length; z++) {
                if (isNaN(val2.charAt(z))) {
                    letra = new RegExp(val2.charAt(z), "g")
                    val2 = val2.replace(letra, "")
                }
            }
        }
        val = ''
        val3 = new Array()
        for (s = 0; s < pattern.length; s++) {
            val3[s] = val2.substring(0, pattern[s])
            val2 = val2.substr(pattern[s])
        }
        for (q = 0; q < val3.length; q++) {
            if (q == 0) {
                val = val3[q]
            } else {
                if (val3[q] != "") {
                    val += separator + val3[q]
                }
            }
        }
        inputField.value = val
        inputField.valant = val
    }
}