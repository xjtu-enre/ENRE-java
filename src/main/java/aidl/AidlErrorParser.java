package aidl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AidlErrorParser {

    private static final Pattern IMPORT_ERROR_PATTERN =
            Pattern.compile("(.+\\.aidl):(\\d+):\\s+couldn't find import for class\\s+([a-zA-Z0-9_.]+)");

    /**
     * 解析 aidl.exe stderr 输出
     */
    public List<AidlImportError> parse(String stderr) {

        List<AidlImportError> errors = new ArrayList<>();

        if (stderr == null || stderr.isEmpty()) {
            return errors;
        }

        String[] lines = stderr.split("\\R");
        for (String line : lines) {
            Matcher m = IMPORT_ERROR_PATTERN.matcher(line);
            if (m.find()) {
                Path aidlFile = Paths.get(m.group(1)).toAbsolutePath().normalize();
                int lineNo = Integer.parseInt(m.group(2));
                String missingClass = m.group(3);

                errors.add(new AidlImportError(
                        aidlFile,
                        lineNo,
                        missingClass
                ));
            }
        }
        return errors;
    }
}
