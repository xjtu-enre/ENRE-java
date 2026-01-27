package aidl;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * AidlPreprocessor
 *
 * ENRE-JAVA AIDL 预处理入口：
 * - 扫描 .aidl
 * - 判断是否已有 Java
 * - 若无则调用 aidl 原地生成
 */
public class AidlPreprocessor {

    public void process(ProjectContext context) {

        AidlDiscoverer discoverer = new AidlDiscoverer();
        // 传入 sourceRoots 和项目根目录 context.getProjectRoot()
        AidlJavaChecker javaChecker = new AidlJavaChecker(context.getSourceRoots());

        AidlCompiler compiler = new AidlCompiler(context.getAidlExecutable().toFile());

        for (Path sourceRoot : context.getSourceRoots()) {

            List<Path> aidlFiles = discoverer.discover(sourceRoot);

            for (Path aidlFile : aidlFiles) {

                Optional<Path> existing =
                        javaChecker.findCorrespondingJava(aidlFile);

                if (existing.isPresent()) {
                    System.out.println("AidlPreprocessor: Found existing Java: " + existing.get());
                    continue;
                }

                try {
                    System.out.println("AidlPreprocessor: Compiling aidl -> java (in-place): " + aidlFile);
                    compiler.compile(aidlFile.toFile());
                } catch (Exception e) {
                    throw new RuntimeException(
                            "Failed to compile aidl: " + aidlFile, e);
                }
            }
        }
    }
}
