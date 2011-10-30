package jp.skypencil.enchantjs.dola;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import jp.skypencil.enchantjs.dola.cli.CharsetHandler;
import jp.skypencil.enchantjs.dola.cli.CommandHandler;
import jp.skypencil.enchantjs.dola.command.Command;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

public class Parameter {
	@Argument(index=0, metaVar = "command", required=true, handler=CommandHandler.class)
	private Command command;

	@Option(name = "-j", aliases = "--javascript")
	private File javaScriptDir = new File("js");

	@Option(name = "-c", aliases = "--css")
	private File cssDir = new File("css");

	@Option(name = "-i", aliases = "--image")
	private File imageDir = new File("image");

	@Option(name = "--charset", handler=CharsetHandler.class)
	private Charset charset = Charset.defaultCharset();

	@Option(name = "-t", aliases = "--testcase")
	private File testCaseDir = new File("test");
	
	@Option(name = "-r", aliases = "--result")
	private File resultDir = new File("result");

	@Option(name = "-h", aliases = "--help", usage = "print usage message")
	private boolean usageFlag;

	@Argument(index=1)
	private List<String> argument = Lists.newArrayList();

	Command getCommand() {
		return command;
	}

	boolean isUsageFlag() {
		return usageFlag;
	}

	public File getJavaScriptDir() {
		return javaScriptDir;
	}

	public File getCssDir() {
		return cssDir;
	}

	public File getImageDir() {
		return imageDir;
	}

	public Charset getCharset() {
		return charset;
	}

	public File getResultDir() {
		return resultDir;
	}

	public File getTestCaseDir() {
		return testCaseDir;
	}

	public List<String> getArgument() {
		return argument;
	}

	@VisibleForTesting
	void setCommand(Command command) {
		this.command = command;
	}

	@VisibleForTesting
	void setJavaScriptDir(File javaScriptDir) {
		this.javaScriptDir = javaScriptDir;
	}

	@VisibleForTesting
	void setCssDir(File cssDir) {
		this.cssDir = cssDir;
	}

	@VisibleForTesting
	void setImageDir(File imageDir) {
		this.imageDir = imageDir;
	}

	@VisibleForTesting
	void setCharset(Charset charset) {
		this.charset = charset;
	}

	@VisibleForTesting
	void setTestCaseDir(File testCaseDir) {
		this.testCaseDir = testCaseDir;
	}

	@VisibleForTesting
	void setResultDir(File resultDir) {
		this.resultDir = resultDir;
	}

	@VisibleForTesting
	void setUsageFlag(boolean usageFlag) {
		this.usageFlag = usageFlag;
	}

	@VisibleForTesting
	void setArgument(List<String> argument) {
		this.argument = argument;
	}
}
