package jp.skypencil.enchantjs.dola.command;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class DownloadStepTest {

	@Test
	public void testToDownloadEnchantJs() {
		List<String> argument = Lists.newArrayList();
		argument.add("enchant.js");
		File javaScriptRoot = Files.createTempDir();
		DownloadStep step = new DownloadStep(
				argument,
				javaScriptRoot,
				Charset.defaultCharset());

		assertThat(new File(javaScriptRoot, "enchant.js").exists(), is(false));
		step.run();
		assertThat(new File(javaScriptRoot, "enchant.js").exists(), is(true));
	}

	@Test
	public void testToDownloadNineleapEnchantJs() {
		List<String> argument = Lists.newArrayList();
		argument.add("nineleap.enchant.js");
		File javaScriptRoot = Files.createTempDir();
		DownloadStep step = new DownloadStep(
				argument,
				javaScriptRoot,
				Charset.defaultCharset());

		assertThat(new File(javaScriptRoot, "nineleap.enchant.js").exists(), is(false));
		step.run();
		assertThat(new File(javaScriptRoot, "nineleap.enchant.js").exists(), is(true));
	}

}
