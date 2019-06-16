package server.database.DAO;

import static org.junit.Assert.*;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import server.database.DatabaseException;
import shared.model.Project;
import server.database.Database;

public class ProjectDAOTest {
	
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
	private ProjectDAO dbProject;

	@Before
	public void setUp() throws Exception {

		db = new Database();
		db.startTransaction();
		dbProject = db.getProjectDAO();
	}

	@After
	public void tearDown() throws Exception {
		
		db.endTransaction(false);
		
		db = null;
		dbProject = null;
	}
	
	@Test
	public void testGetProject() throws DatabaseException{
		
		Project project = new Project(0,"marriages",10, 20, 10);
		
		dbProject.add(project);
		
		Project temp = dbProject.getProject(1);
		
		assertEquals(temp.getId(),1);
		assertEquals(temp.getTitle(), "marriages");
		assertEquals(temp.getRecordsPerImage(), 10);
		assertEquals(temp.getFirstYCoordinate(), 20);
		assertEquals(temp.getRecordHeight(), 10);
	}
	
	@Test
	public void testGetProjects() throws DatabaseException{
		
		Project project1 = new Project(0,"marriages",10, 20, 10);
		Project project2 = new Project(0,"deaths",100, 200, 100);
		Project project3 = new Project(0,"births",20, 40, 30);
		
		dbProject.add(project1);
		dbProject.add(project2);
		dbProject.add(project3);
		
		ArrayList<ArrayList<String>> temp = dbProject.getProjects();
		
		assertEquals(temp.get(0).get(0), "1");
		assertEquals(temp.get(0).get(1), "marriages");
		assertEquals(temp.get(1).get(0), "2");
		assertEquals(temp.get(1).get(1), "deaths");
		assertEquals(temp.get(2).get(0), "3");
		assertEquals(temp.get(2).get(1), "births");
	}
	

	
	@Test
	public void testAdd() throws DatabaseException {
		
		Project project = new Project(0,"draft", 10, 20, 10);
		
		dbProject.add(project);

		assertNotNull(dbProject.getProject(1));
	}

	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException {
		
		Project invalidProject = new Project(-1,null,0, 0, 0);
		dbProject.add(invalidProject);
	}
	
	@Test
	public void testInvalidGetProject() throws DatabaseException {
		
		Project temp = dbProject.getProject(1);
		assertNull(temp);
	}

}
