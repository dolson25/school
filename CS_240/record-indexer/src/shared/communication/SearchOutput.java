package shared.communication;

import java.util.ArrayList;

/** a container for Search Result*/
public class SearchOutput {
	
	private ArrayList<SearchOutputArray> results;
	
	public SearchOutput(ArrayList<SearchOutputArray> results) {
		
		setResults(results);
	}

	/**gets the list of 4-tuple objects with search output
	 * 
	 * @return the search results
	 */
	public ArrayList<SearchOutputArray> getResults() {
		return results;
	}
	
	/**sets the list of 4-tuple objects with search output
	 * 
	 * @param results the search results
	 */
	public void setResults(ArrayList<SearchOutputArray> results) {
		this.results = results;
	}
	/**a string representation of the output
	 */
	@Override
	public String toString() {
		
		String s = "";
		
		for(int i = 0; i < results.size(); i++)
		{
			SearchOutputArray result = results.get(i);
			s += result.getBatch_id() + "\n" + result.getImage_url() + "\n"
			   + result.getRecord_number() + "\n" + result.getField_id() + "\n";  
		}
		return s;
	}

}
