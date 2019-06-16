package server;

import java.io.*;
import java.rmi.ServerException;
import shared.model.*; 
import org.apache.commons.io.*;
import server.database.DatabaseException;
import server.database.Database;


import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

public class DataImporter {
	
	static Database db;

	public static void main(String[] args) throws ServerException
	{
		db = new Database();
		
			try {
				Database.initialize();		
			}
			catch (DatabaseException e) {
				throw new ServerException(e.getMessage(), e);
			}
		
		DocumentBuilder builder = null;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		File xmlFile = new File(args[0]);
		File dest = new File("Records"); 
		
		File emptydb = new File("database" + File.separator + "recordindexer1.sqlite");
		File currentdb = new File("database" + File.separator + "recordindexer.sqlite");

		try {
			FileUtils.copyFile(emptydb, currentdb);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try
		{
			if(!xmlFile.getParentFile().getCanonicalPath().equals(
															dest.getCanonicalPath()))
				FileUtils.deleteDirectory(dest);
				
			FileUtils.copyDirectory(xmlFile.getParentFile(), dest);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
		File parsefile = new File(dest.getPath() + File.separator + xmlFile.getName());
		Document doc;
		try {
			db.startTransaction();
			doc = builder.parse(parsefile);
			NodeList users = doc.getElementsByTagName("user");
			parseUsers(users);

			NodeList projects = doc.getElementsByTagName("project");
			parseProjects(projects);
			
			db.endTransaction(true);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
	}


	private static void parseUsers(NodeList usersList) 
							                throws ServerException, DatabaseException{		
		
		for (int i = 0; i < usersList.getLength(); i++)
		{               		
			Element userElement  = (Element)usersList.item(i);
			User newUser = new User(0, 
						getString("username", userElement), 
						getString("password", userElement),
						getString("firstname", userElement), 
						getString("lastname", userElement),
						getString("email", userElement),
						Integer.valueOf(getString("indexedrecords",userElement)),0,0);

				db.getUserDAO().add(newUser);
		}
	}
	
	private static void parseProjects(NodeList projectsList) throws DatabaseException
	{	
		for (int i = 0; i < projectsList.getLength(); i++)
		{                		
			Element projectElement  = (Element)projectsList.item(i);
			Project newProject = new Project(0, 
						getString("title", projectElement), 
						Integer.valueOf(getString("recordsperimage", projectElement)),
						Integer.valueOf(getString("firstycoord", projectElement)), 
						Integer.valueOf(getString("recordheight", projectElement)));


				
			int project_id = db.getProjectDAO().add(newProject);
				
				
			Element fieldsElement = (Element)projectElement.getElementsByTagName(
																 "fields").item(0);
			NodeList fieldElements = fieldsElement.getElementsByTagName("field");
				
			int field_id = parseFields(fieldElements, project_id);
				
				
			Element imagesElement = (Element)projectElement.getElementsByTagName(
																 "images").item(0);
			NodeList imageElements = imagesElement.getElementsByTagName("image");
			parseImages(imageElements, project_id, field_id);		
		}
	}
	
	private static int parseFields(NodeList fieldsList, int project_id) 
														throws DatabaseException{
		boolean passedThroughOnce = false;
		int field_id = 0;

		for (int i = 0; i < fieldsList.getLength(); i++)
		{
			Element fieldsElement  = (Element)fieldsList.item(i);
			
			Element fieldElement = (Element)fieldsElement.getElementsByTagName(
																"knowndata").item(0);
			String knownData = "";
			if(fieldElement != null)
				knownData = getString("knowndata", fieldsElement);
			
			Field newField = new Field(0, 
					getString("title", fieldsElement), 
					Integer.valueOf(getString("xcoord", fieldsElement)),
					Integer.valueOf(getString("width", fieldsElement)), 
					getString("helphtml", fieldsElement),knownData, project_id, i + 1);

				if(!passedThroughOnce)
				{
					field_id = db.getFieldDAO().add(newField);
					passedThroughOnce = true;
				}
				else
					db.getFieldDAO().add(newField);
		}
		
		return field_id;
	}
	
	private static void parseImages(NodeList imagesList, int project_id, 
										          int field_id) throws DatabaseException{	
		int available = 1;
		int image_id = 0;
		
		for (int i = 0; i < imagesList.getLength(); i++)
		{
			Element imageElement  = (Element)imagesList.item(i);
			Element recordsElement = (Element)imageElement.getElementsByTagName
																("records").item(0);
			
			if(recordsElement != null)
			{
				available = 0;
			}

			Image newImage = new Image(0, getString("file", imageElement), project_id, 
																				available);
			image_id = db.getImageDAO().add(newImage);				
				
			if(recordsElement != null)
			{
			   NodeList recordElements = recordsElement.getElementsByTagName("record");
			   parseRecords(recordElements, project_id, image_id, field_id);
			}			
		}
	}
	
	private static void parseRecords(NodeList recordsList, int project_id,
							         int image_id, int field_id) throws DatabaseException{		
		
		for (int i = 0; i < recordsList.getLength(); i++)
		{
			Element recordElement  = (Element)recordsList.item(i);

			Record newRecord = new Record(0,i+1,image_id); 			
			
			int record_id = db.getRecordDAO().add(newRecord);
				
			Element valuesElement = (Element)recordElement.
											      getElementsByTagName("values").item(0);
			
			NodeList valueElements = valuesElement.getElementsByTagName("value");
			
			parseValues(valueElements, project_id, record_id, field_id);
		}
	}


	private static void parseValues(NodeList valuesList, int project_id,
							int record_id, int field_id) throws DatabaseException{
		
		for (int i = 0; i < valuesList.getLength(); i++)
		{
			Element valueElement  = (Element)valuesList.item(i);			
		
			Cell newCell = new Cell(0,record_id, field_id + i, valueElement.getTextContent());
			
			db.getCellDAO().add(newCell);
		}
	}
	
	private static String getString(String tagName, Element someElement)
	{
		Element e = (Element) someElement.getElementsByTagName(tagName).item(0);
		return e.getTextContent();
	}
}
