//package com.easypark;
//
//import static org.junit.Assert.assertNull;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import com.easypark.models.Estacionamento;
//import com.easypark.models.EstacionamentoDAO;
//import com.easypark.models.Estada;
//import com.easypark.models.EstadaDAO;
//import com.easypark.models.Veiculo;
//
//class VerificaDAOTest {
//	EstadaDAO estadaDAO;
//	EstacionamentoDAO estacionamentoDAO;
//	LocalDateTime dataEntradaTeste;
//	LocalTime horaEntradaTeste;
//	List<Estada> estadaList;
//	LocalTime horaAbertura;
//	LocalTime horaFechamento;
//	Veiculo veiculoTeste;
//	Veiculo veiculoUpdate;
//	Estada estadaTest;
//	Estacionamento estacionamentoTeste;
//
//	@BeforeEach
//	public void setUp() {
//		estadaDAO = new EstadaDAO();
//		estacionamentoDAO = new EstacionamentoDAO();
//		dataEntradaTeste = LocalDateTime.now();
//		horaEntradaTeste = LocalTime.now();
//		estadaList = new ArrayList<>();
//		veiculoTeste = new Veiculo("XXX000", "Moto");
//		horaAbertura = LocalTime.now();
//		horaFechamento = LocalTime.now();
//		veiculoUpdate = new Veiculo("XXX000", "Caminhao");
//		estadaTest = new Estada(dataEntradaTeste, LocalDateTime.MIN, horaEntradaTeste, LocalTime.MIN, veiculoTeste,
//				LocalTime.MIN, 0.0, 0);
//		estadaDAO.add(estadaTest);
//	}
//
//	@Test
//	public void testGetEstada() {
//		assertEquals("Moto", estadaDAO.get("XXX000").getVeiculo().getTipoVeiculo(),
//				"Assert true caso o tipo do veiculo utilizado no get corresponda ao informado no teste.");
//	}
//
//	@Test
//	public void testAddEstada() {
//		assertEquals("XXX000", estadaDAO.get("XXX000").getVeiculo().getPlaca(),
//				"Assert true caso o veiculo tenha sido adicionado com sucesso");
//	}
//
//	@Test
//	public void testUpdateEstada() {
//		estadaDAO.get("XXX000").setVeiculo(veiculoUpdate);
//		estadaDAO.update(estadaTest);
//		assertEquals("Caminhao", estadaDAO.get("XXX000").getVeiculo().getTipoVeiculo(), "");
//	}
//	
//	@Test
//	public void testDeleteEstada() {
//		estadaDAO.get("XXX000").setVeiculo(veiculoUpdate);
//		estadaDAO.delete(estadaTest);
//		assertNull(estadaDAO.get("XXX000"), "AssertNull(TRUE) caso o veiculo tenha sido deletado com sucesso.");
//	}
//
//	@Test
//	public void testAddEstacionamento() {
//		estacionamentoTeste = new Estacionamento("nomeEstacionamento", horaAbertura, horaFechamento, 30, 12.0);
//		estacionamentoDAO.add(estacionamentoTeste);
//		assertEquals("nomeEstabelecimento", estacionamentoDAO.get("nomeEstabelecimento").getNomeEstabelecimento());
//	}
//
//	@Test
//	public void testGetEstacionamento() {
//		estacionamentoTeste = new Estacionamento("nomeEstacionamento", horaAbertura, horaFechamento, 30, 12.0);
//		estacionamentoDAO.add(estacionamentoTeste);
//		assertEquals(30, estacionamentoDAO.get("nomeEstacionamento").getQuantidadeVagas(),
//		"Assert True caso a quantidade de vagas esteja correta");
//	}
//
//}
