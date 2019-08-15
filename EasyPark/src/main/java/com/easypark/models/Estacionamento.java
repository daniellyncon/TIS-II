package com.easypark.models;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static java.time.temporal.ChronoUnit.MINUTES;

@Component
public class Estacionamento {
    private String nomeEstabelecimento;
    private LocalTime horaAbertura;
    private LocalTime horaFechamento;
    private int quantidadeVagas;
    private int qtdVeiculosEstacionados;
    private Map<String, Estada> mapEstadasAtual;
    private Double valorHora;
    private LocalTime estacionamentoCheio;

    public Estacionamento(String nomeEstabelecimento, LocalTime horaAbertura, LocalTime horaFechamento, int quantidadeVagas, Double valorHora) {
        this.nomeEstabelecimento = nomeEstabelecimento;
        this.horaAbertura = horaAbertura;
        this.horaFechamento = horaFechamento;
        this.quantidadeVagas = quantidadeVagas;
        this.valorHora = valorHora;
        this.mapEstadasAtual = new HashMap<>();
        this.qtdVeiculosEstacionados = 0;
    }

    public Estacionamento() {
        this.mapEstadasAtual = new HashMap<>();
    }

    public String getNomeEstabelecimento() {
        return nomeEstabelecimento;
    }

    public void setNomeEstabelecimento(String nome) {
        this.nomeEstabelecimento = nome;
    }

    public LocalTime getHoraAbertura() {
        return horaAbertura;
    }

    public void setHoraAbertura(LocalTime horaAbertura) {
        this.horaAbertura = horaAbertura;
    }

    public LocalTime getHoraFechamento() {
        return horaFechamento;
    }

    public void setHoraFechamento(LocalTime horaFechamento) {
        this.horaFechamento = horaFechamento;
    }

    public int getQuantidadeVagas() {
        return quantidadeVagas;
    }

    public void setQuantidadeVagas(int qtdVagas) {
        this.quantidadeVagas = qtdVagas;
    }

    public int getQtdVeiculosEstacionados() {
        return qtdVeiculosEstacionados;
    }

    public void setQtdVeiculosEstacionados(int qtdVeiculosEstacionados) {
        this.qtdVeiculosEstacionados = qtdVeiculosEstacionados;
    }

    public Double getValorHora() {
        return valorHora;
    }

    public void setValorHora(Double valorHora) {
        this.valorHora = valorHora;
    }

    public int calcQtdeVagasLivres() {
        System.out.println(this.quantidadeVagas - this.qtdVeiculosEstacionados);
        return this.quantidadeVagas - this.qtdVeiculosEstacionados;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(this.getNomeEstabelecimento()).append(";").append(this.horaAbertura).append(";")
                .append(this.horaFechamento).append(";").append(this.getQuantidadeVagas()).append(";")
                .append(this.getValorHora()).append(";").append(this.getEstadaList()).toString();
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Estacionamento that = (Estacionamento) o;

        return nomeEstabelecimento.equals(that.nomeEstabelecimento);

    }

    public Map<String, Object> entradaVeiculo(Veiculo veiculo) {
        Map<String, Object> infoEntradaVeiculo = new HashMap<>();
        veiculo.setContadorDeVezes();
        if (this.mapEstadasAtual == null) {
            this.mapEstadasAtual = new HashMap<>();
        }

        /* Formatacao String to DateTime */

        LocalDateTime dataEntrada = LocalDateTime.now();
        LocalTime horaEntrada = LocalTime.now();
        Estada novaEstada = new Estada(dataEntrada, horaEntrada, veiculo);
        this.getEstadaList().put(veiculo.getPlaca(), novaEstada);
        this.setQtdVeiculosEstacionados(this.getEstadaList().size());
        mapEstadasAtual.put(novaEstada.getVeiculo().getPlaca(), novaEstada);
        this.setQtdVeiculosEstacionados(this.mapEstadasAtual.size());

        infoEntradaVeiculo.put("placaVeiculo", veiculo.getPlaca());
        infoEntradaVeiculo.put("tipoVeiculo", veiculo.getTipoVeiculo());
        infoEntradaVeiculo.put("dataEntrada", dataEntrada);
        infoEntradaVeiculo.put("horaEntrada", horaEntrada);
        infoEntradaVeiculo.put("novaEstada", novaEstada);
        System.out.println("Exibindo estada no momento da insercao inicial" + novaEstada);
        EstadaDAO estadaDAO = new EstadaDAO();
        estadaDAO.add(novaEstada);
        this.iniciaContagemEstacionamentoCheio();
        return infoEntradaVeiculo;
    }

