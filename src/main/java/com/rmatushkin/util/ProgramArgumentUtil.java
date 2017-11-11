package com.rmatushkin.util;

import com.rmatushkin.exception.ProgramArgumentParserException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.HashMap;
import java.util.Map;

public class ProgramArgumentUtil {

    public static Map<String, String> parse(String[] args, String[] parameters) {
        Map<String, String> valuesByParameters = new HashMap<>();
        CommandLine commandLine = buildCommandLine(args, parameters);
        for (String parameter : parameters) {
            String optionValue = commandLine.getOptionValue(parameter);
            valuesByParameters.put(parameter, optionValue);
        }
        return valuesByParameters;
    }

    private static CommandLine buildCommandLine(String[] args, String[] parameters) {
        Options options = buildOptions(parameters);
        CommandLineParser commandLineParser = new DefaultParser();
        try {
            return commandLineParser.parse(options, args);
        } catch (ParseException e) {
            throw new ProgramArgumentParserException(e.getMessage());
        }
    }

    private static Options buildOptions(String[] parameters) {
        Options options = new Options();
        options.addOption(parameters[0], true, "p1");
        options.addOption(parameters[1], true, "p2");
        options.addOption(parameters[2], true, "p3");
        options.addOption(parameters[3], true, "p4");
        return options;
    }
}
