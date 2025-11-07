function onClick(nombreBoton) {
    $('#' + nombreBoton)[0].click();
}

function limitarNumero(input, maxDecimales) {
    const valor = input.value;
    const partes = valor.split('.');
    if (partes.length > 2) {
        input.value = partes[0] + '.' + partes[1]; // Solo un punto permitido
    } else if (partes.length === 2 && partes[1].length > maxDecimales) {
        input.value = partes[0] + '.' + partes[1].substring(0, maxDecimales);
    }
}