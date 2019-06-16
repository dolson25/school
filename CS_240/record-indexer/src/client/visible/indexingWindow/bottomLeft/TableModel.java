package client.visible.indexingWindow.bottomLeft;

import javax.swing.table.*;
import client.notVisible.BatchState;
import client.notVisible.BatchStateListener;


@SuppressWarnings("serial")
public class TableModel extends AbstractTableModel implements BatchStateListener{
	
	private BatchState bs;
	private TablePanel tp;

	public TableModel( BatchState bss, TablePanel tpp) {
		bs = bss;
		tp = tpp;
		bs.addEmbeddedListener(this);
	}

	@Override
	public int getColumnCount() {
		return bs.getBatchInfo().getNumFields() + 1;
	}

	@Override
	public String getColumnName(int column) {
		if(column == 0)
			return "Record Number";
		return bs.getBatchInfo().getFields().get(column - 1).getTitle();
	}

	@Override
	public int getRowCount() {
		return bs.getBatchInfo().getNumRecords();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if(column == 0)
			return false;
		return true;
	}

	@Override
	public Object getValueAt(int row, int column) {

		Object result = null;

			if(column == 0)
				result = row + 1 + "";
			else
				result = bs.getValues()[row][column - 1];


		return result;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {

			bs.valueChanged(row, column - 1, (String)value);
			this.fireTableCellUpdated(row, column);
	
	}

	@Override
	public void selectedCellChanged(int row, int col) {
		
	}

	@Override
	public void valueChanged(int x, int y, String newValue) {
		
		tp.revalidate();
		
	}

	@Override
	public void highlightsToggled() {
		
	}

	@Override
	public void zoomChanged(double ratio) {
		
	}

	@Override
	public void downloadBatch() {
		
	}

	@Override
	public void submitBatch() {
		
	}

	@Override
	public void imageInvert() {
		
	}
	

	



}