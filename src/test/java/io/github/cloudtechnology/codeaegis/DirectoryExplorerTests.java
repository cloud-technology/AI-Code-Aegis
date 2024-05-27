package io.github.cloudtechnology.codeaegis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.github.cloudtechnology.codeaegis.utility.DirectoryExplorer;
import io.github.cloudtechnology.codeaegis.utility.FileInfo;

/**
 * 測試 DirectoryExplorer 類的功能，包括列出目錄中的所有檔案和目錄資訊，
 * 以及排除特定資料夾的功能。
 */
public class DirectoryExplorerTests {

        /**
         * 使用 JUnit 5 的 @TempDir 注解創建一個臨時目錄。
         * 這個目錄會在每個測試方法運行之前創建，並在測試運行之後刪除。
         */
        @TempDir
        Path tempDir;
        private DirectoryExplorer directoryExplorer;

        /**
         * 在每個測試之前設置測試環境，創建測試目錄和文件。
         *
         * @throws IOException 如果在創建測試目錄或文件時發生 IO 錯誤
         */
        @BeforeEach
        void setUp() throws IOException {
                directoryExplorer = new DirectoryExplorer();
                directoryExplorer.setExcludedDirectories(Set.of("excludedDir"));
                // 建立測試目錄結構
                Files.createDirectories(tempDir.resolve("subDir1"));
                Files.createDirectories(tempDir.resolve("excludedDir"));
                Files.createFile(tempDir.resolve("file1.txt"));
                Files.createFile(tempDir.resolve("subDir1/file2.txt"));
        }

        /**
         * 測試 DirectoryExplorer 能夠正確列出目錄及其子目錄中的所有檔案和目錄。
         *
         * @throws IOException 如果在處理目錄或文件時發生 IO 錯誤
         */
        @Test
        @DisplayName("列出所有檔案和目錄")
        void testListAllFilesAndDirectories() throws IOException {
                File rootDir = tempDir.toFile();
                List<FileInfo> result = directoryExplorer.listAllFilesAndDirectories(rootDir);
                assertNotNull(result, "Result should not be null");
                assertEquals(3, result.size(), "Result size should be 3");
                FileInfo fileInfo1 = result.stream().filter(f -> f.getName().equals("subDir1")).findFirst()
                                .orElse(null);
                assertNotNull(fileInfo1, "subDir1 should be listed");
                assertEquals("subDir1", fileInfo1.getRelativePath(), "Relative path of subDir1 should be 'subDir1'");
                assertEquals(new File(rootDir, "subDir1").getCanonicalPath(), fileInfo1.getAbsolutePath(),
                                "Absolute path of subDir1 should match");
                assertTrue(fileInfo1.isDirectory(), "subDir1 should be a directory");
                FileInfo fileInfo2 = result.stream().filter(f -> f.getName().equals("file1.txt")).findFirst()
                                .orElse(null);
                assertNotNull(fileInfo2, "file1.txt should be listed");
                assertEquals("file1.txt", fileInfo2.getRelativePath(),
                                "Relative path of file1.txt should be 'file1.txt'");
                assertEquals(new File(rootDir, "file1.txt").getCanonicalPath(), fileInfo2.getAbsolutePath(),
                                "Absolute path of file1.txt should match");
                assertFalse(fileInfo2.isDirectory(), "file1.txt should not be a directory");
                FileInfo fileInfo3 = result.stream().filter(f -> f.getName().equals("file2.txt")).findFirst()
                                .orElse(null);
                assertNotNull(fileInfo3, "file2.txt should be listed");
                assertEquals("subDir1/file2.txt", fileInfo3.getRelativePath(),
                                "Relative path of file2.txt should be 'subDir1/file2.txt'");
                assertEquals(new File(rootDir, "subDir1/file2.txt").getCanonicalPath(), fileInfo3.getAbsolutePath(),
                                "Absolute path of file2.txt should match");
                assertFalse(fileInfo3.isDirectory(), "file2.txt should not be a directory");
        }

        /**
         * 測試 DirectoryExplorer 能夠正確排除指定的資料夾。
         *
         * @throws IOException 如果在處理目錄或文件時發生 IO 錯誤
         */
        @Test
        @DisplayName("排除指定資料夾")
        void testExcludedDirectories() throws IOException {
                File rootDir = tempDir.toFile();
                List<FileInfo> result = directoryExplorer.listAllFilesAndDirectories(rootDir);
                assertNotNull(result, "Result should not be null");
                assertEquals(3, result.size(), "Result size should be 3");
                assertTrue(result.stream().noneMatch(fileInfo -> fileInfo.getName().equals("excludedDir")),
                                "excludedDir should not be listed");
        }
}