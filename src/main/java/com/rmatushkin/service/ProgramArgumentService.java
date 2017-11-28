package com.rmatushkin.service;

import com.rmatushkin.exception.ProgramArgumentException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.HashMap;
import java.util.Map;

public class ProgramArgumentService {
    private static final String[] PARAMETERS = {"n", "l", "f", "o"};

    public Map<String, String> parse(String[] args) {
        CommandLine commandLine = buildCommandLine(args);

        Map<String, String> valuesByParameters = new HashMap<>();
        for (Option option : commandLine.getOptions()) {
            String key = option.getOpt();
            String value = option.getValue();
            valuesByParameters.put(key, value);
        }

        return valuesByParameters;
    }

    private static CommandLine buildCommandLine(String[] args) {
        Options options = buildOptions();
        CommandLineParser commandLineParser = new DefaultParser();
        try {
            return commandLineParser.parse(options, args);
        } catch (ParseException e) {
            throw new ProgramArgumentException(e.getMessage());
        }
    }

    private static Options buildOptions() {
        Options options = new Options();
        options.addRequiredOption(PARAMETERS[0], PARAMETERS[0], true, ""); //TODO: write the description
        options.addOption(PARAMETERS[1], PARAMETERS[1], false, ""); //TODO: write the description
        options.addRequiredOption(PARAMETERS[2], PARAMETERS[2], true, ""); //TODO: write the description
        options.addRequiredOption(PARAMETERS[3], PARAMETERS[3], true, ""); //TODO: write the description
        return options;
    }
}
