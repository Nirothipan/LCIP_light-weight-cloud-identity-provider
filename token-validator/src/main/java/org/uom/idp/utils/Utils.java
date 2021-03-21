package org.uom.idp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uom.idp.ValidatorLambda;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utils class.
 *
 * @since 1.0.0
 */
public class Utils {

    private static final Logger LOG = LoggerFactory.getLogger(ValidatorLambda.class);
    private static final Pattern varPattern = Pattern.compile("\\$\\{([^}]*)}");

    private Utils() {

    }

    /**
     * This method is used to load yaml file into a configuration class.
     *
     * @param fileName Configuration file name
     * @param clazz    Class into which the configuration file should be loaded into
     * @return Configuration Object
     */
    public static <T> T loadConfig(String fileName, Class<T> clazz) throws RuntimeException {

        InputStream licenseGeneratorConfigInputstream = Utils.class.getClassLoader().getResourceAsStream(fileName);
        String yamlFileString = null;
        if (licenseGeneratorConfigInputstream != null) {
            try (Scanner scanner = new Scanner(licenseGeneratorConfigInputstream, StandardCharsets.UTF_8.name())) {
                yamlFileString = scanner.useDelimiter("\\A").next();
                yamlFileString = substituteVariables(yamlFileString);

            }
        }

        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
        return yaml.loadAs(yamlFileString, clazz);
    }

    /**
     * Replace system property holders in the property values.
     *
     * @param value string value to substitute
     * @return String substituted string
     */
    private static String substituteVariables(String value) {

        Matcher matcher = varPattern.matcher(value);
        boolean found = matcher.find();
        if (!found) {
            return value;
        }
        StringBuffer sb = new StringBuffer();
        do {
            String sysPropKey = matcher.group(1);
            String sysPropValue = getSystemVariableValue(sysPropKey);
            if (sysPropValue == null || sysPropValue.length() == 0) {
                String msg = "System property " + sysPropKey + " is not specified";
                LOG.error(msg);
                throw new RuntimeException(msg);
            }
            sysPropValue = sysPropValue.replace("\\", "\\\\");
            matcher.appendReplacement(sb, sysPropValue);
        } while (matcher.find());
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * A utility which allows reading variables from the environment or System properties.
     * If the variable in available in the environment as well as a System property, the System property takes
     * precedence.
     *
     * @param variableName System/environment variable name
     * @return value of the system/environment variable
     */
    private static String getSystemVariableValue(String variableName) {

        String value = System.getProperty(variableName);
        if (value != null) {
            return value;
        }
        value = System.getenv(variableName);
        return value;
    }

}

