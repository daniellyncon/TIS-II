package com.easypark.models;



public class Valores {

    private Double valorHora;
    private Double fracaoQuinze;
    private Double valorDiaria;
    private Double valorMensal;
    private Double perNoite;

    public Valores(double valorHora, double fracaoQuinze, double valorDiaria, double valorMensal, double perNoite) {
        this.valorHora = valorHora;
        this.fracaoQuinze = fracaoQuinze;
        this.valorDiaria = valorDiaria;
        this.valorMensal = valorMensal;
        this.perNoite = perNoite;
    }

    public double getValorHora() {
        return valorHora;
    }

    public void setValorHora(double valorHora) {

        this.valorHora = valorHora;
    }

    public double getFracaoQuinze() {
        return fracaoQuinze;
    }

    public void setFracaoQuinze(double fracaoQuinze) {
        this.fracaoQuinze = fracaoQuinze;
    }

    public double getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public double getValorMensal() {
        return valorMensal;
    }

    public void setValorMensal(double valorMensal) {
        this.valorMensal = valorMensal;
    }

    public double getPerNoite() {
        return perNoite;
    }

    public void setPerNoite(double perNoite) {
        this.perNoite = perNoite;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--\t\tValores\t\t--\n").append("- Hora: R$").append(getValorHora()).append("\n");
        if (this.fracaoQuinze != 0)
                sb.append("- Fracao 15 minutos: R$").append(getFracaoQuinze()).append("\n");
        if (this.valorDiaria != 0)
                sb.append("- Diaria: R$").append(getValorDiaria()).append("\n");
        if (this.valorMensal != 0)
                sb.append("- Mensal: R$").append(getValorMensal()).append("\n");
        if (this.perNoite != 0)
                sb.append("- Pernoite: R$").append(getPerNoite());
        return sb.toString();
    }
}