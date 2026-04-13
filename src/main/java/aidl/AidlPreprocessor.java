package aidl;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * AidlPreprocessor
 *
 * ENRE-JAVA 的 AIDL 预处理入口：
 *  1. 扫描所有 .aidl
 *  2. 判断是否已有对应 Java
 *  3. 若无 → 使用 aidl.exe 编译
 */
public class AidlPreprocessor {

    public void process(ProjectContext context) {
        List<File> includeDirs = new ArrayList<>();

        // 1️⃣ 扫描器
        AidlDiscoverer discoverer = new AidlDiscoverer();

        // 2️⃣ Java 存在性检查（源码 / AOSP / Gradle）
        AidlJavaChecker javaChecker = new AidlJavaChecker(context.getSourceRoots());

        // 3️⃣ aidl.exe 编译器（原地生成）
        // 项目源码路径
        for (Path root : context.getSourceRoots()) {
            includeDirs.add(root.toFile());
        }

        // Android framework 路径（需要你从 context 提供）
//        includeDirs.add(context.getAndroidFrameworkDir().toFile());

        // 可选：额外依赖路径
        includeDirs.addAll(context.getExtraAidlIncludes(context));

        AidlCompiler compiler =
                new AidlCompiler(context.getAidlExecutable().toFile(), includeDirs);

        // 4️⃣ 遍历所有源码根
        for (Path sourceRoot : context.getSourceRoots()) {

            List<Path> aidlFiles = discoverer.discover(sourceRoot);

            for (Path aidlFile : aidlFiles) {

                // 4.1 判断是否已有 Java
                Optional<Path> existing =
                        javaChecker.findCorrespondingJava(aidlFile);

                if (existing.isPresent()) {
                    System.out.println(
                            "AidlPreprocessor: Found Java: " + existing.get());
                    continue;
                }

                // 4.2 没有 Java → 编译
                System.out.println(
                        "AidlPreprocessor: Compile AIDL: " + aidlFile);

                try {
                    compiler.compile(aidlFile.toFile());
                } catch (Exception e) {
                    throw new RuntimeException(
                            "Failed to process AIDL: " + aidlFile, e);
                }
            }
        }
    }
}