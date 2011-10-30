package jp.skypencil.enchantjs.dola.command;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

import com.google.common.io.Files;

public class CleanStepTest {
	private File jsDir= Files.createTempDir();
	private File cssDir = Files.createTempDir();
	private File targetDir= Files.createTempDir();
	private CleanStep step = new CleanStep(jsDir, cssDir, targetDir);

	@After
	public void removeTempDir() {
		targetDir.delete();
	}

	@Test
	public void testCleanStepRemoveChildFile() throws IOException {
		File childFile = new File(targetDir, "child.txt");
		assertThat(childFile.createNewFile(), is(true));
		step.run();
		assertThat(targetDir.exists(), is(true));
		assertThat(childFile.exists(), is(false));
	}

	@Test
	public void testCleanStepRemoveChildDir() throws IOException {
		File childDir = new File(targetDir, "child.d");
		assertThat(childDir.mkdir(), is(true));
		File grandchildFile = new File(childDir, "grandchild.txt");
		assertThat(grandchildFile.createNewFile(), is(true));
		step.run();
		assertThat(targetDir.exists(), is(true));
		assertThat(childDir.exists(), is(false));
		assertThat(grandchildFile.exists(), is(false));
	}

	@Test
	public void testJsDirDoesNotExist() {
		jsDir.delete();
		assertThat(jsDir.exists(), is(false));
		step.run();
		assertThat(jsDir.exists(), is(true));
		assertThat(jsDir.isDirectory(), is(true));
	}

	@Test
	public void testCssDirDoesNotExist() {
		cssDir.delete();
		assertThat(cssDir.exists(), is(false));
		step.run();
		assertThat(cssDir.exists(), is(true));
		assertThat(cssDir.isDirectory(), is(true));
	}

	@Test
	public void testTargetDirDoesNotExist() throws IOException {
		targetDir.delete();
		assertThat(targetDir.exists(), is(false));
		step.run();
		assertThat(targetDir.exists(), is(true));
		assertThat(targetDir.isDirectory(), is(true));
	}

}
