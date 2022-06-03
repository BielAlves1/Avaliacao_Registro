package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controllers.ProcessaOrcamento;
import model.Orcamento;

public class OrcamentoForm extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel painel;
	private JLabel id, prod, fornecedor, valor, rotulos;
	private JTextField tfId, tfProd, tfFornecedor, tfValor;
	private JScrollPane rolagem;
	private JTable table;
	private DefaultTableModel tableModel;
	private JButton create, read, update, delete;
	private int autoId = ProcessaOrcamento.orcamentos.get(ProcessaOrcamento.orcamentos.size()-1).getId() + 1;

	private final Locale BRASIL = new Locale("pt", "BR");
	private DecimalFormat df = new DecimalFormat("#.00");

	public OrcamentoForm() {
		setTitle("Cadastro de Animais/Pets");
		setBounds(150, 170, 800, 600);
		painel = new JPanel();
		painel.setBackground(new Color(255, 233, 213));
		setContentPane(painel);
		setLayout(null);

		id = new JLabel("Id:");
		id.setBounds(20, 20, 120, 30);
		painel.add(id);
		prod = new JLabel("Espécie:");
		prod.setBounds(20, 55, 120, 30);
		painel.add(prod);
		fornecedor = new JLabel("Nome pet:");
		fornecedor.setBounds(20, 90, 120, 30);
		painel.add(fornecedor);
		
		tfId = new JTextField(String.format("%d", autoId));
		tfId.setEditable(false);
		tfId.setBounds(140, 25, 140, 30);
		painel.add(tfId);
		tfProd = new JTextField();
		tfProd.setBounds(140, 60, 255, 30);
		painel.add(tfProd);
		tfFornecedor = new JTextField();
		tfFornecedor.setBounds(140, 95, 255, 30);
		painel.add(tfFornecedor);
		tfValor = new JTextField();
		tfValor.setBounds(140, 130, 255, 30);
		painel.add(tfValor);

		table = new JTable();
		tableModel = new DefaultTableModel();
		tableModel.addColumn("Id");
		tableModel.addColumn("Produto");
		tableModel.addColumn("Fornecedor");
		tableModel.addColumn("Valor");
		if (ProcessaOrcamento.orcamentos.size() != 0) {
			preencherTabela();
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
		create.setBounds(285, 25, 110, 30);
		read.setBounds(405, 25, 110, 30);
		update.setBounds(525, 25, 110, 30);
		delete.setBounds(645, 25, 110, 30);
		update.setEnabled(false);
		delete.setEnabled(false);
		painel.add(create);
		painel.add(read);
		painel.add(update);
		painel.add(delete);

		tfProd.addActionListener(this);
		create.addActionListener(this);
		read.addActionListener(this);
		update.addActionListener(this);
		delete.addActionListener(this);

	}
	
	private void limparCampos() {
		tfId.setText(String.format("%d",autoId));
		tfProd.setText(null);
		tfFornecedor.setText(null);
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
			tableModel.addRow(new String[] { or.getId("s"), or.getProduto(), or.getFornecedor(), or.getPreco("s") });
		}
	}

	// CREATE - CRUD
	private void cadastrar() {
		if (tfFornecedor.getText().length() != 0 && tfValor.getText().length() != 0 && tfPeso.getText().length() != 0
				&& tfNascimento.getText().length() != 0 && tfNomeDono.getText().length() != 0
				&& tfTelefone.getText().length() != 0) {

			// Converter o peso no formato Brasileiro usando virgula como decimal
			df.setCurrency(Currency.getInstance(BRASIL));
			float peso;
			try {
				peso = Float.parseFloat(df.parse(tfPeso.getText()).toString());
			} catch (ParseException e) {
				System.out.println(e);
				peso = 0;
			}

			ProcessaOrcamento.orcamentos.add(new Pet(autoId, tfProd.getSelectedItem().toString(), tfFornecedor.getText(),
					tfValor.getText(), peso, tfNascimento.getText(), tfNomeDono.getText(), tfTelefone.getText()));
			autoId++;
			preencherTabela();
			limparCampos();
			ProcessaOrcamento.salvar();
		} else {
			JOptionPane.showMessageDialog(this, "Favor preencher todos os campos.");
		}
	}

	// READ - CRUD
	private void buscar() {
		String entrada = JOptionPane.showInputDialog(this, "Digite o Id do animal:");

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
			Pet pet = new Pet(id);
			if (ProcessaOrcamento.orcamentos.contains(pet)) {
				int indice = ProcessaOrcamento.orcamentos.indexOf(pet);
				tfId.setText(ProcessaOrcamento.orcamentos.get(indice).getId("s"));
				tfProd.setSelectedIndex(obterIndiceEspecie(ProcessaOrcamento.orcamentos.get(indice).getEspecie()));
				tfFornecedor.setText(ProcessaOrcamento.orcamentos.get(indice).getNomePet());
				tfValor.setText(ProcessaOrcamento.orcamentos.get(indice).getRaca());
				tfPeso.setText(ProcessaOrcamento.orcamentos.get(indice).getPeso("s"));
				tfNascimento.setText(ProcessaOrcamento.orcamentos.get(indice).getNascimento("s"));
				tfNomeDono.setText(ProcessaOrcamento.orcamentos.get(indice).getNomeDono());
				tfTelefone.setText(ProcessaOrcamento.orcamentos.get(indice).getTelefone());
				create.setEnabled(false);
				update.setEnabled(true);
				delete.setEnabled(true);
				ProcessaOrcamento.salvar();
			} else {
				JOptionPane.showMessageDialog(this, "Pet n�o encontrado");
			}
		}

	}

	// UPDATE - CRUD
	private void alterar() {
		int id = Integer.parseInt(tfId.getText());
		Pet pet = new Pet(id);
		int indice = ProcessaOrcamento.orcamentos.indexOf(pet);
		if (tfFornecedor.getText().length() != 0 && tfValor.getText().length() != 0 && tfPeso.getText().length() != 0
				&& tfNascimento.getText().length() != 0 && tfNomeDono.getText().length() != 0
				&& tfTelefone.getText().length() != 0) {

			// Converter o peso no formato Brasileiro usando virgula como decimal
			df.setCurrency(Currency.getInstance(BRASIL));
			float peso;
			try {
				peso = Float.parseFloat(df.parse(tfPeso.getText()).toString());
			} catch (ParseException e) {
				System.out.println(e);
				peso = 0;
			}

			ProcessaOrcamento.orcamentos.set(indice, new Pet(id, tfProd.getSelectedItem().toString(), tfFornecedor.getText(),
					tfValor.getText(), peso, tfNascimento.getText(), tfNomeDono.getText(), tfTelefone.getText()));
			preencherTabela();
			limparCampos();
		} else {
			JOptionPane.showMessageDialog(this, "Favor preencher todos os campos.");
		}
		create.setEnabled(true);
		update.setEnabled(false);
		delete.setEnabled(false);
		tfId.setText(String.format("%d", autoId));
		ProcessaOrcamento.salvar();
	}

	// DELETE - CRUD
	private void excluir() {
		int id = Integer.parseInt(tfId.getText());
		Pet pet = new Pet(id);
		int indice = ProcessaOrcamento.orcamentos.indexOf(pet);
		ProcessaOrcamento.orcamentos.remove(indice);
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
		if (e.getSource() == tfProd) {
			alternarImagens(tfProd.getSelectedIndex());
		}
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
}