    public Map<String, Estada> getEstadaList() {
        return mapEstadasAtual;
    }

    public Estada getEstadaVeiculo(String placa) {
        return this.mapEstadasAtual.get(placa);
    }

    public void setEstadaList(Map<String, Estada> estadaList) {
        this.mapEstadasAtual = estadaList;
    }

    public static String formatadorHoras(int horas, int minutos) {
        StringBuilder mediaHorasParaExibir = new StringBuilder();
        if (horas < 10) {
            mediaHorasParaExibir.append("0").append(horas).append(":");
        } else {
            mediaHorasParaExibir.append(horas).append(":");
        }
        if (minutos < 10) {
            mediaHorasParaExibir.append("0").append(minutos);
        } else {
            mediaHorasParaExibir.append(minutos);
        }
        return mediaHorasParaExibir.toString();
    }

    public double mediaTempoPermanecido() {
        EstadaDAO estadaDAO = new EstadaDAO();
        List<Estada> result = estadaDAO.recuperaEstadasGeral();
        return result.stream()
                .filter(t -> t.getDataSaida() != null)
                .mapToDouble(Estada::getTempoDePermanencia)
                .average()
                .getAsDouble();
    }

    public StringBuilder exibeVeiculos(EstadaDAO estadaDAO) {
        StringBuilder veiculos = new StringBuilder();
        List<Estada> veiculosEstacionados = estadaDAO.getAll();
        // DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");
        for (Estada e : veiculosEstacionados) {
            veiculos.append("<div class='row'/>")
                    .append("<div class='cell'>").append(e.getVeiculo().getPlaca()).append("</div>")
                    .append("<div class='cell'>").append(e.getVeiculo().getTipoVeiculo()).append("</div>")
                    .append("<div class='cell'>").append(e.tempoAtualEstada()).append("</div>").append("</div>");
        }
        return veiculos;
    }

    public float taxaDeRetorno(String placa) {
		 EstadaDAO estadaDAO = new EstadaDAO();
	        Estada xv = new Estada();
	        long quant = 0;
	        
	        List<Estada> result = estadaDAO.recuperaEstadasGeral();
	        System.out.println(result);
	       
	        quant =result.stream()
	                .filter(x -> x.getVeiculo().getPlaca().equals(placa)).count();
	        
//	                .mapToInt(y->y.getContadorDeVezes()).hashCode());
//	                .mapToInt(y -> y.getContadorDeVezes()).findFirst().getAsInt());
	                ;
	        System.out.println(quant);
	        return (quant*100)/30;

   }
    public double permanenciaEstadasPorMes(EstadaDAO estadaDAO, int mesEscolhido) {
        List<Estada> estadas = estadaDAO.recuperaEstadasGeral();
        double media = estadas.stream().filter(t -> t.getDataEntrada().getMonth().equals(Month.of(mesEscolhido)))
                .mapToDouble(Estada::getTempoDePermanencia)
                .average()
                .getAsDouble();
        return media;
    }

