package client.notVisible;

public interface BatchStateListener {
	
	public void selectedCellChanged(int row, int col);
	public void valueChanged(int x, int y, String newValue);
	public void highlightsToggled();
	public void zoomChanged(double ratio);
	public void downloadBatch();
	public void submitBatch();
	public void imageInvert();
}
