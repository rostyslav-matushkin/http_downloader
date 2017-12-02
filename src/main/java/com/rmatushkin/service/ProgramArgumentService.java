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

    public Map<String, String> parse(String[] args, String[] parameters) {
        CommandLine commandLine = buildCommandLine(args, parameters);

        Map<String, String> parametersToValues = new HashMap<>();
        for (Option option : commandLine.getOptions()) {
            String key = option.getOpt();
            String value = option.getValue();
            parametersToValues.put(key, value);
        }

        return parametersToValues;
    }

    private static CommandLine buildCommandLine(String[] args, String[] parameters) {
        Options options = buildOptions(parameters);
        CommandLineParser commandLineParser = new DefaultParser();
        try {
            return commandLineParser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            throw new ProgramArgumentException();
        }
    }

    private static Options buildOptions(String[] parameters) {
        Options options = new Options();
        options.addOption(parameters[0], parameters[0], false, ""); //TODO: write the description
        options.addOption(parameters[1], parameters[1], false, ""); //TODO: write the description
        options.addRequiredOption(parameters[2], parameters[2], true, ""); //TODO: write the description
        options.addRequiredOption(parameters[3], parameters[3], true, ""); //TODO: write the description
        return options;
    }
}
