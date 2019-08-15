var url_atual = window.location.href;
console.log(url_atual);

nomeEstabelecimento = document.getElementById("nomeEstabelecimento").innerHTML;
infoEstabelecimento = document.getElementById("setInfoEstacionamento");
v1 = document.getElementById("v1");
v2 = document.getElementById("v2");
v3 = document.getElementById("v3");
v4 = document.getElementById("v4");
v5 = document.getElementById("v5");
v6 = document.getElementById("v6");
divInfo = document.getElementById("divInfo");

if (nomeEstabelecimento == "") {
    v1.style.display = 'none';
    v2.style.display = 'none';
    v3.style.display = 'none';
    v4.style.display = 'none';
    v5.style.display = 'none';
    v6.style.display = 'none';
    infoEstabelecimento.innerHTML = "Cadastrar Estabelecimento";
    divInfo.style.display = 'none';
}