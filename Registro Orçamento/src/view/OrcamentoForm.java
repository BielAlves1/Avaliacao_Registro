package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controllers.ProcessaOrcamento;
import model.Orcamento;

public class OrcamentoForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel painel;
	private JLabel id, fornecedor, produto, valor;
	private JTextField tfId, tfFornecedor, tfProduto, tfValor;
	private JScrollPane rolagem;
	private JTable table;
	private DefaultTableModel tableModel;
	private JButton create, read, update, delete;
	private int autoId = ProcessaOrcamento.orcamentos.get(ProcessaOrcamento.orcamentos.size()-1).getId() + 1;

	private final Locale BRASIL = new Locale("pt", "BR");
	private DecimalFormat df = new DecimalFormat("#.00");

	public OrcamentoForm() {
		setTitle("Registro de Orçamentos");
		setBounds(150, 170, 800, 600);
		painel = new JPanel();
		painel.setBackground(new Color(174, 238, 238));
		setContentPane(painel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);

		id = new JLabel("ID:");
		id.setBounds(115, 20, 120, 30);
		painel.add(id);
		fornecedor = new JLabel("Fornecedor:");
		fornecedor.setBounds(60, 70, 120, 30);
		painel.add(fornecedor);
		produto = new JLabel("Produto:");
		produto.setBounds(80, 120, 120, 30);
		painel.add(produto);
		valor = new JLabel("Valor:");
		valor.setBounds(95, 170, 120, 30);
		painel.add(valor);
		
		tfId = new JTextField(String.format("%d", autoId));
		tfId.setEditable(false);
		tfId.setBounds(140, 25, 80, 35);
		painel.add(tfId);
		tfFornecedor = new JTextField();
		tfFornecedor.setBounds(140, 75, 315, 35);
		painel.add(tfFornecedor);
		tfProduto = new JTextField();
		tfProduto.setBounds(140, 125, 315, 35);
		painel.add(tfProduto);
		tfValor = new JTextField();
		tfValor.setBounds(140, 175, 315, 35);
		painel.add(tfValor);

		table = new JTable();
		tableModel = new DefaultTableModel();
		tableModel.addColumn("ID");
		tableModel.addColumn("Fornecedor");
		tableModel.addColumn("Produto");
		tableModel.addColumn("Valor");
		tableModel.addColumn("Mais Barato");
		if (ProcessaOrcamento.orcamentos.size() != 0) {
			preencherTabela();
			comparar();
		}
		table = new JTable(tableModel);
		table.setEnabled(false);
		rolagem = new JScrollPane(table);
		rolagem.setBounds(20, 310, 740, 230);
		painel.add(rolagem);

		create = new JButton("Cadastrar");
		read = new JButton("Buscar");
		update = new JButton("Atualizar");
		delete = new JButton("Excluir");
		create.setBounds(525, 50, 150, 35);
		read.setBounds(525, 100, 150, 35);
		update.setBounds(525, 150, 150, 35);
		delete.setBounds(525, 200, 150, 35);
		update.setEnabled(false);
		delete.setEnabled(false);
		painel.add(create);
		painel.add(read);
		painel.add(update);
		painel.add(delete);

		tfFornecedor.addActionListener(this);
		create.addActionListener(this);
		read.addActionListener(this);
		update.addActionListener(this);
		delete.addActionListener(this);

	}
	
	public void comparar() {
		for (Orcamento orcamento : ProcessaOrcamento.orcamentos) {
			ProcessaOrcamento.compararProdutos(orcamento.getProduto());
			}
	}
	
	private void limparCampos() {
		tfId.setText(String.format("%d",autoId));
		tfFornecedor.setText(null);
		tfProduto.setText(null);
		tfValor.setText(null);
	}

	private void preencherTabela() {
		int totLinhas = tableModel.getRowCount();
		if (tableModel.getRowCount() > 0) {
			for (int i = 0; i < totLinhas; i++) {
				tableModel.removeRow(0);
			}
		}
		for (Orcamento or : ProcessaOrcamento.orcamentos) {
			tableModel.addRow(new String[] { or.getId("s"), or.getFornecedor(), or.getProduto(), or.getPreco("s"), or.isMaisBarato("s")});
		}
	}

	private void cadastrar() {
		if (tfFornecedor.getText().length() != 0 && tfProduto.getText().length() != 0 && tfValor.getText().length() != 0) {
			df.setCurrency(Currency.getInstance(BRASIL));
			double valor;
			try {
				valor = Double.parseDouble(df.parse(tfValor.getText()).toString());
			} catch (ParseException e) {
				System.out.println(e);
				valor = 0;
			}
			ProcessaOrcamento.orcamentos.add(new Orcamento(autoId, tfFornecedor.getText(), tfProduto.getText(), valor, false));
			autoId++;
			limparCampos();
			comparar();
			preencherTabela();
			ProcessaOrcamento.salvar();
		} else {
			JOptionPane.showMessageDialog(this, "Por Favor preencher todos os campos.");
		}
	}

	private void buscar() {
	//Tava tudo funcionando bonitinho até adicionar a parte do boolean na table e parou de funcionar e não consegui resolver o erro...
		String entrada = JOptionPane.showInputDialog(this, "Digite o Id do Orçamento:");
		boolean isNumeric = true;
		if (entrada != null && !entrada.equals("")) {
			for (int i = 0; i < entrada.length(); i++) {
				if (!Character.isDigit(entrada.charAt(i))) {
					isNumeric = false;
				}
			}
		} else {
			isNumeric = false;
		}
		if (isNumeric) {
			int id = Integer.parseInt(entrada);
			Orcamento orcamento = new Orcamento(id);
			if (ProcessaOrcamento.orcamentos.contains(orcamento)) {
				int indice = ProcessaOrcamento.orcamentos.indexOf(orcamento);
				tfId.setText(ProcessaOrcamento.orcamentos.get(indice).getId("s"));
				tfFornecedor.setText(ProcessaOrcamento.orcamentos.get(indice).getFornecedor());
				tfProduto.setText(ProcessaOrcamento.orcamentos.get(indice).getProduto());
				tfValor.setText(ProcessaOrcamento.orcamentos.get(indice).getPreco("s"));
				create.setEnabled(false);
				update.setEnabled(true);
				delete.setEnabled(true);
				ProcessaOrcamento.salvar();
			} else {
				JOptionPane.showMessageDialog(this, "Orçamento não encontrado!");
			}
		}

	}

	private void alterar() {
		int id = Integer.parseInt(tfId.getText());
		Orcamento orcamento = new Orcamento(id);
		int indice = ProcessaOrcamento.orcamentos.indexOf(orcamento);
		if (tfFornecedor.getText().length() != 0 && tfProduto.getText().length() != 0 && tfValor.getText().length() != 0) {

			df.setCurrency(Currency.getInstance(BRASIL));
			double preco;
			try {
				preco = Double.parseDouble(df.parse(tfValor.getText()).toString());
			} catch (ParseException e) {
				System.out.println(e);
				preco = 0;
			}

			ProcessaOrcamento.orcamentos.set(indice, new Orcamento(id, tfFornecedor.getText(), tfProduto.getText(), preco, false));
			comparar();
			preencherTabela();
			limparCampos();
		} else {
			JOptionPane.showMessageDialog(this, "Por Favor preencher todos os campos.");
		}
		create.setEnabled(true);
		update.setEnabled(false);
		delete.setEnabled(false);
		tfId.setText(String.format("%d", autoId));
		ProcessaOrcamento.salvar();
	}

	private void excluir() {
		int id = Integer.parseInt(tfId.getText());
		Orcamento orcamento = new Orcamento(id);
		int indice = ProcessaOrcamento.orcamentos.indexOf(orcamento);
		ProcessaOrcamento.orcamentos.remove(indice);
		comparar();
		preencherTabela();
		limparCampos();
		create.setEnabled(true);
		update.setEnabled(false);
		delete.setEnabled(false);
		tfId.setText(String.format("%d", autoId));
		ProcessaOrcamento.salvar();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == create) {
			cadastrar();
		}
		if (e.getSource() == read) {
			buscar();
		}
		if (e.getSource() == update) {
			alterar();
		}
		if (e.getSource() == delete) {
			excluir();
		}
	}
	
	public static void main(String[] args) {
		ProcessaOrcamento.abrir();
		new OrcamentoForm().setVisible(true);
	}
}
