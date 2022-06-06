package controllers;

import java.util.ArrayList;

import model.Orcamento;
import model.dao.OrcamentoDAO;

public class ProcessaOrcamento {

	public static ArrayList<Orcamento> orcamentos = new ArrayList<>();
	private static OrcamentoDAO od = new OrcamentoDAO();
	
	public static void compararProdutos(String produto) {
		int indexMaisBarato = 0;
		double maisBarato = 9999999;
		for (Orcamento orcamento : orcamentos) {
			if (orcamento.getProduto().equals(produto) && orcamento.getPreco() < maisBarato) {
			indexMaisBarato = orcamentos.indexOf(orcamento);
			maisBarato = orcamento.getPreco();
			}
		}
		
		for (Orcamento orcamento : orcamentos) {
			if (orcamentos.indexOf(orcamento) == indexMaisBarato) {
				orcamento.setMaisBarato(true);
			} else if(orcamento.getProduto() == produto){
				orcamento.setMaisBarato(false);
			}
		}
	}
	
	public static void abrir() {
		orcamentos = od.ler();
	}
	
	public static void salvar() {
		od.escrever(orcamentos);
	}
}
