package server;

import org.junit.* ;
import static org.junit.Assert.* ;

public class ServerUnitTests {
	
	@Before
	public void setup() {
	}
	
	@After
	public void teardown() {
	}
	
	@Test
	public void test_1() {
		assertEquals("OK", "OK");
		assertTrue(true);
		assertFalse(false);
	}

	public static void main(String[] args) {
		
		String[] testClasses = new String[] {
				"server.ServerUnitTests",
				"server.database.DAO.UserDAOTest",
				"server.database.DAO.ProjectDAOTest",
				"server.database.DAO.ImageDAOTest",
				"server.database.DAO.FieldDAOTest",
				"server.database.DAO.RecordDAOTest",
				"server.database.DAO.CellDAOTest"
		};

		org.junit.runner.JUnitCore.main(testClasses);
	}
	
}

