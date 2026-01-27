package aidl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AidlCompiler
 * 负责实现 aidl -> java 的编译。
 */
public class AidlCompiler {

    private final File aidlExe;

    public AidlCompiler(File aidlExe) {
        if (!aidlExe.exists()) {
            throw new IllegalArgumentException("Invalid aidl executable: " + aidlExe);
        }
        this.aidlExe = aidlExe;
    }

    /**
     * 编译单个 aidl
     * Java 文件将生成在 aidl 文件所在目录
     */
    public void compile(File aidlFile) throws IOException, InterruptedException {
        List<String> cmd = new ArrayList<>();
        cmd.add(aidlExe.getAbsolutePath());
        cmd.add(aidlFile.getAbsolutePath());

        System.out.println("AidlCompiler(exec): " + cmd);

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);

        Process p = pb.start();
        p.getInputStream().transferTo(System.out);

        int code = p.waitFor();
        if (code != 0) {
            throw new RuntimeException("aidl failed: " + aidlFile);
        }
    }
}
