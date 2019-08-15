package com.easypark.controllers;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static java.time.temporal.ChronoUnit.MINUTES;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import com.easypark.models.*;


@Controller
public class EstacionamentoController {
    private EstacionamentoDAO estacionamentoDAO = new EstacionamentoDAO();
    private Estacionamento estacionamentoModel = new Estacionamento();
    private EstadaDAO estadaDAO = new EstadaDAO();

    private static void criaArquivo() throws IOException {
        File f = new File("estada.txt");
        if (!f.exists()) {
            f.createNewFile();
        }
        File x = new File("estacionamento.txt");
        if (!x.exists()) {
            x.createNewFile();
        }
        File g = new File("estacionamentoCheio.txt");
        if (!g.exists()){
            g.createNewFile();
        }
        File z = new File("estadasGeral.txt");
        if (!z.exists()) {
            z.createNewFile();
        }
    }

    @RequestMapping("/index")
    public ModelAndView paginaInicial() {
        //estacionamentoModel.permanenciaTodasEstadas(estadaDAO);
        //EstacionamentoCheio.tempoMedioCheioGeral(MINUTES.between(estacionamentoModel.getHoraAbertura(),estacionamentoModel.getHoraFechamento()));
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
        modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
        modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
        modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
        modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());
        modelAndView.addObject("vagasDisponiveis", estacionamentoModel.calcQtdeVagasLivres());
        return modelAndView;
    }

    @RequestMapping("/")
    public String inicializaBanco() {
        estacionamentoModel = estacionamentoDAO.instanciaEstacionamento();
        estadaDAO.instanciaEstadas(estacionamentoModel);

        try {
            criaArquivo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "index";
    }

    @RequestMapping(value = "/cadastroEstabelecimento", method = RequestMethod.GET)
    public ModelAndView informacoes(Estacionamento estacionamento) {
        ModelAndView modelAndView = new ModelAndView("cadastroEstabelecimento");
        modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
        modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
        modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
        modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
        modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());
        modelAndView.addObject("vagasDisponiveis", estacionamentoModel.calcQtdeVagasLivres());
        return modelAndView;
    }

    @RequestMapping(value = "/cadastroEstabelecimento", method = RequestMethod.POST)
    public ModelAndView salvar(@RequestParam("nomeEstabelecimento") String nomeEstabelecimento,
                               @RequestParam("horaAbertura") String horaAbertura,
                               @RequestParam("horaFechamento") String horaFechamento,
                               @RequestParam("quantidadeVagas") int quantidadeVagas,
                               @RequestParam("valorHora") Double valorHora) {

        estacionamentoDAO.delete(estacionamentoModel);
        ModelAndView mv = new ModelAndView("redirect:/infoEstabelecimentoCadastrado");

        try {
            criaArquivo();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Formatação e Data */
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime horaAberturaFormat = LocalTime.parse(horaAbertura, dtf);
        LocalTime horaFechamentoFormat = LocalTime.parse(horaFechamento, dtf);
        estacionamentoModel.setNomeEstabelecimento(nomeEstabelecimento);
        estacionamentoModel.setQuantidadeVagas(quantidadeVagas);
        estacionamentoModel.setValorHora(valorHora);
        estacionamentoModel.setHoraAbertura(horaAberturaFormat);
        estacionamentoModel.setHoraFechamento(horaFechamentoFormat);

        ModelAndView modelAndView = new ModelAndView("infoEstabelecimentoCadastrado");
        modelAndView.addObject("nomeEstabelecimento", nomeEstabelecimento);
        modelAndView.addObject("quantidadeVagas", quantidadeVagas);
        modelAndView.addObject("valorHora", valorHora);
        modelAndView.addObject("horaAbertura", horaAberturaFormat);
        modelAndView.addObject("horaFechamento", horaFechamentoFormat);
        modelAndView.addObject("vagasDisponiveis", estacionamentoModel.calcQtdeVagasLivres());

        estacionamentoDAO.add(estacionamentoModel);

        /* Printa as informações do estabelecimento instanciado no console */
        System.out.println(estacionamentoModel.toString());

        return modelAndView;
    }
    
    @GetMapping("/taxaDeRetorno")
    public ModelAndView taxaRetornoForm(Model model) {
        ModelAndView modelAndView = new ModelAndView("taxaDeRetorno");
        modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
        modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
        modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
        modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
        modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());
        modelAndView.addObject("vagasDisponiveis", estacionamentoModel.getQtdVeiculosEstacionados());

        model.addAttribute("veiculo", new Veiculo());
        modelAndView.addObject("vagasDisponiveis", estacionamentoModel.calcQtdeVagasLivres());
        return modelAndView;
    }
    
    @PostMapping("/taxaDeRetorno")
   public ModelAndView taxaDeRetorno (@RequestParam("placa") String placaVeiculo) {
        ModelAndView modelAndView = new ModelAndView("taxaDeRetornoSecundaria");
        Map<String, Estada> mapEstada = estacionamentoModel.getEstadaList();
        Estada estadaVeiculo = mapEstada.get(placaVeiculo);
        modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
        modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
        modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
        modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
        modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());
        modelAndView.addObject("vagasDisponiveis", estacionamentoModel.calcQtdeVagasLivres());
        double taxaDeRetorno = estacionamentoModel.taxaDeRetorno(placaVeiculo);
        System.out.println(taxaDeRetorno);
     
            modelAndView.addObject("placaVeiculoInfo", placaVeiculo);
            modelAndView.addObject("taxaDeRetornoInfo", taxaDeRetorno);
        
        

       return modelAndView;
   }


    @GetMapping("/entradaVeiculo")
    public ModelAndView entradaForm(Model model) {
        model.addAttribute("veiculo", new Veiculo());
        ModelAndView modelAndView = new ModelAndView("entradaVeiculo");
        modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
        modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
        modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
        modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
        modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());
        modelAndView.addObject("vagasDisponiveis", estacionamentoModel.calcQtdeVagasLivres());
        return modelAndView;

    }

    @PostMapping("/entradaVeiculo")
    public ModelAndView veiculoEstacionado(@ModelAttribute Veiculo veiculo) {
        ModelAndView modelAndView = new ModelAndView("veiculoEstacionado");
        modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
        modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
        modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
        modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
        modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());
        if (estacionamentoModel.calcQtdeVagasLivres() > 0 && !(estacionamentoModel.getEstadaList().containsKey(veiculo.getPlaca()))){
            Map<String, Object> infoEntradaVeiculo = estacionamentoModel.entradaVeiculo(veiculo);
            String placaN = (String) infoEntradaVeiculo.get("placaVeiculo");
            String tipoVeiculoN = (String) infoEntradaVeiculo.get("tipoVeiculo");
            LocalDateTime dataEntradaN = (LocalDateTime) infoEntradaVeiculo.get("dataEntrada");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            modelAndView.addObject("dataHoraAtual", LocalTime.now());
            modelAndView.addObject("placaVeiculo"   , placaN);
            modelAndView.addObject("tipoVeiculo", tipoVeiculoN);
            modelAndView.addObject("dataEntrada", dataEntradaN.format(formatter));
            modelAndView.addObject("mensagem", "Entrada de veículo efetuada com sucesso");
            modelAndView.addObject("subMensagem", "");
            modelAndView.addObject("vagasDisponiveis", estacionamentoModel.calcQtdeVagasLivres());
        } else {
            modelAndView.addObject("mensagem", "Entrada de veículo não efetuada");
            modelAndView.addObject("subMensagem", "Veículo já estacionado no estabelecimento ou não há vagas disponíveis.");
            System.out.println("Entrada de veiculo nao efetuada,VEICULO JA ESTACIONADO OU NAO HA VAGAS DISPONIVEIS");
            modelAndView.addObject("vagasDisponiveis", estacionamentoModel.calcQtdeVagasLivres());
        }

        return modelAndView;
    }

    @GetMapping("/saidaVeiculo")
    public ModelAndView saidaForm(Model model) {
        ModelAndView modelAndView = new ModelAndView("saidaVeiculo");
        modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
        modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
        modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
        modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
        modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());
        modelAndView.addObject("vagasDisponiveis", estacionamentoModel.getQtdVeiculosEstacionados());

        model.addAttribute("veiculo", new Veiculo());
        modelAndView.addObject("vagasDisponiveis", estacionamentoModel.calcQtdeVagasLivres());
        return modelAndView;
    }

    @PostMapping("/saidaVeiculo")
    public ModelAndView veiculoEstacionado(@RequestParam("placa") String placaVeiculoSaindo) {
        ModelAndView modelAndView = new ModelAndView("veiculoSaindo");
        Map<String, Estada> mapEstada = estacionamentoModel.getEstadaList();
        Estada estadaVeiculo = mapEstada.get(placaVeiculoSaindo);
        modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
        modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
        modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
        modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
        modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());

        if (mapEstada.containsKey(placaVeiculoSaindo)) {
            Map<String, Object> informacoesSaida = estadaVeiculo.saidaVeiculo(estacionamentoModel, placaVeiculoSaindo);
            modelAndView.addObject("placaVeiculo", placaVeiculoSaindo);
            modelAndView.addObject("tempoPermanecido", informacoesSaida.get("tempoPermanecido"));
            modelAndView.addObject("dataHoraEntrada", informacoesSaida.get("dataHoraEntrada"));
            modelAndView.addObject("dataHoraSaida", informacoesSaida.get("dataHoraSaida"));
            modelAndView.addObject("tipoVeiculo", informacoesSaida.get("tipoVeiculo"));
            DecimalFormat formato = new DecimalFormat("#.##");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            modelAndView.addObject("dataHoraAtual", LocalDateTime.now().format(formatter));
            modelAndView.addObject("valorAPagar", "R$" + formato.format(informacoesSaida.get("valorAPagar")));
            modelAndView.addObject("mensagem", "Saída do veículo efetuada com sucesso");
            modelAndView.addObject("subMensagem", "");
            modelAndView.addObject("vagasDisponiveis", estacionamentoModel.calcQtdeVagasLivres());

            estadaDAO.delete(estadaVeiculo);
        } else {
            modelAndView.addObject("mensagem", "Saída não efetuada");
            modelAndView.addObject("subMensagem", "O veiculo cuja a placa " + placaVeiculoSaindo + " não se encontra estacionado no momento.");
            System.out.println("Saida de veiculo nao realizada. Veiculo com placa "+placaVeiculoSaindo+" nao consta nos veiculos estacionados!");
            modelAndView.addObject("vagasDisponiveis", estacionamentoModel.calcQtdeVagasLivres());
        }

		return modelAndView;
}

    @RequestMapping(value = "/veiculosEstacionados", method = RequestMethod.GET)
    public ModelAndView veículos(Estacionamento estacionamento) {
        StringBuilder sbVeiculos = estacionamentoModel.exibeVeiculos(estadaDAO);
        StringBuilder barraDeProgressoVeiculos = estacionamentoModel.porcentagemTipoVeiculo(estadaDAO);
        ModelAndView modelAndView = new ModelAndView("veiculosEstacionados");
        modelAndView.addObject("infoVeiculos", sbVeiculos);
        modelAndView.addObject("porcentagemVeiculos", barraDeProgressoVeiculos);
        modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
        modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
        modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
        modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
        modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());
        modelAndView.addObject("vagasDisponiveis", estacionamentoModel.calcQtdeVagasLivres());
        return modelAndView;
    }

    @RequestMapping(value = "/estatisticaDoEstabelecimento", method = RequestMethod.GET)
    public ModelAndView estatistica(Estacionamento estacionamento) {
        ModelAndView modelAndView = new ModelAndView("estatisticaDoEstabelecimento");
        modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
        modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
        modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
        modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
        modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());
        modelAndView.addObject("vagasDisponiveis", estacionamentoModel.calcQtdeVagasLivres());
        DecimalFormat formato = new DecimalFormat("#.##");

        double horas = estacionamentoModel.mediaTempoPermanecido() / 60;
        double minutos = estacionamentoModel.mediaTempoPermanecido() % 60;
        String mediaHorasParaExibir = Estacionamento.formatadorHoras((int)horas,(int)minutos);

        modelAndView.addObject("mediaTempoPermanecido", mediaHorasParaExibir);
        modelAndView.addObject("quantidadeTotalVeiculos", estacionamentoModel.totalCarrosEstacionados(estadaDAO) + " veículos");
        modelAndView.addObject("mediaArrecadadoHora", "R$ " + formato.format(estacionamentoModel.mediaArrecadacaoHora()));
        modelAndView.addObject("porcentagemCarros", "Carros " + formato.format(estacionamentoModel.porcentagemCarrosGeral(estadaDAO)) + "%");
        modelAndView.addObject("porcentagemMotos", "Motos " + formato.format(estacionamentoModel.porcentagemMotosGeral(estadaDAO)) + "%");
        modelAndView.addObject("porcentagemCaminhonetes", "Caminhonetes " + formato.format(estacionamentoModel.porcentagemCaminhonetesGeral(estadaDAO)) + "%");

        estacionamentoModel.totalCarrosEstacionados(estadaDAO);
        return modelAndView;
    }

    @RequestMapping(value = "/lotacaoTabela", method = RequestMethod.GET)
    public ModelAndView lotacao(Estacionamento estacionamento) {
        ModelAndView modelAndView = new ModelAndView("lotacaoTabela");
        modelAndView.addObject("nomeEstabelecimentoInfo", estacionamentoModel.getNomeEstabelecimento());
        modelAndView.addObject("horaAberturaInfo", estacionamentoModel.getHoraAbertura());
        modelAndView.addObject("horaFechamentoInfo", estacionamentoModel.getHoraFechamento());
        modelAndView.addObject("quantidadeVagasInfo", estacionamentoModel.getQuantidadeVagas());
        modelAndView.addObject("precoInfo", estacionamentoModel.getValorHora());
        modelAndView.addObject("vagasDisponiveis", estacionamentoModel.calcQtdeVagasLivres());
        modelAndView.addObject("infoTabela", EstacionamentoCheio.tempoMedioCheioGeral(MINUTES.between(estacionamentoModel.getHoraAbertura(), estacionamentoModel.getHoraFechamento())));
        //System.out.println(EstacionamentoCheio.tempoMedioCheioGeral(MINUTES.between(estacionamentoModel.getHoraAbertura(), estacionamentoModel.getHoraFechamento())));
        return modelAndView;
    }
}
