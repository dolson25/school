package server.database.DAO;

import java.io.File;
import java.io.IOException;
import static org.junit.Assert.*;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import server.database.DatabaseException;
import server.database.DAO.UserDAO;
import shared.model.*;
import server.database.Database;

public class UserDAOTest {

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
	private UserDAO dbUser;

	@Before
	public void setUp() throws Exception {
	
		db = new Database();
		db.startTransaction();
		dbUser = db.getUserDAO();
	}

	@After
	public void tearDown() throws Exception {
		
		db.endTransaction(false);
		
		db = null;
		dbUser = null;
	}
	
	@Test
	public void testVAlidateUser() throws DatabaseException {
		
		User user1 = new User(0,"username1","password1", "blah", "blah", "blah", 0, 0, 0);
		User user2 = new User(0,"username2","password2", "blah", "blah", "blah", 0, 0, 0);
	
		dbUser.add(user1);
		dbUser.add(user2);
		
		assertTrue(dbUser.validateUser("username1", "password1"));
		assertTrue(dbUser.validateUser("username2", "password2"));
		
		assertFalse(dbUser.validateUser("username1", "password"));
		assertFalse(dbUser.validateUser("Username1", "password1"));
	}
	
	@Test
	public void testGetUser() throws DatabaseException{
		
		User user1 = new User(0,"name","password1", "tim", "white", "blah", 6, 0, 0);
		User user2 = new User(0,"name2","password2", "jim", "black", "blah", 15, 0, 0);
		
		dbUser.add(user1);
		dbUser.add(user2);
		
		User temp = dbUser.getUser("name");
		User temp2 = dbUser.getUser("name2");
		
		assertEquals(temp.getId(), 1);
		assertEquals(temp.getUsername(), "name");
		assertEquals(temp.getPassword(), "password1");
		assertEquals(temp.getFirstname(), "tim");
		assertEquals(temp.getLastname(), "white");
		assertEquals(temp.getNumberRecordsIndexed(), 6);
		assertEquals(temp.getBatchCheckedOut(), 0);
		
		assertEquals(temp2.getBatchCheckedOut(), 0);
	}

	@Test
	public void testAdd() throws DatabaseException {
		
		User user1 = new User(0,"name","password1", "tim", "white", "blah", 6, 1, 0);
		User user2 = new User(0,"name2","password2", "jim", "black", "blah", 15, 0, 0);
		
		dbUser.add(user1);
		dbUser.add(user2);
		
		assertNotNull(dbUser.getUser("name"));
		assertNotNull(dbUser.getUser("name2"));
	}
	
	@Test
	public void testUpdateNumRecordsIndexed() throws DatabaseException {
		
		User user1 = new User(0,"name","password1", "tim", "white", "blah", 6, 1, 0);
		User user2 = new User(0,"name2","password2", "jim", "black", "blah", 15, 0, 0);
		
		dbUser.add(user1);
		dbUser.add(user2);
		
		dbUser.updateNumRecordsIndexed("name", 2);
		dbUser.updateNumRecordsIndexed("name2", 21);
		
		assertEquals(dbUser.getUser("name").getNumberRecordsIndexed(), 2);
		assertEquals(dbUser.getUser("name2").getNumberRecordsIndexed(), 21);
	}
	
	
	@Test
	public void testUpdateBatchCheckedOut() throws DatabaseException{
		User user1 = new User(0, "name1", "p", "j","b","b", 1, 0, 0);
		User user2 = new User(0, "name2", "p", "j","b","b", 1, 1, 0);
		
		dbUser.add(user1);
		dbUser.add(user2);
		
		dbUser.updateBatchCheckedOut("name1", 1);
		dbUser.updateBatchCheckedOut("name2", 0);
		
		User temp1 = dbUser.getUser("name1");
		User temp2 = dbUser.getUser("name2");
		
		assertEquals(temp1.getBatchCheckedOut(), 1);
		assertEquals(temp2.getBatchCheckedOut(), 0);
	}
	
	@Test
	public void testUpdateBatchNumberAssigned() throws DatabaseException {
		
		User user1 = new User(0,"name","password1", "tim", "white", "blah", 6, 1, 0);
		
		dbUser.add(user1);
		
		dbUser.updateBatchNumberAssigned("name", 2);
		
		assertEquals(dbUser.getUser("name").getBatchNumberAssigned(), 2);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidValidateUser() throws DatabaseException{
		
		User user1 = new User(0,"username1","password1", "blah", "blah", "blah", 0, 0);
	
		dbUser.add(user1);

		dbUser.validateUser(null, "password1");
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidGetUser() throws DatabaseException {
		
		User user1 = new User(0,"username1","password1", "blah", "blah", "blah", 0, 0);
		
		dbUser.add(user1);

		dbUser.getUser(null);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidUpdateBatchCheckedOut() throws DatabaseException {
		
		dbUser.updateBatchCheckedOut(null, 1);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidGetUser2() throws DatabaseException {
		
		User user1 = new User(0,"username1","password1", "blah", "blah", "blah", 0, 0);
		
		dbUser.add(user1);

		dbUser.getUser("mysteryMan");
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException {
		
		User invalidUser = new User(-1, null, null, null, null, null, 0, 0);
		dbUser.add(invalidUser);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidUpdateNumRecordsIndexed() throws DatabaseException {
	
		dbUser.updateNumRecordsIndexed(null, 1);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidUpdateBatchNumberAssigned() throws DatabaseException {
	
		dbUser.updateBatchNumberAssigned(null, 1);
	}
}
