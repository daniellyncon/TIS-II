package com.easypark.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EstadaDAO implements SystemDAO<Estada, String> {

	public EstadaDAO() {
	}

	@Override
	public void add(Estada nova) {

		try (BufferedWriter buffer_saida = new BufferedWriter(new FileWriter("estada.txt", true))) {
			buffer_saida.write(nova.getDataEntrada() + "/");
			buffer_saida.write(nova.getHoraEntrada() + "/");
			buffer_saida.write(nova.getVeiculo().getPlaca() + "/");
			buffer_saida.write(nova.getVeiculo().getTipoVeiculo() + "/");
			if (nova.getDataSaida() != null) {
				buffer_saida.write(nova.getDataSaida() + "/");
				buffer_saida.write(nova.getHoraSaida() + "/");
				buffer_saida.write(nova.getTempoDePermanencia() + "/");
				buffer_saida.write(nova.getValorEstada() + "/");
			}
			buffer_saida.newLine();
			buffer_saida.flush();
		} catch (Exception e) {
			System.out.println("Erro ao gravar o Veiculo " + nova.getVeiculo().getPlaca() + "' no disco!");
			e.printStackTrace();
		}
	}

	@Override
	public Estada get(String key) {
		Estada est = null;
		try (BufferedReader buffer_entrada = new BufferedReader(new FileReader("estada.txt"))) {
			String line;
			while ((line = buffer_entrada.readLine()) != null) {
				if (line.contains(key)) {
					est = setValues(line);
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Erro ao ler o Veiculo de placa '" + key + "' do disco rigido!");
			e.printStackTrace();
		}
		return est;
	}

	private Estada setValues(String line) {
		String[] atributos = line.split("/");
		LocalDateTime dataEntrada = LocalDateTime.parse(atributos[0]);
		LocalTime horaEntrada = LocalTime.parse(atributos[1]);
		String placaVeiculo = atributos[2];
		String tipoVeiculo = atributos[3];
		Veiculo v = new Veiculo(placaVeiculo, tipoVeiculo);
		Estada est = new Estada(dataEntrada, horaEntrada, v);
		if (atributos.length > 4) {
			est.setDataSaida(LocalDateTime.parse(atributos[4]));
			est.setHoraSaida(LocalTime.parse(atributos[5]));
			est.setTempoDePermanencia(Double.parseDouble(atributos[6]));
			est.setValorEstada(Double.parseDouble(atributos[7]));
		}
		est.setVeiculo(v);
		return est;
	}

	@Override
	public List<Estada> getAll() {
		List<Estada> ests = new ArrayList<>();
		Estada est;
		try (BufferedReader buffer_entrada = new BufferedReader(new FileReader("estada.txt"))) {
			String line;
			while ((line = buffer_entrada.readLine()) != null) {
				est = setValues(line);
				ests.add(est);
			}
		} catch (Exception e) {
			System.out.println("Erro ao ler as Estadas do disco rigido!");
			e.printStackTrace();
		}
		return ests;
	}

	@Override
	public void update(Estada n) {

		List<Estada> ests = getAll();
		int index = ests.indexOf(n);
		System.out.println("Utualizado index: " + index);
		if (index != -1) {
			ests.set(index, n);
		}
		saveToFile(ests);

	}

	@Override
	public void delete(Estada est) {

		List<Estada> ests = getAll();
		int index = ests.indexOf(est);
		System.out.println("Deletado index: " + index);
		if (index != -1) {
			ests.remove(index);
		}
		saveToFile(ests);

	}

	private void saveToFile(List<Estada> ests) {
		try (BufferedWriter buffer_saida = new BufferedWriter(new FileWriter("estada.txt", false))) {
			for (Estada est : ests) {

				buffer_saida.write(est.getDataEntrada() + "/");
				buffer_saida.write(est.getHoraEntrada() + "/");
				buffer_saida.write(est.getVeiculo().getPlaca() + "/");
				buffer_saida.write(est.getVeiculo().getTipoVeiculo() + "/");
				if (est.getHoraSaida() != null) {
					buffer_saida.write(est.getDataSaida() + "/");
					buffer_saida.write(est.getHoraSaida() + "/");
					buffer_saida.write(est.getTempoDePermanencia() + "/");
					buffer_saida.write(est.getValorEstada() + "/");
				}
				buffer_saida.newLine();
				buffer_saida.flush();
			}
		} catch (Exception e) {
			System.out.println("Erro ao gravar a Estada no disco!");
			e.printStackTrace();
		}
	}

	public void instanciaEstadas(Estacionamento estacionamento) {
		try (BufferedReader buffer_entrada = new BufferedReader(new FileReader("estada.txt"))) {
			String line, dataEntradaStr, horaEntradaStr, placaVeiculo, tipoVeiculo;
			String[] lineStr;
			Veiculo veiculo;
			Estada estada;

			System.out.println("\n\nInstancias: \n");
			while ((line = buffer_entrada.readLine()) != null) {
				lineStr = line.split("/");
				dataEntradaStr = lineStr[0];
				horaEntradaStr = lineStr[1];
				placaVeiculo = lineStr[2];
				tipoVeiculo = lineStr[3];

				/* Converte String para Datetime */
				LocalDateTime dataEntrada = LocalDateTime.parse(dataEntradaStr);
				LocalTime horaEntrada = LocalTime.parse(horaEntradaStr);

				/* Intancia Veiculo */
				veiculo = new Veiculo(placaVeiculo, tipoVeiculo);

				/* Intancia Estada */
				estada = new Estada(dataEntrada, horaEntrada, veiculo);

				/* Adiciona a estada na lista de estadas do Estacionamento */
				estacionamento.getEstadaList().put(placaVeiculo, estada);
				estacionamento.setQtdVeiculosEstacionados(estacionamento.getEstadaList().size());
				System.out.println("Data Entrada: " + dataEntradaStr);
				System.out.println("Hora Entrada: " + horaEntradaStr);
				System.out.println("Placa: " + placaVeiculo);
				System.out.println("Tipo: " + tipoVeiculo + "\n");
			}
		} catch (Exception e) {
			System.out.println("Erro ao ler as Estadas do disco rigido!");
			e.printStackTrace();
		}
	}
	
    public void adicionaEstadasGeral(Estada nova) {
        try (BufferedWriter buffer_saida = new BufferedWriter(new FileWriter("estadasGeral.txt", true))) {
            buffer_saida.write(nova.getDataEntrada() + "/");
            buffer_saida.write(nova.getHoraEntrada() + "/");
            buffer_saida.write(nova.getVeiculo().getPlaca() + "/");
            buffer_saida.write(nova.getVeiculo().getTipoVeiculo() + "/");
            buffer_saida.write(nova.getDataSaida() + "/");
            buffer_saida.write(nova.getHoraSaida() + "/");
            buffer_saida.write(nova.getTempoDePermanencia() + "/");
            buffer_saida.write(nova.getValorEstada() + "/");
            buffer_saida.newLine();
            buffer_saida.flush();

        } catch (Exception e) {
            System.out.println("Erro ao gravar o Veiculo " + nova.getVeiculo().getPlaca() + "' no disco!");
            e.printStackTrace();
        }
    }

    public List<Estada> recuperaEstadasGeral() {
        List<Estada> ests = new ArrayList<>();
        Estada est;
        try (BufferedReader buffer_entrada = new BufferedReader(new FileReader("estadasGeral.txt"))) {
            String line;
            while ((line = buffer_entrada.readLine()) != null) {
                est = setValues(line);
                ests.add(est);
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler as Estadas do disco rigido!");
            e.printStackTrace();
        }
        return ests;
    }

}
