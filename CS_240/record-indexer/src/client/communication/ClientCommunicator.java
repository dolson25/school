package client.communication;

import shared.communication.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import shared.model.Field;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import client.*;
import client.communication.ClientException;

public class ClientCommunicator {
	
	private static String SERVER_HOST = "localhost";
	private static String SERVER_PORT = "8080";
	private static String URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;

	private XStream xmlStream;
	
	public ClientCommunicator()
	{
		xmlStream = new XStream(new DomDriver());
	}
	
	public ClientCommunicator(String host, String port) {
		
		SERVER_HOST = host;
		SERVER_PORT = port;
		URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
		xmlStream = new XStream(new DomDriver());
	}

	/**mediates validate user between client and server
	 * 
	 * @param input ValidateUserInput object
	 * @return ValidateUserOutput object
	 * @throws ClientException 
	 */
	public ValidateUserOutput validateUser(ValidateUserInput input) 
														     throws ClientException{
		return (ValidateUserOutput)doPost("/ValidateUser", input);
	}
	
	/**mediates get projects between client and server
	 * 
	 * @param input GetProjectsInput object
	 * @return GetProjectsOutput object
	 * @throws ClientException 
	 */
	public GetProjectsOutput getProjects(GetProjectsInput input) 
															throws ClientException{
		return (GetProjectsOutput)doPost("/GetProjects", input);
	}
	
	/**mediates get sample image between client and server
	 * 
	 * @param input GetSampleImageInput object
	 * @return GetSampleImageOutput object
	 * @throws ClientException 
	 */
	public GetSampleImageOutput getSampleImage(GetSampleImageInput input) 
															throws ClientException{
		
		GetSampleImageOutput output = (GetSampleImageOutput)doPost(
											"/GetSampleImage", input);
		if(output != null)
			output.setFilepath(URL_PREFIX + "/Records/" + output.getFilepath());
		
		return output;
	}
	
	/**mediates download batch between client and server
	 * 
	 * @param input DownloadBatchInput object
	 * @return DownloadBatchOutput object
	 * @throws ClientException 
	 */
	public DownloadBatchOutput downloadBatch(DownloadBatchInput input) 
															throws ClientException{
		
		DownloadBatchOutput output = (DownloadBatchOutput)doPost(
											"/DownloadBatch", input);
		if(output != null)
		{
			output.setImageURL(URL_PREFIX + "/Records/" + output.getImageURL());
			
			ArrayList<Field> newFields = new ArrayList<Field>();
			
			for(int i = 0; i < output.getFields().size(); i++)
			{
				Field f = output.getFields().get(i);
				f.setFieldHelpPath(URL_PREFIX + "/Records/" + f.getFieldHelpPath());
				
				if(!f.getKnownDataPath().equals(""))
					 f.setKnownDataPath(URL_PREFIX + "/Records/" + f.getKnownDataPath());
				
				newFields.add(f);
			}
			
			output.setFields(newFields);
		}

		return output;
	}
	
	/**mediates submit batch between client and server
	 * 
	 * @param input SubmitBatchUserInput object
	 * @return SubmitBatchOutput object
	 * @throws ClientException 
	 */
	public SubmitBatchOutput submitBatch(SubmitBatchInput input) throws ClientException{
		
		return (SubmitBatchOutput)doPost("/SubmitBatch", input);
	}
	
	/**mediates get fields between client and server
	 * 
	 * @param input GetFieldsInput object
	 * @return GetFieldsOutput object
	 * @throws ClientException 
	 */
	public GetFieldsOutput getFields(GetFieldsInput input) throws ClientException
	{
		return (GetFieldsOutput)doPost("/GetFields", input);
	}
	
	/**mediates search between client and server
	 * 
	 * @param input SearchInput object
	 * @return SearchOutput object
	 * @throws ClientException 
	 */
	public SearchOutput search(SearchInput input) throws ClientException
	{
		SearchOutput output = (SearchOutput)doPost("/Search", input);
		
		if(output != null)
		{
			ArrayList<SearchOutputArray> newOutput = new ArrayList<SearchOutputArray>();
			
			for(int i = 0; i < output.getResults().size(); i++)
			{
				SearchOutputArray a = output.getResults().get(i);

				a.setImage_url(URL_PREFIX + "/Records/" + a.getImage_url());
				
				newOutput.add(a);
			}
			
			output.setResults(newOutput);

			return output;
		}
		else
			return null;
	}
	
	/**gets the appropriate bytes off the server for the client
	 * 
	 * @param url an image or text file location
	 * @return the image or text
	 * @throws ClientException 
	 */
	public byte[] fileDownload(String url) throws ClientException
	{
		return  (byte[])doPost("/",url);
	}

	private Object doPost(String urlPath, Object postData) throws ClientException {
		try {
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.connect();
			xmlStream.toXML(postData, connection.getOutputStream());
			connection.getOutputStream().close();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				Object result = xmlStream.fromXML(connection.getInputStream());
				return result;
			}
			else 
				return null;
		}
		catch (IOException e) {
			return null;
		}
	}

}
