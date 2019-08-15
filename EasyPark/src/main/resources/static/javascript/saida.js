statusSaida = document.getElementById("statusSaida");
if (statusSaida.innerHTML != "Saída não efetuada") {
    document.getElementById("veiculoSucesso").style.display = "block";
    statusSaida.style.color = "#9ecb4c";
}


statusEntrada = document.getElementById("statusEntrada");
if (statusEntrada.innerHTML != "Entrada de veículo não efetuada") {
    document.getElementById("veiculoSucesso").style.display = "block";
    statusEntrada.style.color = "#9ecb4c";
}