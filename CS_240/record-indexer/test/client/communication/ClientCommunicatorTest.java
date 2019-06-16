package client.communication;

import static org.junit.Assert.*;
import shared.communication.*;

import org.apache.commons.io.FileUtils;
import org.junit.*;

import client.communication.ClientException;
import client.communication.ClientCommunicator;

import java.io.File;
import java.io.IOException;
import java.util.*;

import server.database.DatabaseException;
import shared.model.Project;
import shared.model.User;
import server.database.Database;
import server.database.DAO.UserDAO;

public class ClientCommunicatorTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		Database.initialize();
		
		File emptydb = new File("database" + File.separator + "recordindexer1.sqlite");
		File currentdb = new File("database" + File.separator + "recordindexer.sqlite");

		try {
			FileUtils.copyFile(emptydb, currentdb);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		return;
	}
		
	private Database db;

	@Before
	public void setUp() throws Exception {

		db = new Database();
		db.startTransaction();
	}

	@After
	public void tearDown() throws Exception {
		
		db.endTransaction(true);
		
		db = null;
	}
	
	@Test
	public void testValidateUser() throws DatabaseException, ClientException{
		
		ClientCommunicator c = new ClientCommunicator();
		UserDAO dbUser;
		dbUser = db.getUserDAO();

		User user2 = new User(0,"username2","password2", "blah", "blah", "blah", 0, 0, 0);

		dbUser.add(user2);

		ValidateUserInput input2 = new ValidateUserInput("username1", "password2");

		ValidateUserOutput output2 = c.validateUser(input2);

		assertEquals(output2.isValidated(), false);
		
		dbUser = null;
	}
}
