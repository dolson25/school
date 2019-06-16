package client.visible.indexingWindow.bottomLeft;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import client.notVisible.BatchState;
import client.notVisible.BatchStateListener;


@SuppressWarnings("serial")
public class TablePanel extends JPanel implements BatchStateListener,TableModelListener{

	private TableModel tableModel;
	private JTable table;
	private BatchState bs;

	public TablePanel(BatchState bss){
		
		bs = bss;
		this.setVisible(false);
		bs.addEmbeddedListener(this);
		tableModel = new TableModel(bs, this);
	
		table = new JTable(tableModel);
		table.setRowHeight(20);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAlignmentX(LEFT_ALIGNMENT);
		table.setAlignmentY(TOP_ALIGNMENT);
		table.addMouseListener(mouseAdapter);
		table.addFocusListener(focusListener);

		table.validate();

		TableColumnModel columnModel = table.getColumnModel();		
		columnModel.setColumnSelectionAllowed(true);

		for (int i = 0; i < tableModel.getColumnCount(); ++i) {
			TableColumn column = columnModel.getColumn(i);
			column.setPreferredWidth(100);
		}		
		for (int i = 1; i < tableModel.getColumnCount(); ++i) {
			TableColumn column = columnModel.getColumn(i);
			column.setCellRenderer(new ColorCellRenderer(bs));
		}
		

		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.add(table.getTableHeader(), BorderLayout.NORTH);
		rootPanel.add(table, BorderLayout.CENTER);
		JScrollPane jsp = new JScrollPane(rootPanel);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(jsp);
	}
	
	public ActionListener a = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {

				SuggestionDialog sd = new SuggestionDialog(bs);
				sd.setVisible(true);

		}
		
	};
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e){
			
			final int row = table.rowAtPoint(e.getPoint());
			final int column = table.columnAtPoint(e.getPoint());
			
			if(SwingUtilities.isRightMouseButton(e))
			{
				if(!bs.getSpellBools()[row][column - 1])
				{
					JPopupMenu menu = new JPopupMenu();
					JMenuItem seeSuggestions = new JMenuItem("See Suggestions");
					seeSuggestions.addActionListener(a);
					menu.add(seeSuggestions);
					table.setComponentPopupMenu(menu);
					menu.show(e.getComponent(), e.getX(), e.getY());
				}
				else
				{
					table.repaint();
				}	
			}
		}	
	};
	
	FocusListener focusListener = new FocusListener(){

		@Override
		public void focusGained(FocusEvent e) {
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			table.clearSelection();	
		}
		
	};

	
	public void selectedCellChanged(int row, int col)
	{
		repaint();
	}
	public void valueChanged(int x, int y, String newValue)
	{
		
	}
	public void highlightsToggled()
	{
		
	}
	public void zoomChanged(double ratio)
	{
		
	}
	public void downloadBatch()
	{

	}
	public void submitBatch()
	{
		
	}
	public void imageInvert()
	{
		
	}

	@Override
	public void tableChanged(TableModelEvent e) {

	}
	
	@SuppressWarnings("serial")
	class ColorCellRenderer extends JLabel implements TableCellRenderer {

		private Border unselectedBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
		private Border selectedBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
		private BatchState bs;
		
		public ColorCellRenderer(BatchState bss) {
		bs = bss;
		setOpaque(true);
		setFont(getFont().deriveFont(12.0f));
	}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			if ((row == bs.getCellSelectedRow()) && (column == bs.getCellSelectedColumn() + 1))
			{
				setBackground(new Color(110, 222, 200, 166));
				setBorder(selectedBorder);
			}
			else if(isSelected)
			{
				if(table.hasFocus())
				{
				     bs.selectedCellChanged(row, column - 1);
				}
			}
			else
			{
				setBackground(Color.WHITE);
				setBorder(unselectedBorder);
			}
			
			if(!bs.getSpellBools()[row][column - 1])
				setBackground(Color.RED);
			
			this.setText((String)tableModel.getValueAt(row, column));
			
			return this;
		}
  };
}

