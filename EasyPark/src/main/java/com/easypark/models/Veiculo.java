package com.easypark.models;

import java.util.ArrayList;
import java.util.List;

public class Veiculo {

    private String placa;
    private String tipoVeiculo;
	private int contadorDeVezes;
    //List<Estada> estadas = new ArrayList<>();

    public Veiculo(String placa, String tipoVeiculo) {
		this.setPlaca(placa);
		this.setTipoVeiculo(tipoVeiculo);
	}
    
    public Veiculo() {
    	
    }

	public String getPlaca() {
        return placa;
    }

	public String getTipoVeiculo() {
		return tipoVeiculo;
	}

	public void setTipoVeiculo(String tipo) {
		this.tipoVeiculo = tipo;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public int getContadorDeVezes() {
		return contadorDeVezes;
	}
	public void setContadorDeVezes() {
		contadorDeVezes++;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(placa).append(";").append(tipoVeiculo).toString();
	}
    

}