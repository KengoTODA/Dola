package jp.skypencil.enchantjs.dola.cli;


import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

public class CharsetHandler extends OptionHandler<Charset> {

	public CharsetHandler(CmdLineParser parser, OptionDef option, Setter<? super Charset> setter) {
		super(parser, option, setter);
	}

	@Override
	public int parseArguments(Parameters params) throws CmdLineException {
		String arg = params.getParameter(0);
		try {
			Charset charset = Charset.forName(arg);
			setter.addValue(charset);
		} catch (IllegalCharsetNameException e) {
			throw new CmdLineException(owner, e);
		}
		return 1;
	}

	@Override
	public String getDefaultMetaVariable() {
		return "CHARSET";
	}

}
