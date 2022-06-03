package controllers;

import java.util.ArrayList;

import model.Orcamento;
import model.dao.OrcamentoDAO;

public class ProcessaOrcamento {

	public static ArrayList<Orcamento> orcamentos = new ArrayList<>();
	private static OrcamentoDAO od = new OrcamentoDAO();
	
	public static void abrir() {
		orcamentos = od.ler();
		if(orcamentos.size() == 0) {
			orcamentos.add(new Orcamento(1, "Intel - XPTO1", "Processador i7 5ºG", 1099.99f));
		}
	}
	
	public static void salvar() {
		od.escrever(orcamentos);
	}
}
