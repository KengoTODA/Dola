package jp.skypencil.enchantjs.dola;

import static com.google.common.base.Preconditions.checkNotNull;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// memo
// clean -> convert -> join -> compress -> test -> clean ... (auto execute)
// -> package (deploy)

// `clean` cleans result/ directory
// `lint` ?
// `convert` makes result/converted_coffee.js, result/converted_sass.css, result/converted_less.css
// `join` makes result/joined.js, result/joind.css
// `compress` makes result/compressed.js, result/compressed.css
// `test` tests product
// `package` makes result/index.html and TITLE_VERSION.zip for upload 9leap.net

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	private final Parameter param;

	public Main() {
		this(new Parameter());
	}

	public Main(Parameter param) {
		checkNotNull(param);
		this.param = param;
	}

	public static void main(String[] args) {
		try {
			new Main().parse(args).run();
		} catch (Throwable t) {
			logger.error("fail to execute", t);
		}
	}

	Main parse(String[] args) {
		CmdLineParser parser = new CmdLineParser(param);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			parser.printUsage(System.err);
			throw new IllegalArgumentException(e);
		}

		if (param.isUsageFlag()) {
			parser.printUsage(System.err);
		}
		return this;
	}

	void run() {
		if (param.isUsageFlag()) {
			return;
		}
		param.getCommand().run(param);
	}
}
