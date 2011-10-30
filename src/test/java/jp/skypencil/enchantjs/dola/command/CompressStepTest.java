package jp.skypencil.enchantjs.dola.command;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.junit.Test;

import com.google.common.io.Files;

public class CompressStepTest {
	private File resultDir = Files.createTempDir();

	@Test
	public void testCompressJs() throws IOException {
		String before = "function(){var myVariable = 0 ;}";
		Reader reader = new StringReader(before);
		StringWriter writer = new StringWriter();
		CompressStep step = new CompressStep(resultDir, ".js", Charset.defaultCharset());
		step.compress(reader, writer);
		String after = writer.toString();
		assertThat(after, is("function(){var a=0;}"));
	}

	@Test
	public void testCompressCss() throws IOException {
		String before = "h1 { color:#000000; }";
		Reader reader = new StringReader(before);
		StringWriter writer = new StringWriter();
		CompressStep step = new CompressStep(resultDir, ".css", Charset.defaultCharset());
		step.compress(reader, writer);
		String after = writer.toString();
		assertThat(after, is("h1{color:#000}"));
	}

}
