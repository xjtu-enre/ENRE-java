package aidl;

import java.nio.file.Path;

public class AidlImportError {

    private final Path aidlFile;
    private final int line;
    private final String missingClass;

    public AidlImportError(Path aidlFile, int line, String missingClass) {
        this.aidlFile = aidlFile;
        this.line = line;
        this.missingClass = missingClass;
    }

    public Path getAidlFile() { return aidlFile; }
    public int getLine() { return line; }
    public String getMissingClass() { return missingClass; }

    public String getPackageName() {
        int idx = missingClass.lastIndexOf('.');
        return idx < 0 ? "" : missingClass.substring(0, idx);
    }

    public String getSimpleName() {
        int idx = missingClass.lastIndexOf('.');
        return idx < 0 ? missingClass : missingClass.substring(idx + 1);
    }
}
