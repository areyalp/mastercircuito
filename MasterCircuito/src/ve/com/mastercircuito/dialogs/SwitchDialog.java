package ve.com.mastercircuito.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import ve.com.mastercircuito.components.MyTableModel;
import ve.com.mastercircuito.db.Db;

public class SwitchDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -563008319631466774L;
	
	private Integer height = 300;
	private Integer width = 800;
	
	private Db db;
	private Object[][] switchesSearchData;
	private JTable tableSwitchesSearchResult;
	private ListSelectionModel listSwitchSearchSelectionModel;
	private String searchSelectedSwitchBrand, searchSelectedSwitchModel, searchSelectedSwitchPhases, searchSelectedSwitchCurrent, searchSelectedSwitchInterruption;
	private JComboBox<String> comboSwitchBrands, comboSwitchModels, comboSwitchPhases, comboSwitchCurrents, comboSwitchInterruptions;
	
	private String[] switchesColumnNames = { "Id", "Referencia", "Marca", "Modelo", "Fases", "Amperaje", "Interrupcion", "Voltaje", "Precio"};
	
	private Integer switchSearchId = 0;
	private Integer switchAddQuantity = 1;
	private Double switchAddPrice = 0.00;
	
	public SwitchDialog(Window owner) {
		this(owner, "");
	}
	
	public SwitchDialog(Window owner, String title) {
		super(owner, title, JDialog.DEFAULT_MODALITY_TYPE);
		this.setMinimumSize(new Dimension(this.width, this.height));
		this.setSize(new Dimension(this.width, this.height));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width / 2) - (this.getWidth() / 2);
		int y = (dim.height / 2) - (this.getHeight() / 2);
		this.setLocation(x, y);
		
		db = new Db();
		
		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.PAGE_AXIS));
		panelCenter.add(createSwitchAddSearchPanel());
		panelCenter.add(Box.createRigidArea(new Dimension(0, 10)));
		JScrollPane scrollPane = new JScrollPane(createSwitchAddTablePanel());
		tableSwitchesSearchResult.setFillsViewportHeight(true);
		panelCenter.add(scrollPane);
		this.add(panelCenter, BorderLayout.CENTER);
		
		JPanel panelLower = new JPanel();
		panelLower.setLayout(new BoxLayout(panelLower, BoxLayout.LINE_AXIS));
		panelLower.add(createSwitchAddCountPanel());
		panelLower.add(createSwitchAddButtonPanel());
		this.add(panelLower, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	public Integer getSwitchSearchId() {
		return this.switchSearchId;
	}
	
	public Integer getSwitchAddQuantity() {
		return this.switchAddQuantity;
	}
	
	public Double getSwitchAddPrice() {
		return this.switchAddPrice;
	}
	
	private JPanel createSwitchAddSearchPanel() {
		JPanel panelSwitchAddSearch = new JPanel();
		
		panelSwitchAddSearch.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		String queryBrands = "SELECT brand "
				+ "FROM switch_brands, switches "
				+ "WHERE switch_brands.id = switches.brand_id "
				+ "GROUP BY switch_brands.brand";
		
		String queryModels = "SELECT model "
				+ "FROM switch_models, switches "
				+ "WHERE switch_models.id = switches.model_id "
				+ "GROUP BY switch_models.model";
		
		String queryPhases = "SELECT phases "
				+ "FROM switches "
				+ "GROUP BY phases";
		
		String queryCurrents = "SELECT current "
				+ "FROM currents, switches "
				+ "WHERE currents.id = switches.current_id "
				+ "GROUP BY currents.current";
		
		String queryInterruptions = "SELECT interruption "
				+ "FROM interruptions, switches "
				+ "WHERE interruptions.id = switches.interruption_id "
				+ "GROUP BY interruptions.interruption";
		
		JLabel brandsLabel = new JLabel("Marca:");
		JLabel modelsLabel = new JLabel("Modelo:");
		JLabel phasesLabel = new JLabel("Fases:");
		JLabel currentsLabel = new JLabel("Amperaje:");
		JLabel interruptionsLabel = new JLabel("Interrupcion:");
		
		ComboBoxListener lForCombo = new ComboBoxListener();
		
		comboSwitchBrands = new JComboBox<String>(new Vector<String>(loadComboList(queryBrands, "brand")));
		comboSwitchBrands.setActionCommand("switch.bar.brand");
		comboSwitchBrands.addActionListener(lForCombo);
		panelSwitchAddSearch.add(brandsLabel);
		panelSwitchAddSearch.add(comboSwitchBrands);
		
		comboSwitchModels = new JComboBox<String>(new Vector<String>(loadComboList(queryModels, "model")));
		comboSwitchModels.setActionCommand("switch.bar.model");
		comboSwitchModels.addActionListener(lForCombo);
		panelSwitchAddSearch.add(modelsLabel);
		panelSwitchAddSearch.add(comboSwitchModels);
		
		comboSwitchPhases = new JComboBox<String>(new Vector<String>(loadComboList(queryPhases, "phases")));
		comboSwitchPhases.setActionCommand("switch.bar.phases");
		comboSwitchPhases.addActionListener(lForCombo);
		panelSwitchAddSearch.add(phasesLabel);
		panelSwitchAddSearch.add(comboSwitchPhases);
		
		comboSwitchCurrents = new JComboBox<String>(new Vector<String>(loadComboList(queryCurrents, "current")));
		comboSwitchCurrents.setActionCommand("switch.bar.current");
		comboSwitchCurrents.addActionListener(lForCombo);
		panelSwitchAddSearch.add(currentsLabel);
		panelSwitchAddSearch.add(comboSwitchCurrents);
		
		comboSwitchInterruptions = new JComboBox<String>(new Vector<String>(loadComboList(queryInterruptions, "interruption")));
		comboSwitchInterruptions.setActionCommand("switch.bar.interruption");
		comboSwitchInterruptions.addActionListener(lForCombo);
		panelSwitchAddSearch.add(interruptionsLabel);
		panelSwitchAddSearch.add(comboSwitchInterruptions);
		
		return panelSwitchAddSearch;
	}
	
	private JPanel createSwitchAddTablePanel() {
		loadSwitchSearchTable("");
		
		MyTableModel mForTable = new MyTableModel(switchesSearchData, switchesColumnNames);
		
		tableSwitchesSearchResult = new JTable();
		tableSwitchesSearchResult.setModel(mForTable);
		tableSwitchesSearchResult.setAutoCreateRowSorter(true);
		tableSwitchesSearchResult.getTableHeader().setReorderingAllowed(false);
		
		MyListSelectionListener lForList = new MyListSelectionListener();
		
		listSwitchSearchSelectionModel = tableSwitchesSearchResult.getSelectionModel();
		listSwitchSearchSelectionModel.addListSelectionListener(lForList);
		tableSwitchesSearchResult.setSelectionModel(listSwitchSearchSelectionModel);
		tableSwitchesSearchResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel panelSwitchAddTable = new JPanel(new BorderLayout());
		panelSwitchAddTable.add(tableSwitchesSearchResult.getTableHeader(), BorderLayout.PAGE_START);
		panelSwitchAddTable.add(tableSwitchesSearchResult, BorderLayout.CENTER);
		
		return panelSwitchAddTable;
	}
	
	private JPanel createSwitchAddCountPanel() {
		JPanel panelCountOuter = new JPanel(new BorderLayout());
		JPanel panelCountInner = new JPanel();
		
		panelCountInner.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton buttonDecrease = new JButton("-");
		JButton buttonIncrease = new JButton("+");
		JLabel labelQuantity = new JLabel("1");
		switchAddQuantity = 1;
		switchAddPrice = 0.00;
		Integer min = 1;
		Integer max = 100;
		
		panelCountInner.add(buttonDecrease);
		panelCountInner.add(labelQuantity);
		panelCountInner.add(buttonIncrease);
		
		buttonDecrease.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(switchAddQuantity > min) {
					switchAddQuantity--;
					labelQuantity.setText(String.valueOf(switchAddQuantity));
				}
			}
		});
		
		buttonIncrease.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(switchAddQuantity < max) {
					switchAddQuantity++;
					labelQuantity.setText(String.valueOf(switchAddQuantity));
				}
			}
		});
		
		panelCountOuter.add(panelCountInner, BorderLayout.CENTER);
		
		return panelCountOuter;
	}
	
	private JPanel createSwitchAddButtonPanel() {
		JPanel panelOuter = new JPanel(new BorderLayout());
		JPanel panelInner = new JPanel();
		panelInner.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton buttonAccept = new JButton("Aceptar");
		JButton buttonCancel = new JButton("Cancelar");
		panelInner.add(buttonAccept);
		panelInner.add(buttonCancel);
		
		buttonAccept.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(tableSwitchesSearchResult.getSelectedRow() > -1) {
					switchAddPrice = Double.valueOf((String) tableSwitchesSearchResult.getValueAt(tableSwitchesSearchResult.getSelectedRow(), 8));
					dispose();
				}
			}
		});
		
		buttonCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				switchSearchId = 0;
				switchAddQuantity = 0;
				switchAddPrice = 0.00;
				dispose();
			}
		});
		
		panelOuter.add(panelInner, BorderLayout.CENTER);
		
		return panelOuter;
	}
	
	private void loadSwitchSearchTable(String whereQuery) {
		String switchesQuery = "SELECT switches.id, "
				+ "switches.reference, "
				+ "switch_brands.brand, "
				+ "switch_models.model, "
				+ "switches.phases, "
				+ "currents.current, "
				+ "interruptions.interruption, "
				+ "switch_voltages.voltage, "
				+ "switches.price "
			+ "FROM switches, "
				+ "switch_brands, "
				+ "switch_models, "
				+ "currents, "
				+ "interruptions, "
				+ "switch_voltages "
			+ "WHERE switches.brand_id = switch_brands.id "
			+ "AND switches.model_id = switch_models.id "
			+ "AND switches.current_id = currents.id "
			+ "AND switches.interruption_id = interruptions.id "
			+ "AND switches.voltage_id = switch_voltages.id "
			+ "AND switches.active = '1' "
			+ whereQuery;

		switchesSearchData = db.fetchAll(db.select(switchesQuery));
		
		
	}
	
	private List<String> loadComboList(String queryString, String columnName) {
		List<String> comboList = new ArrayList<String>();
		comboList.add("Todas");
		comboList.addAll(db.fetchColumnAsList(db.select(queryString), columnName));
		return comboList;
	}
	
	private class MyListSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent ev) {
			ListSelectionModel lsm = (ListSelectionModel) ev.getSource();
			
			if(null != tableSwitchesSearchResult 
					&& tableSwitchesSearchResult.isFocusOwner() 
					&& lsm.getMinSelectionIndex() > -1) {
				switchSearchId = Integer.valueOf((String)tableSwitchesSearchResult.getValueAt(tableSwitchesSearchResult.getSelectedRow(), 0));
			}
		}
		
	}
	
	private class ComboBoxListener implements ActionListener {
		
		private String fromQuery = "";
		private String whereQuery = "";
		
		@Override
		public void actionPerformed(ActionEvent ev) {
			String actionCommand = ev.getActionCommand();
			if(actionCommand.equalsIgnoreCase("switch.bar.brand")) {
				fromQuery = "switches, switch_models ";
				searchSelectedSwitchBrand = comboSwitchBrands.getSelectedItem().toString();
				this.clearSelectedSwitchOptions("model");
				this.selectWhereQuery();
				if(!searchSelectedSwitchBrand.equalsIgnoreCase("Todas")) {
					fromQuery += ", switch_brands ";
					whereQuery += " switches.brand_id = switch_brands.id "
									+ "AND switch_models.brand_id = switch_brands.id "
									+ "AND switch_models.id = switches.model_id "
									+ "AND switch_brands.brand = '"+searchSelectedSwitchBrand+"' ";
				} else {
					whereQuery += " switch_models.id = switches.model_id ";
				}
				this.addSwitchCommonQuery();
				this.loadComboSwitch("models");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("switch.bar.model")) {
				fromQuery = "switches ";
				searchSelectedSwitchModel = comboSwitchModels.getSelectedItem().toString();
				this.clearSelectedSwitchOptions("phases");
				if(!searchSelectedSwitchModel.equalsIgnoreCase("Todas")) {
					fromQuery += ", switch_models ";
					this.selectWhereQuery();
					whereQuery += " switches.model_id = switch_models.id "
									+ "AND switch_models.model = '"+searchSelectedSwitchModel+"' ";
				} else {
					whereQuery = "";
				}
				this.switchBrandQuery();
				this.addSwitchCommonQuery();
				this.loadComboSwitch("phases");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("switch.bar.phases")) {
				fromQuery = "switches ";
				searchSelectedSwitchPhases = comboSwitchPhases.getSelectedItem().toString();
				this.clearSelectedSwitchOptions("current");
				this.switchPhasesQuery();
				this.switchBrandQuery();
				this.addSwitchCommonQuery();
				this.loadComboSwitch("currents");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("switch.bar.current")) {
				fromQuery = "switches ";
				searchSelectedSwitchCurrent = comboSwitchCurrents.getSelectedItem().toString();
				this.clearSelectedSwitchOptions("interruption");
				if(!searchSelectedSwitchCurrent.equalsIgnoreCase("Todas")) {
					this.selectWhereQuery();
					whereQuery += " switches.current_id = currents.id "
									+ "AND currents.current = '" + searchSelectedSwitchCurrent + "' ";
				}
				this.switchPhasesQuery();
				this.switchBrandQuery();
				this.addSwitchCommonQuery();
				this.loadComboSwitch("interruptions");
				this.dynamicSearch();
			} else if (actionCommand.equalsIgnoreCase("switch.bar.interruption")) {
				searchSelectedSwitchInterruption = comboSwitchInterruptions.getSelectedItem().toString();
				this.dynamicSearch();
			}
		}
		
		private void dynamicSearch() {
			String whereQuery = "";
			if(searchSelectedSwitchBrand != null && !searchSelectedSwitchBrand.equalsIgnoreCase("Todas") &&
					!searchSelectedSwitchBrand.isEmpty()) {
				whereQuery += " AND switch_brands.brand = '" + searchSelectedSwitchBrand + "'";
			}
			if(searchSelectedSwitchModel != null && !searchSelectedSwitchModel.equalsIgnoreCase("Todas") &&
					!searchSelectedSwitchModel.isEmpty()) {
				whereQuery += " AND switch_models.model = '" + searchSelectedSwitchModel + "'";
			}
			if(searchSelectedSwitchPhases != null && !searchSelectedSwitchPhases.equalsIgnoreCase("Todas") &&
					!searchSelectedSwitchPhases.isEmpty()) {
				whereQuery += " AND switches.phases = '" + searchSelectedSwitchPhases + "'";
			}
			if(searchSelectedSwitchCurrent != null && !searchSelectedSwitchCurrent.equalsIgnoreCase("Todas") &&
					!searchSelectedSwitchCurrent.isEmpty()) {
				whereQuery += " AND currents.current = '" + searchSelectedSwitchCurrent + "'";
			}
			if(searchSelectedSwitchInterruption != null && !searchSelectedSwitchInterruption.equalsIgnoreCase("Todas") &&
					!searchSelectedSwitchInterruption.isEmpty()) {
				whereQuery += " AND interruptions.interruption = '" + searchSelectedSwitchInterruption + "'";
			}
			
			loadSwitchSearchTable(whereQuery);
			
			if(switchesSearchData.length > 0) {
				tableSwitchesSearchResult.setModel(new MyTableModel(switchesSearchData, switchesColumnNames));
			} else {
				tableSwitchesSearchResult.setModel(new DefaultTableModel());
			}
		}

		private void clearSelectedSwitchOptions(String start) {
			switch(start) {
				case "model":
					searchSelectedSwitchModel = "";
				case "phases":
					searchSelectedSwitchPhases = "";
				case "current":
					searchSelectedSwitchCurrent = "";
				case "interruption":
					searchSelectedSwitchInterruption = "";
					break;
			}
		}
		
		private void addSwitchCommonQuery() {
			fromQuery += ", currents, interruptions ";
			this.selectWhereQuery();
			whereQuery += " switches.current_id = currents.id ";
			this.selectWhereQuery();
			whereQuery += " interruptions.id = switches.interruption_id ";
		}
		
		private void selectWhereQuery() {
			if(whereQuery.isEmpty()) {
				whereQuery = " WHERE ";
			} else {
				whereQuery += " AND ";
			}
		}
		
		private void loadComboSwitch(String start) {
			switch(start) {
				case "models":
					comboSwitchModels.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList("switch_models", "model"))));
				case "phases":
					comboSwitchPhases.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList("switches", "phases"))));
				case "currents":
					comboSwitchCurrents.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList("currents", "current"))));
				case "interruptions":
					comboSwitchInterruptions.setModel(new DefaultComboBoxModel<String>(new Vector<String>(loadList("interruptions", "interruption"))));
					break;
			}
		}
		
		private void switchBrandQuery() {
			if(null != searchSelectedSwitchBrand && !searchSelectedSwitchBrand.equalsIgnoreCase("Todas") && 
					!searchSelectedSwitchBrand.isEmpty()) {
				this.selectWhereQuery();
				fromQuery += ", switch_brands ";
				whereQuery += " switches.brand_id = switch_brands.id "
								+ "AND switch_brands.brand = '" + searchSelectedSwitchBrand + "' ";
				if(!searchSelectedSwitchModel.equalsIgnoreCase("Todas")) {
					if(!fromQuery.contains("switch_models")) {
						fromQuery += ", switch_models ";
					}
					whereQuery += "AND switch_models.brand_id = switch_brands.id "
								+ "AND switch_models.id = switches.model_id ";
				}
			}
		}
		
		private void switchPhasesQuery() {
			if(null != searchSelectedSwitchPhases && !searchSelectedSwitchPhases.equalsIgnoreCase("Todas") && 
					!searchSelectedSwitchPhases.isEmpty()) {
				this.selectWhereQuery();
				whereQuery += " switches.phases = '" + searchSelectedSwitchPhases + "' ";
			} else {
				fromQuery = "switches ";
				whereQuery = "";
			}
		}
		
		private List<String> loadList(String table, String column) {
			List<String> list = new ArrayList<String>();
			list.add("Todas");
			list.addAll(db.fetchColumnAsList(db.select("SELECT " + table + "." + column +" "
														+ " FROM "
														+ fromQuery
														+ whereQuery
														+ " GROUP BY " + table + "." + column), column));
			if(list.size() == 1) {
				list.remove(0);
			}
			return list;
		}
		
	}
	
}
