package io.github.cloudtechnology.codeaegis.utility;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import groovy.util.logging.Slf4j;

/**
 * FileContentReader 用於讀取檔案內容，預設使用 UTF-8 編碼。
 */
@Slf4j
public class FileContentReader {

    /**
     * 讀取指定檔案的內容，並返回作為字符串。
     *
     * @param filePath 檔案的路徑
     * @return 檔案的內容作為字符串
     * @throws IOException 如果在讀取檔案時發生 IO 錯誤
     */
    public static String readFileContent(Path filePath) throws IOException {
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }

    /**
     * 讀取指定檔案的內容，並返回作為字符串列表，每行一個元素。
     *
     * @param filePath 檔案的路徑
     * @return 檔案的內容作為字符串列表
     * @throws IOException 如果在讀取檔案時發生 IO 錯誤
     */
    public static List<String> readFileLines(Path filePath) throws IOException {
        return Files.readAllLines(filePath, StandardCharsets.UTF_8);
    }
}
