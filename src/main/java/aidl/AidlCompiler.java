package aidl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AidlCompiler
 * 负责实现 aidl -> java 的编译。
 */
public class AidlCompiler {

    private final File aidlExe;
    private final List<File> includeDirs;
    private final List<File> succeededFiles = new ArrayList<>();
    private final List<File> failedFiles = new ArrayList<>();

    public AidlCompiler(File aidlExe, List<File> includeDirs) {
        if (!aidlExe.exists()) {
            throw new IllegalArgumentException("Invalid aidl executable: " + aidlExe);
        }
        this.aidlExe = aidlExe;
        this.includeDirs = includeDirs;
    }

    /**
     * 编译单个 aidl
     * Java 文件将生成在 aidl 文件所在目录
     */
    public void compile(File aidlFile) {
        if (aidlFile == null || !aidlFile.exists()) {
            System.err.println("AIDL file does not exist: " + aidlFile);
            failedFiles.add(aidlFile);
            return;
        }

        // Check if the file defines a parcelable
        if (isParcelableFile(aidlFile)) {
            System.out.println("Skipping Parcelable declaration file: " + aidlFile);
            return;
        }

        List<String> cmd = new ArrayList<>();
        cmd.add(aidlExe.getAbsolutePath());

        for (File dir : includeDirs) {
            cmd.add("-I " + dir.getAbsolutePath());
        }

        cmd.add(aidlFile.getAbsolutePath());

        System.out.println("AidlCompiler(exec): " + cmd);

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);

        try {
            Process process = pb.start();

            // Read and log the output
            String output = readStream(process.getInputStream());
            System.out.println(output);

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                if (!succeededFiles.contains(aidlFile)) {
                    succeededFiles.add(aidlFile);
                }
                System.out.println("✅ Success: " + aidlFile);
            } else {
                if (!failedFiles.contains(aidlFile)) {
                    failedFiles.add(aidlFile);
                }
                System.err.println("❌ Failed: " + aidlFile);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error running aidl.exe for: " + aidlFile);
            failedFiles.add(aidlFile);
        }
        System.out.println("failedFiles: "+ failedFiles.size());
//        System.out.println(failedFiles);
    }

    public List<File> getSucceededFiles() {
        return succeededFiles;
    }

    public List<File> getFailedFiles() {
        return failedFiles;
    }

    private boolean isParcelableFile(File aidlFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(aidlFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("parcelable")) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + aidlFile);
        }
        return false;
    }

    private String readStream(java.io.InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}