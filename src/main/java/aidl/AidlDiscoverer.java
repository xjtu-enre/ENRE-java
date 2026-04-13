package aidl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * AidlDiscoverer
 * 负责在指定的源码根目录中递归扫描所有 AIDL 文件（*.aidl）
 */
public class AidlDiscoverer {

    /**
     * 在给定的源码根目录下递归查找所有 .aidl 文件。
     *
     * @param sourceRoot 源码根目录
     * @return 所有发现的 .aidl 文件路径列表
     * @throws RuntimeException 当文件遍历过程中发生 IO 异常时抛出
     */
    public List<Path> discover(Path sourceRoot) {
        List<Path> result = new ArrayList<>();

        // 使用 Files.walk 递归遍历目录树
        try (Stream<Path> paths = Files.walk(sourceRoot)) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".aidl"))
                    .forEach(result::add);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to discover AIDL files under: " + sourceRoot, e);
        }

        System.out.println("AidlDiscoverer: " + result.size());
        return result;
    }
}
