package jp.skypencil.enchantjs.dola;

import java.io.File;
import java.util.Arrays;

import jp.skypencil.enchantjs.dola.command.Command;

import org.junit.Test;

import com.google.common.io.Files;

public class DownloadTest {
	private File jsDir= Files.createTempDir();

	@Test
	public void testJsDirDoesNotExist() {
		jsDir.delete();

		Parameter param = new Parameter();
		param.setCommand(Command.DOWNLOAD);
		param.setJavaScriptDir(jsDir);
		param.setArgument(Arrays.asList("enchant.js"));
		new Main(param).run();
	}
}
