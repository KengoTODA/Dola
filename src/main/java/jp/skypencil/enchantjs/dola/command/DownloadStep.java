package jp.skypencil.enchantjs.dola.command;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.io.Resources;

class DownloadStep implements Step {

	private static final Map<String, URL> registry = Maps.newHashMap();

	private final Logger logger = LoggerFactory.getLogger(getClass());

	static {
		try {
			registry.put("enchant.js", new URL("https://raw.github.com/wise9/enchant.js/master/enchant.js"));
			registry.put("nineleap.enchant.js", new URL("https://raw.github.com/wise9/enchant.js/master/plugins/nineleap.enchant.js"));
			registry.put("ui.enchant.js", new URL("https://raw.github.com/wise9/enchant.js/master/plugins/ui.enchant.js"));
		} catch (MalformedURLException e) {
			throw new AssertionError(e);
		}
	}

	private final ArrayList<String> targetLibs;
	private final Charset charset;
	private final File javaScriptDir;

	public DownloadStep(List<String> argument, File javascriptRoot, Charset charset) {
		checkNotNull(argument);
		checkNotNull(javascriptRoot);
		checkNotNull(charset);
		this.targetLibs = Lists.newArrayList(argument);
		this.javaScriptDir = javascriptRoot;
		this.charset = charset;
	}

	public void run() {
		checkState(javaScriptDir.isDirectory());
		logger.info("downloading...");
		for (String lib : targetLibs) {
			try {
				URL url = registry.get(lib.toLowerCase());
				if (url == null) {
					// FIXME error
				}
				String fileName = url.getFile();
				Files.write(
						Resources.toString(url, charset),
						new File(javaScriptDir, fileName.substring(fileName.lastIndexOf('/') + 1)),
						charset);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
