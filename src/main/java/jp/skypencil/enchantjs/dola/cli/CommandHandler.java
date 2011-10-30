package jp.skypencil.enchantjs.dola.cli;

import jp.skypencil.enchantjs.dola.command.Command;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.EnumOptionHandler;
import org.kohsuke.args4j.spi.Setter;

public class CommandHandler extends EnumOptionHandler<Command> {

	public CommandHandler(CmdLineParser parser, OptionDef option, Setter<? super Command> setter) {
		super(parser, option, setter, Command.class);
	}

	@Override
	public String getDefaultMetaVariable() {
		return "COMMAND";
	}

}
