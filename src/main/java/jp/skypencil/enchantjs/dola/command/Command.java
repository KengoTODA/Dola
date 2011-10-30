package jp.skypencil.enchantjs.dola.command;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import jp.skypencil.enchantjs.dola.Parameter;


public enum Command {
	DOWNLOAD {
		@Override
		List<Step> createSteps(Parameter parameter) {
			List<Step> result = CLEAN.createSteps(parameter);
			result.add(
					new DownloadStep(
							parameter.getArgument(),
							parameter.getJavaScriptDir(),
							parameter.getCharset())
			);
			return result;
		}
	},
	CLEAN {
		@Override
		List<Step> createSteps(Parameter parameter) {
			return Lists.newArrayList(new Step[] {
					new CleanStep(
							parameter.getJavaScriptDir(),
							parameter.getCssDir(),
							parameter.getResultDir())
			});
		}
	},
	BUILD {
		@Override
		List<Step> createSteps(Parameter parameter) {
			List<Step> result = CLEAN.createSteps(parameter);
			result.addAll(Arrays.asList(new Step[] {
					new JoinStep(parameter.getResultDir(), parameter.getJavaScriptDir(), parameter.getCharset(), ".js"),
					new JoinStep(parameter.getResultDir(), parameter.getCssDir(), parameter.getCharset(), ".css"),
					new CompressStep(parameter.getResultDir(), ".js", parameter.getCharset())
			}));
			return result;
		}
	},
	TEST {
		@Override
		List<Step> createSteps(Parameter parameter) {
			List<Step> result = BUILD.createSteps(parameter);
			result.add(new TestStep(parameter.getTestCaseDir()));
			return result;
		}
	},
	PACKAGE {
		@Override
		List<Step> createSteps(Parameter parameter) {
			List<Step> result = TEST.createSteps(parameter);
			// TODO 
			return result;
		}
	};

	public void run(Parameter parameter) {
		for (Step step : createSteps(parameter)) {
			step.run();
		}
	}

	abstract List<Step> createSteps(Parameter parameter);
}
