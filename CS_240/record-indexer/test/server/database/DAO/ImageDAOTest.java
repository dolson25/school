package server.database.DAO;

import static org.junit.Assert.*;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import java.io.File;
import java.io.IOException;
import server.database.DatabaseException;
import shared.model.Image;
import server.database.Database;

public class ImageDAOTest {
	
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
	private ImageDAO dbImage;

	@Before
	public void setUp() throws Exception {

		db = new Database();
		db.startTransaction();
		dbImage = db.getImageDAO();
	}

	@After
	public void tearDown() throws Exception {
		
		db.endTransaction(false);
		
		db = null;
		dbImage = null;
	}
	
	@Test
	public void testGetSampleImage() throws DatabaseException
	{
		Image image1 = new Image(0,"file1",1, 0);
		Image image2 = new Image(0,"file2",1, 1);
		Image image3 = new Image(0,"file3",2, 0);
		Image image4 = new Image(0,"file4",2, 1);
		Image image5 = new Image(0,"file5",2, 1);
		
		dbImage.add(image1);
		dbImage.add(image2);
		dbImage.add(image3);
		dbImage.add(image4);
		dbImage.add(image5);
		
		Image temp1 = dbImage.getSampleImage(1);
		Image temp2 = dbImage.getSampleImage(2);

		assertEquals(temp1.getFilepath(), "file1");
		assertEquals(temp2.getFilepath(), "file3");
	}
	
	
	@Test
	public void testGetAvailableImage() throws DatabaseException
	{
		Image image1 = new Image(0,"file1",1, 0);
		Image image2 = new Image(0,"file2",1, 1);
		Image image3 = new Image(0,"file3",2, 0);
		Image image4 = new Image(0,"file4",2, 1);
		Image image5 = new Image(0,"file5",2, 1);
		
		dbImage.add(image1);
		dbImage.add(image2);
		dbImage.add(image3);
		dbImage.add(image4);
		dbImage.add(image5);
		
		Image temp1 = dbImage.getAvailableImage(3);
		Image temp2 = dbImage.getAvailableImage(2);
		
		assertEquals(temp2.getFilepath(), "file4");
	}
	
	@Test
	public void testUpdateAvailable() throws DatabaseException{
		
		Image image1 = new Image(0,"file1",1, 0);
		Image image2 = new Image(0,"file2",2, 1);
		
		dbImage.add(image1);
		dbImage.add(image2);
		
		dbImage.updateAvailable(1, 1);
		dbImage.updateAvailable(0, 2);
	}
	
	@Test
	public void testIsAvailable() throws DatabaseException{
		
		Image image1 = new Image(0,"file1",1, 1);
		Image image2 = new Image(0,"file2",2, 0);
		
		dbImage.add(image1);
		dbImage.add(image2);
	}
	
	@Test
	public void testAdd() throws DatabaseException {
		
		Image image = new Image(0,"file", 1, 1);
		
		dbImage.add(image);

		assertNotNull(dbImage.getAvailableImage(1));
	}
	
	@Test
	public void testGetProjectID() throws DatabaseException {
		
		Image image = new Image(0,"file", 1, 1);
		
		dbImage.add(image);
		
		int i = db.getImageDAO().getProjectID(1);

		assertEquals(i,1);
	}
	
	@Test
	public void testInvalidGetSampleImage() throws DatabaseException{
		Image image1 = new Image(0,"file1",1, 0);
		Image image2 = new Image(0,"file2",2, 1);
		
		dbImage.add(image1);
		dbImage.add(image2);
		
		Image tempImage = dbImage.getSampleImage(5);
		assertNull(tempImage);
	}
	
	@Test
	public void testInvalidGetAvailableImage() throws DatabaseException{
		Image image1 = new Image(0,"file1",1, 0);
		Image image2 = new Image(0,"file2",2, 1);
		
		dbImage.add(image1);
		dbImage.add(image2);
		
		Image tempImage = dbImage.getAvailableImage(5);
		assertNull(tempImage);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException {
		
		Image invalidImage = new Image(-1,null,0,0);
		dbImage.add(invalidImage);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidUpdateAvailable() throws DatabaseException {
		
		dbImage.updateAvailable(0, 1);
	}
	
	@Test
	public void testInvalidGetProjectID() throws DatabaseException {
		
		Image image = new Image(0,"file", 1, 1);
		
		dbImage.add(image);
		
		int i = db.getImageDAO().getProjectID(2);

		assertEquals(i,0);
	}

}
