package server.database.DAO;

import static org.junit.Assert.*;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import java.io.File;
import java.io.IOException;
import server.database.DatabaseException;
import shared.model.Record;
import server.database.Database;

public class RecordDAOTest {
	
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
	private RecordDAO dbRecord;

	@Before
	public void setUp() throws Exception {

		db = new Database();
		db.startTransaction();
		dbRecord = db.getRecordDAO();
	}

	@After
	public void tearDown() throws Exception {
		
		// Roll back transaction so changes to database are undone
		db.endTransaction(false);
		
		db = null;
		dbRecord = null;
	}

	@Test
	public void testAdd() throws DatabaseException {
		
		Record record1 = new Record(0, 3, 10);
		Record record2 = new Record(0, 1, 10);
		Record record3 = new Record(0, 3, 1);
		
		dbRecord.add(record1);
		dbRecord.add(record2);
		dbRecord.add(record3);
	}
	
	@Test
	public void testGetRecord() throws DatabaseException{
		
		Record Record1 = new Record(0, 3, 10);
		Record Record2 = new Record(0, 1, 10);
		Record Record3 = new Record(0, 3, 1);
		
		dbRecord.add(Record1);
		dbRecord.add(Record2);
		dbRecord.add(Record3);
	}
}
