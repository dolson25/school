package servertester.controllers;

import java.util.*;

import client.communication.ClientException;
import client.communication.ClientCommunicator;
import servertester.views.*;
import shared.communication.*;

public class Controller implements IController {

	private IView _view;
	
	public Controller() {
		return;
	}
	
	public IView getView() {
		return _view;
	}
	
	public void setView(IView value) {
		_view = value;
	}
	
	@Override
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("39640");
		operationSelected();
	}

	@Override
	public void operationSelected() {
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add("User");
		paramNames.add("Password");
		
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			break;
		case GET_PROJECTS:
			break;
		case GET_SAMPLE_IMAGE:
			paramNames.add("Project");
			break;
		case DOWNLOAD_BATCH:
			paramNames.add("Project");
			break;
		case GET_FIELDS:
			paramNames.add("Project");
			break;
		case SUBMIT_BATCH:
			paramNames.add("Batch");
			paramNames.add("Record Values");
			break;
		case SEARCH:
			paramNames.add("Fields");
			paramNames.add("Search Values");
			break;
		default:
			assert false;
			break;
		}
		
		getView().setRequest("");
		getView().setResponse("");
		getView().setParameterNames(paramNames.toArray(new String[paramNames.size()]));
	}

	@Override
	public void executeOperation() {
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			validateUser();
			break;
		case GET_PROJECTS:
			getProjects();
			break;
		case GET_SAMPLE_IMAGE:
			getSampleImage();
			break;
		case DOWNLOAD_BATCH:
			downloadBatch();
			break;
		case GET_FIELDS:
			getFields();
			break;
		case SUBMIT_BATCH:
			submitBatch();
			break;
		case SEARCH:
			search();
			break;
		default:
			assert false;
			break;
		}
	}
	
	private void validateUser() {

		String[] params = getView().getParameterValues();
		ValidateUserInput input = new ValidateUserInput(params[0], params[1]);
		ClientCommunicator c = new ClientCommunicator(getView().getHost(),
															getView().getPort());
		ValidateUserOutput output = null;
		try {
			output = c.validateUser(input);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		getView().setRequest(params[0] + " " + params[1]);
		
		if(output == null)
		{
			getView().setResponse("FAILED\n");
		}
		else
		{
			getView().setResponse(output.toString());
		}
		
	}
	
	private void getProjects() {
		
		String[] params = getView().getParameterValues();
		GetProjectsInput input = new GetProjectsInput(params[0], params[1]);
		ClientCommunicator c = new ClientCommunicator(getView().getHost(),
														getView().getPort());
		GetProjectsOutput output = null;
		try {
			output = c.getProjects(input);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		getView().setRequest(params[0] + " " + params[1]);
		
		if(output == null)
		{
			getView().setResponse("FAILED\n");
		}
		else
		{
			getView().setResponse(output.toString());
		}
	}
	
	private void getSampleImage() {
		
		String[] params = getView().getParameterValues();
		
		if(params[2].equals("")){
			getView().setResponse("FAILED\n");
			return;
		}
		
		GetSampleImageInput input = new GetSampleImageInput(params[0], params[1],
														Integer.valueOf(params[2]));
		ClientCommunicator c = new ClientCommunicator(getView().getHost(),
														getView().getPort());
		GetSampleImageOutput output = null;
		try {
			output = c.getSampleImage(input);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		getView().setRequest(params[0] + " " + params[1] + " " + params[2]);
		
		if(output == null)
		{
			getView().setResponse("FAILED\n");
		}
		else
		{
			getView().setResponse(output.toString());
		}
	}
	
	private void downloadBatch() {

		String[] params = getView().getParameterValues();
		
		if(params[2].equals("")){
			getView().setResponse("FAILED\n");
			return;
		}
		
		DownloadBatchInput input = new DownloadBatchInput(params[0], params[1],
													     	Integer.valueOf(params[2]));
		ClientCommunicator c = new ClientCommunicator(getView().getHost(),
														     getView().getPort());
		DownloadBatchOutput output = null;
		try {
			output = c.downloadBatch(input);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		getView().setRequest(params[0] + " " + params[1] + " " + params[2]);
		
		if(output == null)
		{
			getView().setResponse("FAILED\n");
		}
		else
		{
			getView().setResponse(output.toString());
		}
		
		
	}
	
	private void getFields() {
		String[] params = getView().getParameterValues();
		
		GetFieldsInput input = new GetFieldsInput(params[0], params[1], params[2]);
		
		ClientCommunicator c = new ClientCommunicator(getView().getHost()
																	, getView().getPort());
		GetFieldsOutput output = null;
		
		try {
			output = c.getFields(input);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		getView().setRequest(params[0] + " " + params[1] + " " + params[2]);
		
		if(output == null)
		{
			getView().setResponse("FAILED\n");
		}
		else
		{
			getView().setResponse(output.toString());
		}
	}
	
	private void submitBatch() {
	
		String[] params = getView().getParameterValues();
		
		if(params[2].equals("")){
			getView().setResponse("FAILED\n");
			return;
		}
		
		SubmitBatchInput input = new SubmitBatchInput(params[0], params[1],
														         params[2], params[3]);
		ClientCommunicator c = new ClientCommunicator(getView().getHost(),
														        getView().getPort());
		SubmitBatchOutput output = null;
		try {
			output = c.submitBatch(input);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		getView().setRequest(params[0] + "\n" + params[1] + "\n" 
										+ params[2] + "\n" + params[3]);
		
		if(output == null)
		{
			getView().setResponse("FAILED\n");
		}
		else
		{
			getView().setResponse(output.toString());
		}
	}
	
	private void search() {
		
		String[] params = getView().getParameterValues();
		
		if(params[2].equals("")){
			getView().setResponse("FAILED\n");
			return;
		}
		
		SearchInput input = new SearchInput(params[0], params[1],
														params[2], params[3]);
		ClientCommunicator c = new ClientCommunicator(getView().getHost(),
														getView().getPort());
		SearchOutput output = null;
		try {
			output = c.search(input);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		
		getView().setRequest(params[0] + "\n" + params[1] + "\n" 
										+ params[2] + "\n" + params[3]);
		
		if(output == null)
		{
			getView().setResponse("FAILED\n");
		}
		else
		{
			getView().setResponse(output.toString());
		}
	}

}

