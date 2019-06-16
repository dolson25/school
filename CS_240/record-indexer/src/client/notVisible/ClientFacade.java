package client.notVisible;

import client.communication.ClientCommunicator;
import client.communication.ClientException;
import shared.communication.*;

public class ClientFacade {
	
	private static String host;
	private static String portNumber;

	public static void setHost(String host) {
		ClientFacade.host = host;
	}

	public static void setPortNumber(String portNumber) {
		ClientFacade.portNumber = portNumber;
	}

	public static ValidateUserOutput validateUser(String username, String password){
		
		ValidateUserInput input = new ValidateUserInput(username, password);
		
		ClientCommunicator c = new ClientCommunicator(host,portNumber);
		ValidateUserOutput output = null;
		try {
			output = c.validateUser(input);
		} catch (ClientException e) {
			e.printStackTrace();
		}

		return output;
	}
	
	public static GetProjectsOutput getProjects(String username, String password){
		
		GetProjectsInput input = new GetProjectsInput(username, password);
		
		ClientCommunicator c = new ClientCommunicator(host,portNumber);
		GetProjectsOutput output = null;
		try {
			output = c.getProjects(input);
		} catch (ClientException e) {
			e.printStackTrace();
		}

		return output;
	}
	
	public static GetSampleImageOutput getSampleImage(String username, String password, int id){
		
		GetSampleImageInput input = new GetSampleImageInput(username, password, id);
		
		ClientCommunicator c = new ClientCommunicator(host,portNumber);
		GetSampleImageOutput output = null;
		try {
			output = c.getSampleImage(input);
		} catch (ClientException e) {
			e.printStackTrace();
		}

		return output;
	}
	
	public static byte[] downloadFile(String url){
		
		ClientCommunicator c = new ClientCommunicator(host,portNumber);
		byte[] output = null;
		try {
			output = c.fileDownload(url);
		} catch (ClientException e) {
			e.printStackTrace();
		}

		return output;
	}
	
	public static DownloadBatchOutput downloadBatch(String username, String password, int id){
		
		DownloadBatchInput input = new DownloadBatchInput(username, password, id);

		ClientCommunicator c = new ClientCommunicator(host,portNumber);
		DownloadBatchOutput output = null;
		try {
			output = c.downloadBatch(input);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		
		return output;
	}
	
	public static SubmitBatchOutput submitBatch(String username, String password, 
														String batch_id, String values){
		
		SubmitBatchInput input = new SubmitBatchInput(username, password, batch_id, values);

		ClientCommunicator c = new ClientCommunicator(host,portNumber);
		SubmitBatchOutput output = null;
		try {
			output = c.submitBatch(input);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return output;
	}

}
