package RecursiveFileReader;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecursiveFileReader {

    /**
     * �ݹ��ȡָ��Ŀ¼�µ������ļ�
     *
     * @param directory Ҫ��ȡ��Ŀ¼
     * @return ���������ļ����б�
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
                    // �ݹ鴦����Ŀ¼
                    fileList.addAll(listAllFiles(file));
                } else {
                    // ����ļ����б�
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }

    /**
     * �ݹ��ȡָ��Ŀ¼�µ������ļ�����·�����ˣ�
     *
     * @param directory Ҫ��ȡ��Ŀ¼
     * @param basePath  ����·�������ڼ������·��
     * @param fileList  �洢������б�
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
                    // �ݹ鴦����Ŀ¼
                    listFilesRecursively(file, relativePath, fileList);
                } else {
                    // ������·�����б�
                    fileList.add(relativePath);
                }
            }
        }
    }

    public static void main(String[] args) {
        // ʾ��1����ȡ�����ļ�����
        File rootDir = new File("/path/to/your/directory");
        List<File> allFiles = listAllFiles(rootDir);
        System.out.println("���� " + allFiles.size() + " ���ļ�");
        for (File file : allFiles) {
            System.out.println(file.getAbsolutePath());
        }

        // ʾ��2����ȡ�����ļ������·��
        List<String> relativePaths = new ArrayList<>();
        listFilesRecursively(rootDir, "", relativePaths);
        System.out.println("\n���·���б�");
        for (String path : relativePaths) {
            System.out.println(path);
        }
    }
}

