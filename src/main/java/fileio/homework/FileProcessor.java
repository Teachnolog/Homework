package fileio.homework;

import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.*;

public class FileProcessor {

  public List<Path> splitFile(String sourcePath, String outputDir, int partSize) throws IOException {
    List<Path> partPaths = new ArrayList<>();
    Path sourceFile = Paths.get(sourcePath);

    if (!Files.exists(sourceFile)) {
      throw new FileNotFoundException("Source file not found: " + sourcePath);
    }

    Path outputDirectory = Paths.get(outputDir);
    Files.createDirectories(outputDirectory);

    String fileName = sourceFile.getFileName().toString();
    String baseName = fileName.contains(".") ?
        fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
    String extension = fileName.contains(".") ?
        fileName.substring(fileName.lastIndexOf('.')) : "";

    try (FileChannel sourceChannel = FileChannel.open(sourceFile, StandardOpenOption.READ)) {
      long fileSize = sourceChannel.size();
      long totalParts = (fileSize + partSize - 1) / partSize;

      ByteBuffer buffer = ByteBuffer.allocate(partSize);

      for (int partNumber = 1; partNumber <= totalParts; partNumber++) {
        String partFileName = String.format("%s.part%d%s", baseName, partNumber, extension);
        Path partPath = outputDirectory.resolve(partFileName);

        try (FileChannel partChannel = FileChannel.open(partPath,
            StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

          buffer.clear();
          int bytesRead = sourceChannel.read(buffer);

          if (bytesRead > 0) {
            buffer.flip();
            partChannel.write(buffer);
            partPaths.add(partPath);
          }
        }
      }
    }

    return partPaths;
  }

  public void mergeFiles(List<Path> partPaths, String outputPath) throws IOException {
    if (partPaths == null || partPaths.isEmpty()) {
      throw new IllegalArgumentException("Part paths list cannot be empty");
    }

    Path outputFile = Paths.get(outputPath);

    try (FileChannel outputChannel = FileChannel.open(outputFile,
        StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

      ByteBuffer buffer = ByteBuffer.allocate(8192);

      for (Path partPath : partPaths) {
        if (!Files.exists(partPath)) {
          throw new FileNotFoundException("Part file not found: " + partPath);
        }

        try (FileChannel partChannel = FileChannel.open(partPath, StandardOpenOption.READ)) {
          buffer.clear();

          while (partChannel.read(buffer) > 0) {
            buffer.flip();
            outputChannel.write(buffer);
            buffer.compact();
          }
        }
      }
    }
  }

  public void mergeFilesEfficient(List<Path> partPaths, String outputPath) throws IOException {
    if (partPaths == null || partPaths.isEmpty()) {
      throw new IllegalArgumentException("Part paths list cannot be empty");
    }

    Path outputFile = Paths.get(outputPath);

    try (FileChannel outputChannel = FileChannel.open(outputFile,
        StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

      for (Path partPath : partPaths) {
        if (!Files.exists(partPath)) {
          throw new FileNotFoundException("Part file not found: " + partPath);
        }

        try (FileChannel partChannel = FileChannel.open(partPath, StandardOpenOption.READ)) {
          long position = outputChannel.position();
          long transferred = partChannel.transferTo(0, partChannel.size(), outputChannel);

          if (transferred != partChannel.size()) {
            throw new IOException("Failed to transfer complete file: " + partPath);
          }
        }
      }
    }
  }

  public static void main(String[] args) {
    FileProcessor processor = new FileProcessor();

    try {
      Path testFile = Paths.get("large_test_file.dat");
      byte[] testData = new byte[2500];
      new Random().nextBytes(testData);
      Files.write(testFile, testData);

      System.out.println("Исходный файл создан: " + testFile.toAbsolutePath());
      System.out.println("Размер: " + Files.size(testFile) + " байт");

      String outputDir = "file_parts";
      List<Path> parts = processor.splitFile(testFile.toString(), outputDir, 1000);

      System.out.println("\nСоздано частей: " + parts.size());
      for (Path part : parts) {
        System.out.println(" - " + part.getFileName() + " (" + Files.size(part) + " байт)");
      }

      Path mergedFile = Paths.get("merged_file.dat");
      processor.mergeFiles(parts, mergedFile.toString());

      System.out.println("\nФайл объединен: " + mergedFile.toAbsolutePath());
      System.out.println("Размер объединенного файла: " + Files.size(mergedFile) + " байт");

      byte[] original = Files.readAllBytes(testFile);
      byte[] merged = Files.readAllBytes(mergedFile);

      if (Arrays.equals(original, merged)) {
        System.out.println("✓ Файлы идентичны!");
      } else {
        System.out.println("✗ Файлы различаются!");
      }

    } catch (IOException e) {
      System.err.println("Ошибка: " + e.getMessage());
      e.printStackTrace();
    }
  }
}