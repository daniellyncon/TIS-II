package com.easypark.models;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Estada {
    private LocalDateTime dataEntrada;
    private LocalTime horaEntrada;
    private Veiculo veiculo;
    private LocalDateTime dataSaida;
    private LocalTime horaSaida;
    private double tempoDePermanencia;
    private double valorEstada;

    public Estada(LocalDateTime dataEntrada, LocalTime horaEntrada, Veiculo veiculo) {
        this.dataEntrada = dataEntrada;
        this.horaEntrada = horaEntrada;
        this.veiculo = veiculo;
    }

    public Estada(LocalDateTime dataEntrada, LocalTime horaEntrada, Veiculo veiculo, LocalDateTime dataSaida, LocalTime horaSaida,
                  long tempoDePermanencia, double valorEstada) {
        this.dataEntrada = dataEntrada;
        this.veiculo = veiculo;
        this.horaEntrada = horaEntrada;
        this.dataSaida = dataSaida;
        this.horaSaida = horaSaida;
        this.tempoDePermanencia = tempoDePermanencia;
        this.valorEstada = valorEstada;
    }

    public Estada() {
    }

    public Map<String, Object> saidaVeiculo(Estacionamento estacionamento, String placa) {

        Map<String, Object> infoSaidaVeiculo = new HashMap<>();
        Estada estadaVeiculo = estacionamento.getEstadaVeiculo(placa);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm");
        estadaVeiculo.setDataSaida(LocalDateTime.now());
        estadaVeiculo.setHoraSaida(LocalTime.now());

        long begin = estadaVeiculo.getDataEntrada().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
        long end = estadaVeiculo.getDataSaida().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
        long diferencaMili = end - begin;

        double minutosPermanecidos = (double) diferencaMili / 60000;
        estadaVeiculo.setTempoDePermanencia(minutosPermanecidos);
        double horasPermanecidas = (double) diferencaMili / 3600000;

        System.out.println("Imprimindo minutos: " + minutosPermanecidos);
        System.out.println("Imprimindo horas: " + horasPermanecidas);

        estadaVeiculo.calculaValor(horasPermanecidas, minutosPermanecidos, estacionamento.getValorHora());

        int mostraHora = 0;
        int mostraMinuto = (int) diferencaMili / 60000;
        while (mostraMinuto > 59) {
            mostraMinuto -= 60;
            mostraHora++;
        }

        String tempoPermanecido  = Estacionamento.formatadorHoras(mostraHora, mostraMinuto);

        //View
        infoSaidaVeiculo.put("tempoPermanecido", tempoPermanecido);
        infoSaidaVeiculo.put("dataHoraEntrada", estadaVeiculo.getDataEntrada().format(formatter));
        infoSaidaVeiculo.put("dataHoraSaida", estadaVeiculo.getDataSaida().format(formatter));
        infoSaidaVeiculo.put("tipoVeiculo", estadaVeiculo.getVeiculo().getTipoVeiculo());
        infoSaidaVeiculo.put("valorAPagar", estadaVeiculo.getValorEstada());

        estacionamento.getEstadaList().remove(placa);
        estacionamento.encerraContagem();
        estacionamento.setQtdVeiculosEstacionados(estacionamento.getEstadaList().size());
        EstadaDAO estadaDAO = new EstadaDAO();
        estadaDAO.update(estadaVeiculo);
        estadaDAO.adicionaEstadasGeral(estadaVeiculo);
        return infoSaidaVeiculo;
    }


    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(LocalTime horaSaida) {
        this.horaSaida = horaSaida;
    }

    public double getValorEstada() {
        return valorEstada;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public String getTipoVeiculo() {
        return veiculo.getTipoVeiculo();
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public void setValorEstada(Double valorEstada) {
        DecimalFormat formato = new DecimalFormat("#.##");
        valorEstada = Double.valueOf(formato.format(valorEstada).replace(",", "."));
        this.valorEstada = valorEstada;
    }

    public void calculaValor(double horasPermanecidas, double minutosPermanecidos, double valorHora) {
        if (minutosPermanecidos <= 59) {
            setValorEstada((valorHora / 60) * minutosPermanecidos);
        } else {
            setValorEstada(((valorHora / 60) * minutosPermanecidos) + (valorHora * horasPermanecidas));
        }
    }

    public double getTempoDePermanencia() {
        return tempoDePermanencia;
    }

    public void setTempoDePermanencia(double tempoDePermanencia) {
        this.tempoDePermanencia = tempoDePermanencia;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.dataEntrada).append("/").append(this.horaEntrada).append("/")
                .append(this.veiculo.getPlaca()).append("/").append(this.veiculo.getTipoVeiculo()).append("/");
        if (this.getDataSaida() != null) {
            sb.append(this.dataSaida).append("/").append(this.horaSaida).append("/")
                    .append(this.tempoDePermanencia).append("/").append(this.valorEstada);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estada estada = (Estada) o;
        return Objects.equals(veiculo.getPlaca(), estada.veiculo.getPlaca());
    }


    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public String tempoAtualEstada() {
        long inicio = this.getDataEntrada().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
        long fim = LocalDateTime.now().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
        long diferencaMili = fim - inicio;
        int minutosPermanecidos = (int) diferencaMili / 60000;
        int horasPermanecidas = minutosPermanecidos / 60;
        minutosPermanecidos = minutosPermanecidos % 60;
        return Estacionamento.formatadorHoras(horasPermanecidas,minutosPermanecidos);
    }

    public double taxaDeRetorno(String placa) {
        EstadaDAO estadaDAO = new EstadaDAO();
        double quant = 0;
        List<Estada> result = estadaDAO.recuperaEstadasGeral();
        quant = result.stream()
                .filter(x -> x.getVeiculo().getPlaca().equals(placa))
                .mapToInt(w -> w.getVeiculo().getContadorDeVezes())
                .average().getAsDouble();
        return (quant / 30) * 100;

    }


}
