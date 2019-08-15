statusEntrada = document.getElementById("statusEntrada");
if (statusEntrada.innerHTML != "Entrada de veículo não efetuada") {
    document.getElementById("veiculoSucesso").style.display = "block";
    statusEntrada.style.color = "#9ecb4c";
}