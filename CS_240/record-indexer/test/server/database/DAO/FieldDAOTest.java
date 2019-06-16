package server.database.DAO;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import server.database.DatabaseException;
import shared.model.Field;
import server.database.Database;

public class FieldDAOTest {
	
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
	private FieldDAO dbField;

	@Before
	public void setUp() throws Exception {

		db = new Database();
		db.startTransaction();
		dbField = db.getFieldDAO();
	}

	@After
	public void tearDown() throws Exception {
		
		db.endTransaction(false);
		
		db = null;
		dbField = null;
	}
	
	@Test
	public void testGetFieldInfo() throws DatabaseException
	{
		Field field1 = new Field(0, "name1", 10, 10, "file11", "file21", 1, 1);
		Field field2 = new Field(0, "name2", 10, 10, "file12", "file22", 2, 1);
		Field field3 = new Field(0, "name3", 10, 10, "file13", "file23", 2, 3);
		
		dbField.add(field1);
		dbField.add(field2);
		dbField.add(field3);
		
		ArrayList<Field> temp1 = dbField.getFieldInfo(1);
		ArrayList<Field> temp2 = dbField.getFieldInfo(2);
		
		assertEquals(temp1.get(0).getTitle(), "name1");
		assertEquals(temp1.get(0).getFieldHelpPath(), "file11");
		assertEquals(temp1.get(0).getKnownDataPath(), "file21");
		
		assertEquals(temp2.get(0).getTitle(), "name2");
		assertEquals(temp2.get(0).getFieldHelpPath(), "file12");
		assertEquals(temp2.get(0).getKnownDataPath(), "file22");
		
		assertEquals(temp2.get(1).getTitle(), "name3");
		assertEquals(temp2.get(1).getFieldHelpPath(), "file13");
		assertEquals(temp2.get(1).getKnownDataPath(), "file23");
	}
	
	@Test
	public void testAdd() throws DatabaseException {
		
		Field field1 = new Field(0, "name1", 10, 10, "file11", "file21", 1, 1);
		Field field2 = new Field(0, "name2", 10, 10, "file12", "file22", 2, 1);
		Field field3 = new Field(0, "name3", 10, 10, "file13", "file23", 2, 3);
		
		dbField.add(field1);
		dbField.add(field2);
		dbField.add(field3);
		
		assertNotNull(dbField.getFieldInfo(1));
	}
	
	@Test
	public void testGetNumFields() throws DatabaseException {
		
		Field field1 = new Field(0, "name1", 10, 10, "file11", "file21", 1, 1);
		Field field2 = new Field(0, "name2", 10, 10, "file12", "file22", 2, 1);
		Field field3 = new Field(0, "name3", 10, 10, "file13", "file23", 2, 3);
		
		dbField.add(field1);
		dbField.add(field2);
		dbField.add(field3);
		
		int numFields = db.getFieldDAO().getNumFields(1);
		assertEquals(numFields, 1);
	}
	
	@Test
	public void IsField() throws DatabaseException {
		
		Field field = new Field(0, "name1", 10, 10, "file11", "file21", 1, 1);

		
		dbField.add(field);

		assertTrue(db.getFieldDAO().isField(1));
		assertFalse(db.getFieldDAO().isField(9));
	}
	
	@Test
	public void testGetNumFields2() throws DatabaseException {
		
		int i = db.getFieldDAO().getNumFields(1);
		assertEquals(i,0);
	}

	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException {
		
		Field invalidField = new Field (-1, null, 0, 0, null, null, 0, 0);
		dbField.add(invalidField);
	}
}