    public StringBuilder porcentagemTipoVeiculo(EstadaDAO estadaDAO) {
        List<Estada> result = estadaDAO.getAll();

        double porcentagemCarros = (double) (result.stream().filter(t -> t.getVeiculo().getTipoVeiculo().equals("Carro")).count()) / result.size() * 100;
        double porcentagemMotos = (double) (result.stream().filter(t -> t.getVeiculo().getTipoVeiculo().equals("Moto")).count()) / result.size() * 100;
        double porcentagemCaminhonete = (double) (result.stream().filter(t -> t.getVeiculo().getTipoVeiculo().equals("Caminhonete")).count()) / result.size() * 100;

        StringBuilder barraProgresso = new StringBuilder();

        DecimalFormat df = new DecimalFormat("##.##");

        barraProgresso.append("<div class='progress-bar progress-bar-success' role='progressbar' style='width:" + porcentagemCarros + "%'> Carro (" + df.format(porcentagemCarros) + "%) </div>")
                .append("<div class='progress-bar progress-bar-warning' role='progressbar' style='width:" + porcentagemMotos + "%'> Moto (" + df.format(porcentagemMotos) + "%) </div>")
                .append("<div class='progress-bar progress-bar-danger' role='progressbar' style='width:" + porcentagemCaminhonete + "%'> Caminhonete (" + df.format(porcentagemCaminhonete) + "%) </div>");

        return barraProgresso;
    }

    public int totalCarrosEstacionados(EstadaDAO estadaDAO){
        List<Estada> result = estadaDAO.recuperaEstadasGeral();
        System.out.println(result.size());
        System.out.println(getEstadaList().size());
        System.out.println(result.size() + getEstadaList().size());
        return result.size() + getEstadaList().size();
    }

    public double porcentagemCarrosGeral(EstadaDAO estadaDAO) {
        List<Estada> result = estadaDAO.recuperaEstadasGeral();
        return (double) (result.stream().filter(t -> t.getVeiculo()
                .getTipoVeiculo().equals("Carro"))
                .count()) / result.size() * 100;
    }

    public double porcentagemMotosGeral(EstadaDAO estadaDAO) {
        List<Estada> result = estadaDAO.recuperaEstadasGeral();
        return (double) (result.stream().filter(t -> t.getVeiculo()
                .getTipoVeiculo().equals("Moto"))
                .count()) / result.size() * 100;

    }

    public double porcentagemCaminhonetesGeral(EstadaDAO estadaDAO) {
        List<Estada> result = estadaDAO.recuperaEstadasGeral();
        return (double) (result.stream().filter(t -> t.getVeiculo()
                .getTipoVeiculo().equals("Caminhonete"))
                .count()) / result.size() * 100;
    }


    public double mediaArrecadacaoHora() {
        EstadaDAO estadaDAO = new EstadaDAO();
        List<Estada> result = estadaDAO.recuperaEstadasGeral();
        double media = result.stream()
                .filter(t -> t.getDataSaida() != null)
                .mapToDouble(Estada::getValorEstada)
                .average().getAsDouble();
        double horasFuncionamento = (double) (MINUTES.between(this.getHoraAbertura(), this.getHoraFechamento()) / 60);
        return (media / horasFuncionamento);
    }

    public LocalTime getEstacionamentoCheio() {
        return estacionamentoCheio;
    }

    public void setEstacionamentoCheio(LocalTime estacionamentoCheio) {
        this.estacionamentoCheio = estacionamentoCheio;
    }

    public void iniciaContagemEstacionamentoCheio() {
        if (calcQtdeVagasLivres() == 0) {
            this.estacionamentoCheio = LocalTime.now();
        }
    }

    public void encerraContagem() {
        if (calcQtdeVagasLivres() == 0) {
            EstacionamentoCheio e = new EstacionamentoCheio(LocalDate.now(), this.estacionamentoCheio);
            e.setFim(LocalTime.now());
            e.setTempoCheio();
            EstacionamentoCheioDAO.add(e);
            this.qtdVeiculosEstacionados--;
        }
    }
}
