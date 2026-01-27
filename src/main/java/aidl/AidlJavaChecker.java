package aidl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * AidlJavaChecker
 *
 * <p>
 * 判定一个 .aidl 文件是否已经存在对应的 Java 产物。
 *
 * 支持三类来源：
 * <ol>
 *   <li>同目录下的 .java（原地生成 / 手写）</li>
 *   <li>Java 源码目录（src/main/java 等）</li>
 *   <li>Android Gradle 生成目录
 *       (build/generated/aidl_source_output_dir/../out)</li>
        * </ol>
        * </p>
        */
public class AidlJavaChecker {

    private static final Pattern PACKAGE_PATTERN =
            Pattern.compile("^\\s*package\\s+([\\w.]+);");

    private final List<Path> sourceRoots;

    public AidlJavaChecker(List<Path> sourceRoots) {
        this.sourceRoots = sourceRoots;
    }

    /**
     * 查找是否已有对应的 Java 文件
     */
    public Optional<Path> findCorrespondingJava(Path aidlFile) {

        // 1️⃣ 同目录检查
        Path localJava = getLocalJavaPath(aidlFile);
        if (Files.exists(localJava)) {
            return Optional.of(localJava);
        }

        // 2️⃣ Java source roots
        Optional<Path> inSource = findInSourceRoots(aidlFile);
        if (inSource.isPresent()) {
            return inSource;
        }

        // 3️⃣ Android Gradle build/generated
        return findInGradleGenerated(aidlFile);
    }

    /* ======================== strategies ======================== */

    /**
     * aidl 同目录 -> java
     */
    private Path getLocalJavaPath(Path aidlFile) {
        String name = aidlFile.getFileName().toString();
        String javaName = name.substring(0, name.lastIndexOf('.')) + ".java";
        return aidlFile.getParent().resolve(javaName);
    }

    /**
     * 在 src/main/java 等源码根目录中查找
     */
    private Optional<Path> findInSourceRoots(Path aidlFile) {
        try {
            String relativeJavaPath = getRelativeJavaPath(aidlFile);
            if (relativeJavaPath == null) return Optional.empty();

            for (Path root : sourceRoots) {
                Path candidate = root.resolve(relativeJavaPath);
                if (Files.exists(candidate)) {
                    return Optional.of(candidate);
                }
            }
        } catch (IOException ignored) {
        }
        return Optional.empty();
    }

    /**
     * 在 Android Gradle 生成目录中查找：
     * build/generated/aidl_source_output_dir/../out/...
            */
    private Optional<Path> findInGradleGenerated(Path aidlFile) {
        try {
            String relativeJavaPath = getRelativeJavaPath(aidlFile);
            if (relativeJavaPath == null) return Optional.empty();

            System.out.println("Looking for: " + relativeJavaPath);

            for (Path projectRoot : sourceRoots) {

                System.out.println("Scan project root: " + projectRoot);

                if (!Files.isDirectory(projectRoot)) continue;

                try (Stream<Path> walk = Files.walk(projectRoot)) {

                    Optional<Path> found = walk
                            .filter(p -> p.toString().contains("aidl_source_output_dir"))
                            .filter(p -> p.toString().endsWith(relativeJavaPath))
                            .findFirst();

                    if (found.isPresent()) {
                        System.out.println("FOUND AIDL JAVA: " + found.get());
                        return found;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /* ======================== helpers ======================== */

    /**
     * 计算 package + class 的相对路径
     * com.example.Foo → com/example/Foo.java
     */
    private String getRelativeJavaPath(Path aidlFile) throws IOException {
        String pkg = extractPackage(aidlFile);
        if (pkg == null) return null;

        String cls =
                aidlFile.getFileName().toString().replace(".aidl", ".java");

        return pkg.replace('.', '/') + "/" + cls;
    }

    /**
     * 解析 aidl 文件中的 package 声明
     */
    private String extractPackage(Path aidlFile) throws IOException {
        try (Stream<String> lines = Files.lines(aidlFile)) {
            return lines
                    .map(PACKAGE_PATTERN::matcher)
                    .filter(Matcher::find)
                    .map(m -> m.group(1))
                    .findFirst()
                    .orElse(null);
        }
    }
}
