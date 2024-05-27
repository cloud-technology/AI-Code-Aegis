package io.github.cloudtechnology.codeaegis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.github.cloudtechnology.codeaegis.utility.FileContentReader;

/**
 * 測試 FileContentReader 類的功能，包括讀取檔案內容和讀取檔案行內容。
 */
public class FileContentReaderTests {

    @TempDir
    Path tempDir;

    private Path testFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = tempDir.resolve("testFile.txt");
        Files.writeString(testFile, "Hello, World!\nThis is a test file.\n");
    }

    @Test
    @DisplayName("讀取檔案內容")
    void testReadFileContent() throws IOException {
        String content = FileContentReader.readFileContent(testFile);

        assertNotNull(content, "File content should not be null");
        assertEquals("Hello, World!\nThis is a test file.\n", content, "File content should match expected value");
    }

    @Test
    @DisplayName("讀取檔案行內容")
    void testReadFileLines() throws IOException {
        List<String> lines = FileContentReader.readFileLines(testFile);

        assertNotNull(lines, "File lines should not be null");
        assertEquals(2, lines.size(), "File should have 2 lines");
        assertEquals("Hello, World!", lines.get(0), "First line should match expected value");
        assertEquals("This is a test file.", lines.get(1), "Second line should match expected value");
    }
}