package com.rmatushkin;

import com.rmatushkin.exception.ProgramArgumentParserException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.HashMap;
import java.util.Map;

public class ProgramArgumentParser {
    private static final String[] PARAMETERS = {"n", "l", "f", "o"};

    public static Map<String, String> parse(String[] args) {
        Map<String, String> valuesByParameters = new HashMap<>();
        CommandLine commandLine = buildCommandLine(args);
        for (String parameter : PARAMETERS) {
            String optionValue = commandLine.getOptionValue(parameter);
            valuesByParameters.put(parameter, optionValue);
        }
        return valuesByParameters;
    }

    private static CommandLine buildCommandLine(String[] args) {
        Options options = buildOptions();
        CommandLineParser commandLineParser = new DefaultParser();
        try {
            return commandLineParser.parse(options, args);
        } catch (ParseException e) {
            throw new ProgramArgumentParserException(e.getMessage());
        }
    }

    private static Options buildOptions() {
        Options options = new Options();
        options.addOption(PARAMETERS[0], true, "parameter1"); //TODO: rename description
        options.addOption(PARAMETERS[1], true, "parameter2"); //TODO: rename description
        options.addOption(PARAMETERS[2], true, "parameter3"); //TODO: rename description
        options.addOption(PARAMETERS[3], true, "parameter4"); //TODO: rename description
        return options;
    }
}
