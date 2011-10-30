package jp.skypencil.enchantjs.dola.command;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.isdc.wro.extensions.processor.css.YUICssCompressorProcessor;
import ro.isdc.wro.extensions.processor.js.YUIJsCompressorProcessor;
import ro.isdc.wro.model.resource.processor.ResourcePostProcessor;

import com.google.common.io.Closeables;
import com.google.common.io.Files;

class CompressStep implements Step {

	private final File resultDir;
	private final String targetExt;
	private final Charset charset;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public CompressStep(File resultDir, String targetExt, Charset charset) {
		checkNotNull(resultDir);
		checkNotNull(targetExt);
		checkNotNull(charset);
		this.resultDir = resultDir;
		this.targetExt = targetExt;
		this.charset = charset;
	}

	public void run() {
		checkState(resultDir.isDirectory());
		checkState(targetExt.startsWith("."));
		checkState(targetExt.length() > 1);
		logger.info("compressing...");
		File input = new File(resultDir, "joined" + targetExt);
		File output = new File(resultDir, "compressed" + targetExt);
		Reader reader = null;
		Writer writer = null;
		IOException exception = null;

		try {
			reader = Files.newReader(input, charset);
			writer = Files.newWriter(output, charset);
			compress(reader, writer);
		} catch (IOException e) {
			exception = e;
		} finally {
			Closeables.closeQuietly(reader);
			try {
				Closeables.close(writer, false);
			} catch (IOException e) {
				if (exception != null) {
					e.printStackTrace();	// TODO use logger
				} else {
					exception = e;
				}
			}
		}
		if (exception != null) {
			throw new RuntimeException(exception);
		}
	}

	protected void compress(Reader reader, Writer writer) throws IOException {
		ResourcePostProcessor processor = null;
		if (targetExt.equals(".js")) {
			processor = YUIJsCompressorProcessor.doMungeCompressor();
		} else if (targetExt.equals(".css")) {
			processor = new YUICssCompressorProcessor();
		}
		processor.process(reader, writer);
	}

}
