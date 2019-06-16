package server.database.DAO;

import static org.junit.Assert.*;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import java.io.File;
import java.io.IOException;
import server.database.DatabaseException;
import shared.model.Cell;
import server.database.Database;

public class CellDAOTest {
	
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
	private CellDAO dbCell;

	@Before
	public void setUp() throws Exception {

		db = new Database();
		db.startTransaction();
		dbCell = db.getCellDAO();
	}

	@After
	public void tearDown() throws Exception {

		db.endTransaction(false);
		
		db = null;
		dbCell = null;
	}
	
	@Test
	public void testAdd() throws DatabaseException {
		
		Cell cell1 = new Cell(0, 3, 10, "bill");
		Cell cell2 = new Cell(0, 1, 10, "josh");
		Cell cell3 = new Cell(0, 3, 1, "45");
		
		dbCell.add(cell1);
		dbCell.add(cell2);
		dbCell.add(cell3);
	}
}
