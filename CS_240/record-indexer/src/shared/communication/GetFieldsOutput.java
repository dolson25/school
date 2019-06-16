package shared.communication;

import java.util.ArrayList;

import shared.model.Field;

/** a container for Get Fields Result*/
public class GetFieldsOutput {
	
	private ArrayList<ArrayList<Field>> fields;
	private String project_id;
	private boolean validated;
	
	public GetFieldsOutput(ArrayList<ArrayList<Field>> fields, String project_id,
																boolean validated) {
	
		setFields(fields);
		setValidated(validated);
		setProject_id(project_id);
	}

	/**gets the array representing projects, holding arrays of fields
	 * 
	 * @return fields
	 */
	public ArrayList<ArrayList<Field>> getFields() {
		return fields;
	}
	
	/**sets the array representing projects, holding arrays of fields
	 * 
	 * @oparam fields fields
	 */
	public void setFields(ArrayList<ArrayList<Field>> fields) {
		this.fields = fields;
	}
	
	/**gets the project id
	 * 
	 * @return the project id
	 */
	public String getProject_id() {
		return project_id;
	}

	/**sets the project id
	 * 
	 * @oparam project_id the project id
	 */
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	/**a check if the fields were retrieved
	 * 
	 * @return true if successful
	 */
	public boolean isValidated() {
		return validated;
	}

	/**set the fields retrieved status
	 * 
	 * @param validated true if successful
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}
	
	/**a string representation of the output
	 */
	@Override
	public String toString() {
		
		String s = "";
		if(project_id.equals(""))
		{
			ArrayList<Field> tempFields = fields.get(0);
			System.out.println("here");
			for(int i = 0; i<tempFields.size(); i++)
			{
				s += tempFields.get(i).getProject_id()+ "\n" + 
					tempFields.get(i).getId() + "\n" +
					tempFields.get(i).getTitle() + "\n";
			}
		}
		else
		{
			ArrayList<Field> tempFields = fields.get(0);
			
			for(int i = 0; i<tempFields.size(); i++)
			{
				s += project_id + "\n" + tempFields.get(i).getId() + "\n" +
											tempFields.get(i).getTitle() + "\n";
			}
		}
		
		return s;
	}

}
