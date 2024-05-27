package io.github.cloudtechnology.codeaegis.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

/**
 * DirectoryExplorer 用於列出目錄中的所有檔案和目錄資訊。
 * 可以自訂需要排除的資料夾名稱。
 */
@Slf4j
public class DirectoryExplorer {
    private Set<String> excludedDirectories = new HashSet<>();
    private Set<String> excludedFiles = new HashSet<>();

    /**
     * 設定需要排除的資料夾名稱。
     *
     * @param excludedDirectories 需要排除的資料夾名稱集合
     */
    public void setExcludedDirectories(Set<String> excludedDirectories) {
        this.excludedDirectories = excludedDirectories;
    }

    /**
     * 設定需要排除的檔案名稱。
     *
     * @param excludedFiles 需要排除的檔案名稱集合
     */
    public void setExcludedFiles(Set<String> excludedFiles) {
        this.excludedFiles = excludedFiles;
    }

    /**
     * 列出當前目錄及其子目錄的所有檔案和目錄資訊。
     * 
     * @return 包含所有檔案和目錄資訊的清單
     */
    public List<FileInfo> listAllFilesAndDirectories() {
        return listAllFilesAndDirectories(new File("."));
    }

    /**
     * 列出指定目錄及其子目錄的所有檔案和目錄資訊。
     *
     * @param dir 要列出的根目錄
     * @return 包含所有檔案和目錄資訊的清單
     */
    public List<FileInfo> listAllFilesAndDirectories(File dir) {
        List<FileInfo> fileInfoList = new ArrayList<>();
        this.exploreDirectory(dir, fileInfoList, dir.toPath());
        return fileInfoList;
    }

    /**
     * 遞迴遍歷目錄，並將檔案和目錄資訊添加到清單中。
     *
     * @param dir          當前目錄
     * @param fileInfoList 用於存儲檔案和目錄資訊的清單
     * @param rootPath     根目錄的絕對路徑
     */
    private void exploreDirectory(File dir, List<FileInfo> fileInfoList, Path rootPath) {
        if (dir.isDirectory()) {
            File[] filesAndDirs = dir.listFiles();
            // 確認目錄不是空的
            if (filesAndDirs != null) {
                for (File file : filesAndDirs) {
                    if (file.isDirectory() && excludedDirectories.contains(file.getName())) {
                        continue; // 跳過被排除的目錄
                    }
                    if (file.isFile() && excludedFiles.contains(file.getName())) {
                        continue; // 跳過被排除的檔案
                    }
                    try {
                        String absolutePath = file.getCanonicalPath();
                        String relativePath = rootPath.relativize(file.toPath()).toString();
                        FileInfo fileInfo = new FileInfo(file.getName(), relativePath, absolutePath,
                                file.isDirectory(), null);
                        fileInfoList.add(fileInfo);
                        if (file.isDirectory()) {
                            exploreDirectory(file, fileInfoList, rootPath);
                        }
                    } catch (IOException e) {
                        log.error("file={}", file, e);
                    }
                }
            }
        }
    }
}
