package com.easypark.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EstacionamentoCheioDAO {

    public static EstacionamentoCheio get(LocalDate chave) {
        EstacionamentoCheio e = null;
        try (BufferedReader buffer_entrada = new BufferedReader(new FileReader("estacionamentoCheio.txt"))) {
            String line;
            while ((line = buffer_entrada.readLine()) != null) {
                if (line.contains(chave.toString())) {
                    e = setValues(line);
                    break;
                }
            }
        } catch (Exception exception) {
            System.out.println("Erro ao ler o Veiculo de placa '" + chave + "' do disco rigido!");
            exception.printStackTrace();
        }
        return e;
    }


    public static void add(EstacionamentoCheio e) {
        try (BufferedWriter buffer_saida = new BufferedWriter(new FileWriter("estacionamentoCheio.txt", true))) {
            buffer_saida.write(e.getData() + "/");
            buffer_saida.write(e.getInicio() + "/");
            buffer_saida.write(e.getFim() + "/");
            buffer_saida.write(e.getTempoCheio() + "/");
            buffer_saida.newLine();
            buffer_saida.flush();
        } catch (Exception exception) {
            System.out.println("Erro ao gravar o estacionamentoCheio " + e + "' no disco!");
            exception.printStackTrace();
        }
    }


    public static List getAll() {
        List<EstacionamentoCheio> e = new ArrayList<>();
        EstacionamentoCheio estacionamentoCheio;
        try (BufferedReader buffer_entrada = new BufferedReader(new FileReader("estacionamentoCheio.txt"))) {
            String line;
            while ((line = buffer_entrada.readLine()) != null) {
                estacionamentoCheio = setValues(line);
                e.add(estacionamentoCheio);
            }
        } catch (Exception exception) {
            System.out.println("Erro ao ler as Estadas do disco rigido!");
            exception.printStackTrace();
        }
        return e;
    }

    private static EstacionamentoCheio setValues(String line) {
        String[] atributos = line.split("/");
        LocalDate d = LocalDate.parse(atributos[0]);
        LocalTime i = LocalTime.parse(atributos[1]);
        LocalTime f = LocalTime.parse(atributos[2]);
        String t = atributos[3];
        EstacionamentoCheio e = new EstacionamentoCheio(d,i);
        e.setFim(f);
        e.setTempoCheio();
        return e;
    }
}
