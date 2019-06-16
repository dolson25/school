package server;

import java.util.*;

import server.database.*;
import shared.communication.*;
import shared.model.*;

public class ServerFacade {

	public static void initialize() throws ServerException {		
		try {
			Database.initialize();		
		}
		catch (DatabaseException e) {
			throw new ServerException(e.getMessage(), e);
		}		
	}
	
	public static ValidateUserOutput validateUser(ValidateUserInput input) 
														throws ServerException {	
		Database db = new Database();
		ValidateUserOutput output;
		
		try {
			db.startTransaction();
			
			if(db.getUserDAO().validateUser(input.getUsername(),input.getPassword()))
			{
				User user = db.getUserDAO().getUser(input.getUsername());
				
				output = new ValidateUserOutput(user.getFirstname(), 
						          user.getLastname(),user.getNumberRecordsIndexed(),true);
			}
			else
			{
				output = new ValidateUserOutput("","",1,false);
			}
			
			db.endTransaction(true);

			return output;
		}
		catch (DatabaseException e) {
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	public static GetProjectsOutput getProjects(GetProjectsInput input) 
														    throws ServerException {

		Database db = new Database();
		GetProjectsOutput output;
		
		try {
			db.startTransaction();
			
			if(db.getUserDAO().validateUser(input.getUsername(),input.getPassword()))
			{
				ArrayList<ArrayList<String>> projects = db.getProjectDAO().getProjects();
				output = new GetProjectsOutput(projects, true);
			}
			else
			{
				output = new GetProjectsOutput(null,false);
				db.endTransaction(false);
			}
			
			db.endTransaction(true);
			return output;
		}
		catch (DatabaseException e) {
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	public static GetSampleImageOutput getSampleImage(GetSampleImageInput input) 
														   throws ServerException {

		Database db = new Database();
		GetSampleImageOutput output = null;
		
		try {
			db.startTransaction();
			
			if(db.getUserDAO().validateUser(input.getUsername(),input.getPassword()))
			{
				Image image = db.getImageDAO().getSampleImage(input.getProject_id());
				if(image == null)
					output = null;
				else
					output = new GetSampleImageOutput(image.getFilepath(), true);
			}
			else
			{
				output = new GetSampleImageOutput(null,false);
				db.endTransaction(false);
			}
			
			db.endTransaction(true);
			return output;
		}
		catch (DatabaseException e) {
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	
	public static DownloadBatchOutput downloadBatch(DownloadBatchInput input) 
															throws ServerException {
		Database db = new Database();
		DownloadBatchOutput output;
		
		try {
			db.startTransaction();
			
			if(db.getUserDAO().validateUser(input.getUsername(),input.getPassword())
					& (db.getUserDAO().getUser(input.getUsername()).getBatchCheckedOut()
					== 0)){
				
				Image image = db.getImageDAO().getAvailableImage(
														input.getProject_id());
				
				if(image != null)
				{
					Project project = db.getProjectDAO().getProject(input.getProject_id());
					ArrayList<Field> fields = db.getFieldDAO().getFieldInfo(input.getProject_id());
					
					output = new DownloadBatchOutput(image.getId(),project.getId(),
								fields, image.getFilepath(),project.getFirstYCoordinate(),
								project.getRecordHeight(), project.getRecordsPerImage(),
								fields.size(), true);
					
					db.getUserDAO().updateBatchCheckedOut(input.getUsername(), 1);
					db.getImageDAO().updateAvailable(0, image.getId());
					db.getUserDAO().updateBatchNumberAssigned(input.getUsername(),
																	image.getId());
				}
				else
				{
					output = null;
					db.endTransaction(false);
				}
			}
			else
			{
				output = null;
				db.endTransaction(false);
			}
			
			db.endTransaction(true);
			return output;
		}
		catch (DatabaseException e) {
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	public static SubmitBatchOutput submitBatch(SubmitBatchInput input) 
															throws ServerException {
		Database db = new Database();
		SubmitBatchOutput output;
		String tempUsername = input.getUsername();
		
		int batch_id = Integer.valueOf(input.getBatch_id());

		try {
			db.startTransaction();
			
			User user = db.getUserDAO().getUser(tempUsername);
			
			if((db.getUserDAO().validateUser(tempUsername,input.getPassword()))
					& (user.getBatchNumberAssigned() == batch_id)){
				
				db.getUserDAO().updateBatchCheckedOut(tempUsername, 0);
				db.getUserDAO().updateBatchNumberAssigned(tempUsername, 0);
				int project_id = db.getImageDAO().getProjectID(
									Integer.valueOf(input.getBatch_id()));
				
				//update number of records indexed (# of total records)
				db.getUserDAO().updateNumRecordsIndexed(tempUsername, 
						user.getNumberRecordsIndexed() + db.getProjectDAO()
						.getProject(project_id).getRecordsPerImage());
			
				
				
				
				String[] records = input.getCellValues().split(";");
				
				//check number of records 
				if(records.length != db.getProjectDAO().getProject(
													project_id).getRecordsPerImage()){
					db.endTransaction(false);
					return null;
				}
				
				for(int i = 0; i < records.length; i++)
				{	
					Record record = new Record(0,i+1,batch_id);
					int record_id = db.getRecordDAO().add(record);
					
					String[] values = records[i].split(",");

					if(values.length != db.getFieldDAO().getNumFields(project_id))
					{
						db.endTransaction(false);
						return null;
					}
					
					ArrayList<Field> fields = db.getFieldDAO().getFieldInfo(project_id);
					int field_id = fields.get(1).getId() -1;
					
					for(int j = 0; j < values.length; j++)
					{
						Cell cell = new Cell(0, record_id, field_id + j, values[j]);
						
						db.getCellDAO().add(cell);
					}
				}
				
				output = new SubmitBatchOutput(true);
			}
			else
			{
					output = null;
					db.endTransaction(false);
			}
			
			db.endTransaction(true);
			return output;
		}
		catch (DatabaseException e) {
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	public static GetFieldsOutput getFields(GetFieldsInput input) 
															throws ServerException {
		Database db = new Database();
		GetFieldsOutput output;
		ArrayList<ArrayList<Field>> finalFields = new ArrayList<ArrayList<Field>>();
		String project_id = "";

		try {
			db.startTransaction();
			
			if(db.getUserDAO().validateUser(input.getUsername(),input.getPassword()))
			{
				if(input.getProject_id().equals("")) 
				{
					ArrayList<Field> fields = db.getFieldDAO().getFieldInfo(0);
					finalFields.add(fields);
				}
				else
				{
					ArrayList<Field> fields = db.getFieldDAO().getFieldInfo(
						Integer.valueOf(input.getProject_id()));
					finalFields.add(fields);
					project_id = input.getProject_id();
					
					if(fields.size() == 0)
						return null;
				}
			
				output = new GetFieldsOutput(finalFields,project_id, true);
			}
			else
			{
				db.endTransaction(false);
				output = null;
			}
				
			db.endTransaction(true);
			return output;
		}
		catch (DatabaseException e) {
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	public static SearchOutput search(SearchInput input) throws ServerException {

		Database db = new Database();
		SearchOutput output;
		String tempUsername = input.getUsername();
		ArrayList<SearchOutputArray> result = new ArrayList<SearchOutputArray>();

		try {
			db.startTransaction();
			
			if(db.getUserDAO().validateUser(tempUsername,input.getPassword())){
							
				if(input.getSearchFields().equals("") || 
									input.getSearchValues().equals(""))
					return null;
				
				String[] fields = input.getSearchFields().split(",");
				String[] values = input.getSearchValues().split(",");
				
				for(int i = 0; i < fields.length; i++)
				{	
					if(db.getFieldDAO().isField(Integer.valueOf(fields[i])))
					{
						for(int j = 0; j < values.length; j++)
						{
							ArrayList<SearchOutputArray> searchResult = db.getCellDAO().search(
								   Integer.valueOf(fields[i]), values[j]);

							for(int k = 0; k<searchResult.size();k++)
							{
								result.add(searchResult.get(k));
							}
								
						}
					}
					else
						return null;
				}
				
				output = new SearchOutput(result);
			}
			else
			{
					output = null;
					db.endTransaction(false);
			}
			
			db.endTransaction(true);
			return output;
		}
		catch (DatabaseException e) {
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}

}
