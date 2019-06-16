package client.notVisible;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import shared.communication.DownloadBatchOutput;
import shared.model.Field;
import shared.model.User;
import shared.model.Project;

public class BatchState implements BatchStateListener{
	
	private String userName;
	private String password;
	private Point framePosition;
	private Dimension frameSize;
	private double zoomLevel;
	private boolean highlights;
	private boolean imageInverted;
	private boolean batchAssigned;
	private ArrayList<ArrayList<String>> projects;
	private DownloadBatchOutput batchInfo;
	private int cellSelectedRow;
	private int cellSelectedColumn;
	private transient ArrayList<BatchStateListener> listeners;
	private String[][] values;
	private boolean[][] spellBools;
	ArrayList<ArrayList<TreeSet<String>>> words = 
			new ArrayList<ArrayList<TreeSet<String>>>();
	private transient ArrayList<BatchStateListener> embeddedListeners;


	
	public BatchState(){
		
		listeners = new ArrayList<BatchStateListener>();
		embeddedListeners = new ArrayList<BatchStateListener>();
        words = new ArrayList<ArrayList<TreeSet<String>>>();
		batchAssigned = false;
		highlights = true;
		zoomLevel = .5;
		imageInverted = false;
	}
	
	
	
	
	public ArrayList<ArrayList<TreeSet<String>>> getWords() {
		return words;
	}
	public void setWords(ArrayList<ArrayList<TreeSet<String>>> words) {
		this.words = words;
	}
	public Dimension getFrameSize() {
		return frameSize;
	}
	public void setFrameSize(Dimension frameSize) {
		this.frameSize = frameSize;
	}
	public Point getFramePosition() {
		return framePosition;
	}
	public void setFramePosition(Point framePosition) {
		this.framePosition = framePosition;
	}
	public double getZoomLevel() {
		return zoomLevel;
	}
	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
	}
	public boolean isHighlights() {
		return highlights;
	}
	public void setHighlights(boolean highlights) {
		this.highlights = highlights;
	}
	public boolean isImageInverted() {
		return imageInverted;
	}
	public void setImageInverted(boolean imageInverted) {
		this.imageInverted = imageInverted;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isBatchAssigned() {
		return batchAssigned;
	}
	public void setBatchAssigned(boolean batchAssigned) {
		this.batchAssigned = batchAssigned;
	}
	public void setSpellBools(boolean[][] spellBools) {
		this.spellBools = spellBools;
	}
	public boolean[][] getSpellBools() {
		return spellBools;
	}
	public String[][] getValues() {
		return values;
	}
	public void setValues(String[][] values) {
		this.values = values;
	}
	public int getCellSelectedRow() {
		return cellSelectedRow;
	}
	public void setCellSelectedRow(int cellSelectedRow) {
		this.cellSelectedRow = cellSelectedRow;
	}
	public int getCellSelectedColumn() {
		return cellSelectedColumn;
	}
	public void setCellSelectedColumn(int cellSeclecedColumn) {
		this.cellSelectedColumn = cellSeclecedColumn;
	}
	public DownloadBatchOutput getBatchInfo() {
		return batchInfo;
	}
	public void setBatchInfo(DownloadBatchOutput batchInfo) {
		this.batchInfo = batchInfo;
	}
	public ArrayList<ArrayList<String>> getProjects() {
		return projects;
	}
	public void setProjects(ArrayList<ArrayList<String>> projects) {
		this.projects = projects;
	}
	
	
	
	
	
	
	
	//-------------------
	//Listener Stuff
	//-------------
	
	public void addEmbeddedListener(BatchStateListener l)
	{
		embeddedListeners.add(l);
	}
	
	public void addListener(BatchStateListener l) {
		listeners.add(l);
	}
	
	public void selectedCellChanged(int row, int col)
	{
		setCellSelectedRow(row);
		setCellSelectedColumn(col);
		
		for(BatchStateListener l : listeners){
			l.selectedCellChanged(row, col);;
		}

	}
	public void valueChanged(int x, int y, String newValue)
	{
		values[x][y] = newValue;
		if(newValue.length() == 0)
		{
			spellBools[x][y] = true;
			return;
		}
		
		boolean validChar = true;
		for(int i = 0; i< newValue.length(); i++)
		{
			
			char c = newValue.charAt(i);
			
			if((c < 32) || (c > 122)  ||
			   ((c > 32) && (c < 45)) ||
			   ((c > 45) && (c < 65)) ||
			   ((c > 90) && (c < 97))){ 
				
				validChar = false;
				
			}
		}

		if(!validChar)
		{
			spellBools[x][y] = false;
				
		}
		else
		{

		try {
			URL url = new URL(batchInfo.getFields().get(y).getKnownDataPath());
			
			
			SpellCorrector sc = new SpellCorrector();
			
			sc.useDictionary(batchInfo.getFields().get(y).getKnownDataPath());
			Set<String> spellCorrectorResult = sc.suggestSimilarWord(newValue, new Trie());
			
			if(sc.suggestSimilarWord(newValue, new Trie()) == null)
			{
				spellBools[x][y] = true;
			}
			else
			{
				spellBools[x][y] = false;
				if(spellCorrectorResult.size() != 0)
				{
					for(String s : spellCorrectorResult)
					{
						words.get(x).get(y).add(s);
					}
					
				}
				else
				{
					//no suggested words available
				}
			}
			
		} catch (MalformedURLException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	  }
		
		for(BatchStateListener l : listeners){
			l.valueChanged(x, y, newValue);
		}
	}
	public void highlightsToggled()
	{
		for(BatchStateListener l : listeners){
			l.highlightsToggled();
		}
	}
	public void zoomChanged(double ratio)
	{
		for(BatchStateListener l : listeners){
			l.zoomChanged(ratio);
		}
	}
	public void downloadBatch()
	{
		for(BatchStateListener l : listeners){
			l.downloadBatch();
		}
		
		for(BatchStateListener l : embeddedListeners){
			listeners.add(l);
		}
	}
	public void submitBatch()
	{
		String valuesString = "";
		
		for(int i = 0; i < batchInfo.getNumRecords(); i++){
			
			for(int j = 0; j < batchInfo.getNumFields(); j++){
				
				if(!values[i][j].equals(""))
					valuesString += values[i][j]; 
				else
					valuesString += " ";
				
				if(j < batchInfo.getNumFields() - 1)
				{
					valuesString += ",";
				}
			}
			
			if(i < batchInfo.getNumRecords() - 1)
				valuesString += ";";
		}
		
		ClientFacade.submitBatch(userName, password,batchInfo.getBatch_id() + "", valuesString);
		
		values = null;
	}
	public void imageInvert()
	{
		for(BatchStateListener l : listeners){
			l.imageInvert();
		}
	}




}
