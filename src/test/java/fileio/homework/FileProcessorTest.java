package fileio.homework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessorTest {

  @Test
  void testSplitAndMergeFile(@TempDir Path tempDir) throws IOException {
    FileProcessor processor = new FileProcessor();

    Path testFile = tempDir.resolve("test.dat");
    byte[] testData = new byte[1500];
    new Random().nextBytes(testData);
    Files.write(testFile, testData);

    Path outputDir = tempDir.resolve("parts");
    List<Path> parts = processor.splitFile(testFile.toString(), outputDir.toString(), 500);

    assertEquals(3, parts.size());

    assertEquals(500, Files.size(parts.get(0)));
    assertEquals(500, Files.size(parts.get(1)));
    assertEquals(500, Files.size(parts.get(2)));

    assertTrue(Files.exists(parts.get(0)));
    assertTrue(Files.exists(parts.get(1)));
    assertTrue(Files.exists(parts.get(2)));

    Path mergedFile = tempDir.resolve("merged.dat");
    processor.mergeFiles(parts, mergedFile.toString());

    assertArrayEquals(Files.readAllBytes(testFile), Files.readAllBytes(mergedFile));
  }

  @Test
  void testSplitFileExactSize(@TempDir Path tempDir) throws IOException {
    FileProcessor processor = new FileProcessor();

    Path testFile = tempDir.resolve("exact.dat");
    byte[] testData = new byte[1000];
    new Random().nextBytes(testData);
    Files.write(testFile, testData);

    List<Path> parts = processor.splitFile(testFile.toString(), tempDir.toString(), 500);

    assertEquals(2, parts.size());
    assertEquals(500, Files.size(parts.get(0)));
    assertEquals(500, Files.size(parts.get(1)));
  }

  @Test
  void testSplitFileSmallLastPart(@TempDir Path tempDir) throws IOException {
    FileProcessor processor = new FileProcessor();

    Path testFile = tempDir.resolve("small_last.dat");
    byte[] testData = new byte[1200];
    new Random().nextBytes(testData);
    Files.write(testFile, testData);

    List<Path> parts = processor.splitFile(testFile.toString(), tempDir.toString(), 500);

    assertEquals(3, parts.size());
    assertEquals(500, Files.size(parts.get(0)));
    assertEquals(500, Files.size(parts.get(1)));
    assertEquals(200, Files.size(parts.get(2)));
  }

  @Test
  void testSplitEmptyFile(@TempDir Path tempDir) throws IOException {
    FileProcessor processor = new FileProcessor();

    Path emptyFile = tempDir.resolve("empty.dat");
    Files.createFile(emptyFile);

    List<Path> parts = processor.splitFile(emptyFile.toString(), tempDir.toString(), 500);

    assertEquals(0, parts.size());
  }

  @Test
  void testMergeFilesEfficient(@TempDir Path tempDir) throws IOException {
    FileProcessor processor = new FileProcessor();

    Path testFile = tempDir.resolve("test_eff.dat");
    byte[] testData = new byte[2000];
    new Random().nextBytes(testData);
    Files.write(testFile, testData);

    Path outputDir = tempDir.resolve("parts_eff");
    List<Path> parts = processor.splitFile(testFile.toString(), outputDir.toString(), 700);

    Path mergedFile = tempDir.resolve("merged_eff.dat");
    processor.mergeFilesEfficient(parts, mergedFile.toString());

    assertArrayEquals(Files.readAllBytes(testFile), Files.readAllBytes(mergedFile));
  }

  @Test
  void testMergeWithMissingPart(@TempDir Path tempDir) throws IOException {
    FileProcessor processor = new FileProcessor();

    Path testFile = tempDir.resolve("test_missing.dat");
    Files.write(testFile, new byte[1000]);

    List<Path> parts = processor.splitFile(testFile.toString(), tempDir.toString(), 500);

    Files.delete(parts.get(1));

    Path mergedFile = tempDir.resolve("merged_missing.dat");

    assertThrows(IOException.class, () -> {
      processor.mergeFiles(parts, mergedFile.toString());
    });
  }
}