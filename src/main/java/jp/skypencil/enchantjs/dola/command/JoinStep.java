package jp.skypencil.enchantjs.dola.command;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.io.Closeables;
import com.google.common.io.Files;

class JoinStep implements Step {
	private final File resultDir;
	private final File targetDir;
	private final Charset charset;
	private final String targetExt;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public JoinStep(File resultDir, File targetDir, Charset charset, String targetExt) {
		checkNotNull(resultDir);
		checkNotNull(targetDir);
		checkNotNull(charset);
		checkNotNull(targetExt);
		this.resultDir = resultDir;
		this.targetDir = targetDir;
		this.charset = charset;
		this.targetExt = targetExt;
	}

	public void run() {
		checkState(resultDir.exists());
		checkState(resultDir.exists());
		checkState(targetDir.isDirectory());
		checkState(targetDir.isDirectory());
		checkState(targetExt.startsWith("."));
		checkState(targetExt.length() > 1);
		logger.info("joining " + targetExt + " files...");
		File resultFile = new File(resultDir, "joined" + targetExt);
		BufferedWriter writer = null;
		IOException exception = null;
		try {
			writer = Files.newWriter(resultFile, charset);
			for (File targetFile : sortByName(listTargetFiles(targetDir))) {
				// TODO embed images&sounds as data URI
				copy(targetFile, writer);
			}
		} catch (IOException e) {
			exception = e;
		} finally {
			try {
				Closeables.close(writer, false);
			} catch (IOException e) {
				if (exception != null) {
					logger.warn("IOException caused when writer was closed.", e);
				} else {
					exception = e;
				}
			}
		}
		if (exception != null) {
			throw new RuntimeException(exception);
		}
	}

	private List<File> sortByName(List<File> listTargetFiles) {
		Comparator<File> fileNameComparator = new Comparator<File>() {
			public int compare(File o1, File o2) {
				return o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
			}
		};

		List<File> result = Lists.newArrayList(listTargetFiles);
		Collections.sort(result, fileNameComparator);
		return result;
	}

	private List<File> listTargetFiles(File dir) {
		List<File> result = Lists.newArrayList();
		for (File child : dir.listFiles()) {
			if (child.isDirectory()) {
				result.addAll(listTargetFiles(child));
			} else if (child.isFile() && child.getName().endsWith(targetExt)) {
				result.add(child);
			}
		}
		return result;
	}

	private void copy(File targetFile, BufferedWriter writer) throws IOException {
		BufferedReader reader = new BufferedReader(Files.newReader(targetFile, charset));
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				embedTo(writer, line, targetFile);
			}
		} finally {
			Closeables.closeQuietly(reader);
		}
	}

	// TODO use AST
	private static final Pattern ASSETS_PATTERN = Pattern.compile(".*assets\\[['\"](.+)['\"]\\].*");
	private void embedTo(BufferedWriter writer, String line, File targetFile) throws IOException {
		if (!line.contains("assets[")) {
			writer.write(line);
			writer.write('\n');
			return;
		}

		Matcher m = ASSETS_PATTERN.matcher(line);
		if (m.matches()) {
			String resourceName = m.group(1);
			File resource = new File(targetFile.getParent(), resourceName);
			if (resource.exists()) {
				writer.write(line.substring(0, m.start(1)));
				appendHeader(writer, resourceName);
				appendEncodedResource(writer, resourceName, targetFile);
				writer.write(line.substring(m.end(1)));
			} else {
				writer.write(line);
			}
		} else {
			writer.write(line);
		}
		writer.write('\n');
	}

	private void appendHeader(BufferedWriter writer, String resourceName) throws IOException {
		writer.write("data:");
		if (resourceName.endsWith(".txt")) {
			writer.write("text/plain");
		} else if (resourceName.endsWith(".wav")) {
			writer.write("audio/wav");
		} else if (resourceName.endsWith(".png")) {
			writer.write("image/png");
		} else if (resourceName.endsWith(".jpg")) {
			writer.write("image/jpeg");
		} else if (resourceName.endsWith(".gif")) {
			writer.write("image/gif");
		} else {
			throw new UnsupportedOperationException("unknown resource type: " + resourceName);
		}
		writer.write(";base64,");
	}

	private void appendEncodedResource(BufferedWriter writer, String resourceName, File targetFile) throws IOException {
		File resource = new File(targetFile.getParent(), resourceName);
		Base64InputStream iStream = new Base64InputStream(Files.newInputStreamSupplier(resource).getInput(), true, 0, new byte[0]);
		Reader reader = new InputStreamReader(iStream);
		try {
			int length;
			char[] buffer = new char[1024];
			while ((length = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, length);
			}
		} finally {
			Closeables.closeQuietly(reader);
		}
	}

}
