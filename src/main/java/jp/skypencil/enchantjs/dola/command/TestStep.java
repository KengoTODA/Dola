package jp.skypencil.enchantjs.dola.command;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestStep implements Step {

	private final File testCaseDir;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public TestStep(File testCaseDir) {
		checkNotNull(testCaseDir);
		checkArgument(testCaseDir.isDirectory());
		checkArgument(testCaseDir.exists());
		this.testCaseDir = testCaseDir;
	}

	public void run() {
		logger.info("testing...");
		// TODO kick QUnit (use phantomjs?)
	}

}
