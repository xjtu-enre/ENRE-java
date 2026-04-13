package aidl;

import client.EnreCommand;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * ProjectContext
 * 用于 AidlPreprocessor，封装 ENRE-JAVA 现有参数，
 */
public class ProjectContext {

    private final EnreCommand app;

    /**
     * 构造
     * @param app EnreCommand 对象
     */
    public ProjectContext(EnreCommand app) {
        this.app = app;
    }

    /**
     * 获取源码根目录列表
     */
    public List<Path> getSourceRoots() {
        List<Path> roots = new ArrayList<>();
        roots.add(Paths.get(app.getSrc()));
        for (String dir : app.getDir()) {
            roots.add(Paths.get(dir));
        }
        return roots;
    }

    public List<File> getExtraAidlIncludes(ProjectContext context) {
        List<Path> sourceRoots = context.getSourceRoots();
        return sourceRoots.stream()
                .map(Path::toFile)
                .toList();
    }

    /**
     * 返回 aidl 可执行路径
     */
//    public Path getAidlExecutable() {
//        // aidl.exe 放在项目根的 aidl 文件夹
//        Path projectRoot = Paths.get(System.getProperty("user.dir")); // 项目根
//        Path aidlPath = projectRoot.resolve("src").resolve("main").resolve("java")
//                .resolve("aidl").resolve("aidl.exe");
//
//        if (!Files.exists(aidlPath)) {
//            throw new IllegalArgumentException("Cannot find aidl.exe at: " + aidlPath);
//        }
//
//        return aidlPath;
//    }
    public Path getAidlExecutable() {

        try {
            String resourcePath = "/tools/aidl/windows/aidl.exe";

            InputStream is = getClass().getResourceAsStream(resourcePath);

            if (is == null) {
                throw new IllegalArgumentException("aidl.exe not found in jar: " + resourcePath);
            }

            // ⭐ 创建临时文件
            Path tempFile = Files.createTempFile("aidl-", ".exe");

            // 写入
            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);

            // ⭐ Windows 可执行（保险起见）
            tempFile.toFile().setExecutable(true);

            return tempFile;

        } catch (IOException e) {
            throw new RuntimeException("Failed to extract aidl.exe", e);
        }
    }

    public Path getFrameworkAidl() {

        try {
            String resourcePath = "/tools/aidl/framework.aidl";

            InputStream is = getClass().getResourceAsStream(resourcePath);

            if (is == null) {
                throw new IllegalArgumentException("framework.aidl not found in jar");
            }

            Path tempFile = Files.createTempFile("framework-", ".aidl");

            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);

            return tempFile;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
