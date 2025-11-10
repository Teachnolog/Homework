package fileio.homework;

import java.io.*;
import java.util.*;

public class TextFileAnalyzer {

  public static class AnalysisResult {
    private final long lineCount;
    private final long wordCount;
    private final long charCount;
    private final Map<Character, Integer> charFrequency;

    public AnalysisResult(long lineCount, long wordCount, long charCount,
        Map<Character, Integer> charFrequency) {
      this.lineCount = lineCount;
      this.wordCount = wordCount;
      this.charCount = charCount;
      this.charFrequency = new HashMap<>(charFrequency);
    }

    public long getLineCount() { return lineCount; }
    public long getWordCount() { return wordCount; }
    public long getCharCount() { return charCount; }
    public Map<Character, Integer> getCharFrequency() { return new HashMap<>(charFrequency); }

    @Override
    public String toString() {
      return "AnalysisResult{" +
          "lineCount=" + lineCount +
          ", wordCount=" + wordCount +
          ", charCount=" + charCount +
          ", charFrequency=" + charFrequency +
          '}';
    }
  }

  public AnalysisResult analyzeFile(String filePath) throws IOException {
    long lineCount = 0;
    long wordCount = 0;
    long charCount = 0;
    Map<Character, Integer> charFrequency = new HashMap<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lineCount++;
        charCount += line.length() + 1;

        String[] words = line.split("\\s+");
        wordCount += Arrays.stream(words)
            .filter(word -> !word.isEmpty())
            .count();

        for (char c : line.toCharArray()) {
          charFrequency.put(c, charFrequency.getOrDefault(c, 0) + 1);
        }
      }
      if (charCount > 0) charCount--;
    }

    return new AnalysisResult(lineCount, wordCount, charCount, charFrequency);
  }

  public void saveAnalysisResult(AnalysisResult result, String outputPath) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
      writer.write("=== АНАЛИЗ ТЕКСТОВОГО ФАЙЛА ===");
      writer.newLine();
      writer.write("Количество строк: " + result.getLineCount());
      writer.newLine();
      writer.write("Количество слов: " + result.getWordCount());
      writer.newLine();
      writer.write("Количество символов: " + result.getCharCount());
      writer.newLine();
      writer.newLine();
      writer.write("=== ЧАСТОТА СИМВОЛОВ ===");
      writer.newLine();

      result.getCharFrequency().entrySet().stream()
          .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
          .forEach(entry -> {
            try {
              char c = entry.getKey();
              String charDisplay = (c == ' ') ? "[ПРОБЕЛ]" :
                  (c == '\t') ? "[TAB]" :
                      String.valueOf(c);
              writer.write("'" + charDisplay + "': " + entry.getValue());
              writer.newLine();
            } catch (IOException e) {
              throw new UncheckedIOException(e);
            }
          });

      writer.newLine();
      writer.write("=== СТАТИСТИКА ===");
      writer.newLine();
      writer.write("Средняя длина строки: " +
          (result.getLineCount() > 0 ?
              String.format("%.2f", (double) result.getCharCount() / result.getLineCount()) : "0"));
      writer.newLine();
      writer.write("Средняя длина слова: " +
          (result.getWordCount() > 0 ?
              String.format("%.2f", (double) result.getCharCount() / result.getWordCount()) : "0"));
    }
  }

  public static void main(String[] args) {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    try {
      String testFilePath = "test_input.txt";
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath))) {
        writer.write("Hello world!");
        writer.newLine();
        writer.write("This is a test file for analysis.");
        writer.newLine();
        writer.write("Java IO is powerful!");
      }

      AnalysisResult result = analyzer.analyzeFile(testFilePath);
      System.out.println("Результат анализа:");
      System.out.println(result);

      String outputPath = "analysis_result.txt";
      analyzer.saveAnalysisResult(result, outputPath);
      System.out.println("\nРезультаты сохранены в: " + outputPath);

    } catch (IOException e) {
      System.err.println("Ошибка: " + e.getMessage());
    }
  }
}