package fileio.homework;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TextFileAnalyzerTest {

  @Test
  void testAnalyzeFile(@TempDir Path tempDir) throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    Path testFile = tempDir.resolve("test.txt");
    List<String> lines = Arrays.asList("Hello world!", "This is test.", "Java IO");
    Files.write(testFile, lines);

    TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(testFile.toString());

    assertEquals(3, result.getLineCount());
    assertEquals(7, result.getWordCount());
    assertTrue(result.getCharCount() > 30);
    assertTrue(result.getCharFrequency().get('l') >= 3);
    assertTrue(result.getCharFrequency().containsKey(' '));
  }

  @Test
  void testAnalyzeEmptyFile(@TempDir Path tempDir) throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    Path emptyFile = tempDir.resolve("empty.txt");
    Files.createFile(emptyFile);

    TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(emptyFile.toString());

    assertEquals(0, result.getLineCount());
    assertEquals(0, result.getWordCount());
    assertEquals(0, result.getCharCount());
    assertTrue(result.getCharFrequency().isEmpty());
  }

  @Test
  void testSaveAnalysisResult(@TempDir Path tempDir) throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    Map<Character, Integer> frequency = new HashMap<>();
    frequency.put('a', 5);
    frequency.put('b', 3);
    TextFileAnalyzer.AnalysisResult result =
        new TextFileAnalyzer.AnalysisResult(2, 5, 20, frequency);

    Path outputFile = tempDir.resolve("analysis_output.txt");
    analyzer.saveAnalysisResult(result, outputFile.toString());

    assertTrue(Files.exists(outputFile));
    assertTrue(Files.size(outputFile) > 0);

    String content = new String(Files.readAllBytes(outputFile), StandardCharsets.UTF_8);
    assertTrue(content.contains("Количество строк: 2"));
    assertTrue(content.contains("Количество слов: 5"));
    assertTrue(content.contains("Количество символов: 20"));
    assertTrue(content.contains("'a': 5"));
    assertTrue(content.contains("'b': 3"));
  }
}