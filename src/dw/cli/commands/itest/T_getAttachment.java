package dw.cli.commands.itest;

import static org.junit.Assert.*;

import java.io.File;

import dw.cli.Output;
import dw.cli.itest.TestHelper;
import dw.xmlrpc.itest.TestParams;

public class T_getAttachment extends TestHelper {
	@org.junit.Test
	public void canGetAttachment() throws Exception {
		File localFile = new File("myFile.gif");

		//Make sure we're in a clean state
		localFile.delete();
		assertFalse(localFile.exists());

		Output output = runWithArguments("getAttachment", "ro_for_tests:img1.gif", "myFile.gif");
		assertEquals("", output.out);
		assertEquals("", output.err);
		assertEquals(0, output.exitCode);

		assertTrue(localFile.exists());
		assertFileEquals(new File(TestParams.localFileToUpload), new File("myFile.gif"));
	}

	@org.junit.Test
	public void attachmentDoesntExist() throws Exception {
		Output output = runWithArguments("getAttachment", "ro_for_tests:unexisting_file.gif", "myFile.gif");
		assertNotNullOrEmpty(output.err);
		assertEquals("", output.out);
		assertNotZero(output.exitCode);
	}
}