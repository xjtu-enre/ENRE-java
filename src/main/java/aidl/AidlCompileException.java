package aidl;

/**
 * aidl.exe 编译失败时抛出
 * 保存 stderr，供 ErrorParser 使用
 */
public class AidlCompileException extends RuntimeException {

    private final String stdout;
    private final String stderr;

    public AidlCompileException(String message, String stdout, String stderr) {
        super(message);
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public String getStdout() {
        return stdout;
    }

    public String getStderr() {
        return stderr;
    }
}
