package com.easypark.models;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.easypark.models.EstacionamentoCheioDAO.getAll;
import static java.time.temporal.ChronoUnit.MINUTES;

public class EstacionamentoCheio {
    private LocalDate data;
    private LocalTime inicio;
    private LocalTime fim;
    private String tempoCheio;
    private long duracao;

    public long getDuracao() {
        return duracao;
    }

    public String getTempoCheio() {
        return tempoCheio;
    }


    public void setTempoCheio() {
        this.duracao = (MINUTES.between(this.inicio,this.fim));
        double m = (double) this.duracao / 60000;
        int mostraHora = 0;
        int mostraMinuto = (int) m;
        while (mostraMinuto > 59) {
            mostraMinuto -= 60;
            mostraHora++;
        }
        this.tempoCheio = Estacionamento.formatadorHoras(mostraHora,mostraMinuto);
    }

    public EstacionamentoCheio(LocalDate data, LocalTime inicio) {
        this.data = data;
        this.inicio = inicio;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalTime inicio) {
        this.inicio = inicio;
    }

    public LocalTime getFim() {
        return fim;
    }

    public void setFim(LocalTime fim) {
        this.fim = fim;
    }

    public static StringBuilder tempoMedioCheioGeral(long tempoEstacionamentoAberto) {
        StringBuilder retornaDataTempoPerCent = new StringBuilder();
        List<EstacionamentoCheio> ests = getAll();
        SortedSet<LocalDate> listaDatas = new TreeSet<>();
        for (EstacionamentoCheio est : ests) {
            listaDatas.add(est.data);
        }
        for (LocalDate l : listaDatas) {
            long minutosMesmoDiaCheio = 0;
            for (int i = 0; i < ests.size(); i++) {
               if (ests.get(i).data.equals(l)){
                   minutosMesmoDiaCheio = ests.get(i).getDuracao() + minutosMesmoDiaCheio;
               }
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
            double m = (double) minutosMesmoDiaCheio;
            DecimalFormat formato = new DecimalFormat("#.##");
            double media = (double) minutosMesmoDiaCheio / tempoEstacionamentoAberto*100;
            int mostraHora = 0;
            int mostraMinuto = (int) m;
            while (mostraMinuto > 59) {
                mostraMinuto -= 60;
                mostraHora++;
            }
            String exibeTempo = Estacionamento.formatadorHoras(mostraHora, mostraMinuto);
            retornaDataTempoPerCent.append("<div class='row'/>")
                    .append("<div class='cell'>").append(l.format(formatter)).append("</div>")
                    .append("<div class='cell'>").append(exibeTempo).append("</div>")
                    .append("<div class='cell'>").append(formato.format(media)).append("%").append("</div>").append("</div>");
        }
        return retornaDataTempoPerCent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EstacionamentoCheio that = (EstacionamentoCheio) o;

        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public String toString() {
        return data + tempoCheio;
    }
}
