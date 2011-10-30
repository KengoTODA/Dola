package jp.skypencil.enchantjs.dola;

import org.junit.Ignore;
import org.junit.Test;

public class MainTest {

	@Test(expected=IllegalArgumentException.class)
	public void testParseNoArgs() {
		String[] args = {  };
		new Main().parse(args);
	}

	@Test(expected=IllegalArgumentException.class)
	@Ignore("this test will fail. see https://github.com/kohsuke/args4j/pull/4")
	public void testParseEmpty() {
		String[] args = { "" };
		new Main().parse(args);
	}

	@Test
	public void testParsePackage() {
		String[] args = { "package" };
		new Main().parse(args);
	}

	@Test(expected = IllegalArgumentException.class)
	@Ignore("this test will fail. see https://github.com/kohsuke/args4j/pull/4")
	public void testParseUnknownCommand() {
		String[] args = { "unknown" };
		new Main().parse(args);
	}

}
