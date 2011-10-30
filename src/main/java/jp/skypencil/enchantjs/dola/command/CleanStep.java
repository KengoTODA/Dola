package jp.skypencil.enchantjs.dola.command;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CleanStep implements Step {

	private final File targetDir;
	private final File javaScriptDir;
	private final File cssDir;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	CleanStep(File javaScriptDir, File cssDir, File targetDir) {
		checkNotNull(javaScriptDir);
		checkNotNull(cssDir);
		checkNotNull(targetDir);
		checkArgument(!javaScriptDir.exists() || javaScriptDir.isDirectory());
		checkArgument(!cssDir.exists() || cssDir.isDirectory());
		checkArgument(!targetDir.exists() || targetDir.isDirectory());
		this.javaScriptDir = javaScriptDir;
		this.cssDir = cssDir;
		this.targetDir = targetDir;
	}

	public void run() {
		logger.info("cleaning...");
		if (!javaScriptDir.exists()) {
			javaScriptDir.mkdir();
		}
		if (!cssDir.exists()) {
			cssDir.mkdir();
		}
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		} else {
			removeChildren(targetDir);
		}
	}

	private void removeChildren(File dir) {
		assert dir.isDirectory();

		for (File child : dir.listFiles()) {
			if (child.isDirectory()) {
				removeChildren(child);
			}
			child.delete();
		}
	}

}
