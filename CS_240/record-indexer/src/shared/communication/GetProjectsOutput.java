package shared.communication;

import java.util.ArrayList;

/** a container for Get Projects Result*/
public class GetProjectsOutput {
	
	private ArrayList<ArrayList<String>> projects;
	private boolean validated;

	public GetProjectsOutput(ArrayList<ArrayList<String>> projects, boolean validated){
		
		setProjects(projects);
		setValidated(validated);
	}
	
	/**gets all the projects, stored in an ArrayList
	 * 
	 * @return the projects
	 */
	public ArrayList<ArrayList<String>> getProjects() {
		return projects;
	}

	/**sets all the projects, by passing them in an ArrayList
	 * 
	 * @param projects the projects
	 */
	public void setProjects(ArrayList<ArrayList<String>> projects) {
		this.projects = projects;
	}

	/** a check if the user was validated
	 * 
	 * @return true if validated
	 */
	public boolean isValidated() {
		return validated;
	}

	/** set the validation state
	 * 
	 * @param validated true if validated
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}
	
	/**a string representation of the output
	 * 
	 */
	@Override
	public String toString() {
		
		String s = "";
		if(this.validated)
		{
			for(int i = 0; i<projects.size();i++)
			{
				for(int j = 0; j<projects.get(i).size(); j++)
				{
					s += projects.get(i).get(j) + "\n";
				}	
			}

		}
		else
		{
			s += "FAILED\n";
		}
		
		return s;
	}

}
