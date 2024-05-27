package io.github.cloudtechnology.codeaegis.utility;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * FileInfo 用於存儲檔案或目錄的基本資訊。
 */
@Data
@AllArgsConstructor
public class FileInfo {
    String name;
    String relativePath;
    String absolutePath;
    boolean isDirectory;
    String content;

    @Override
    public String toString() {
        return "FileInfo{" +
                "name='" + name + '\'' +
                ", relativePath='" + relativePath + '\'' +
                ", absolutePath='" + absolutePath + '\'' +
                ", isDirectory=" + isDirectory + '\'' +
                ", content=" + content +
                '}';
    }
}
