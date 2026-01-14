package com.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DataFileUtil {
    // 外部数据目录（在程序运行目录下的data目录）
    private static final String EXTERNAL_DATA_DIR = "data";
    // 资源内的数据目录（在resources/data下）
    private static final String RESOURCE_DATA_DIR = "/data";

    /**
     * 初始化数据文件：如果外部数据文件不存在，则从资源中复制
     */
    public static void initDataFiles() throws IOException {
        // 确保外部数据目录存在
        Files.createDirectories(Paths.get(EXTERNAL_DATA_DIR));

        String[] dataFiles = {"users.txt", "scores.txt"};

        for (String fileName : dataFiles) {
            Path externalFile = Paths.get(EXTERNAL_DATA_DIR, fileName);

            // 如果外部文件不存在，则从资源中复制
            if (!Files.exists(externalFile)) {
                // 从资源路径读取
                String resourcePath = RESOURCE_DATA_DIR + "/" + fileName;
                InputStream inputStream = DataFileUtil.class.getResourceAsStream(resourcePath);

                if (inputStream != null) {
                    Files.copy(inputStream, externalFile, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    // 如果资源中没有，创建一个空文件
                    Files.createFile(externalFile);
                }
            }
        }
    }

    /**
     * 获取外部数据文件的路径（字符串形式）
     */
    public static String getFilePath(String fileName) {
        return Paths.get(EXTERNAL_DATA_DIR, fileName).toString();
    }
}
