package RecursiveFileReader;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecursiveFileReader {

    /**
     * 递归读取指定目录下的所有文件
     *
     * @param directory 要读取的目录
     * @return 包含所有文件的列表
     */
    public static List<File> listAllFiles(File directory) {
        List<File> fileList = new ArrayList<>();
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            return fileList;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归处理子目录
                    fileList.addAll(listAllFiles(file));
                } else {
                    // 添加文件到列表
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }

    /**
     * 递归读取指定目录下的所有文件（带路径过滤）
     *
     * @param directory 要读取的目录
     * @param basePath  基础路径，用于计算相对路径
     * @param fileList  存储结果的列表
     */
    public static void listFilesRecursively(File directory, String basePath, List<String> fileList) {
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String relativePath = basePath.isEmpty()
                        ? file.getName()
                        : basePath + File.separator + file.getName();

                if (file.isDirectory()) {
                    // 递归处理子目录
                    listFilesRecursively(file, relativePath, fileList);
                } else {
                    // 添加相对路径到列表
                    fileList.add(relativePath);
                }
            }
        }
    }

    public static void main(String[] args) {
        // 示例1：获取所有文件对象
        File rootDir = new File("/path/to/your/directory");
        List<File> allFiles = listAllFiles(rootDir);
        System.out.println("发现 " + allFiles.size() + " 个文件");
        for (File file : allFiles) {
            System.out.println(file.getAbsolutePath());
        }

        // 示例2：获取所有文件的相对路径
        List<String> relativePaths = new ArrayList<>();
        listFilesRecursively(rootDir, "", relativePaths);
        System.out.println("\n相对路径列表：");
        for (String path : relativePaths) {
            System.out.println(path);
        }
    }
}

