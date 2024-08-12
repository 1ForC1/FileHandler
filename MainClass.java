import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainClass {

    public static void main(String[] args) throws InterruptedException, IOException {

        System.out.println("Выберите файл для сортировки по возрастанию...");
        Thread.sleep(1000);

        // Получение абсолютного пути
        JFileChooser j = new JFileChooser(Paths.get("").toAbsolutePath().toString());

        // фильтр на .txt файлы
        j.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt", "text"));
        j.setAcceptAllFileFilterUsed(false);

        // Открытие файлового диалога
        int select = j.showOpenDialog(new JFrame());

        if (select == JFileChooser.APPROVE_OPTION) {
            // Создание директории для хранения временных файлов
            File directory = new File(Paths.get("").toAbsolutePath().toString() + "/directory");
            if (!directory.exists()) {
                directory.mkdir();
                System.out.println("Директория создана");
            } else {
                System.out.println("Директория уже существует");
            }

            // Сортировка
            externalSort(j.getSelectedFile(),
                    new File(Paths.get("").toAbsolutePath().toString() + "/sorted_" + j.getSelectedFile().getName()),
                    3);

            // Удаление директории и временных файлов
            if (directory.exists()) {
                String[] files = directory.list();
                for (String file : files) {
                    File currentFile = new File(directory.getPath(), file);
                    currentFile.delete();
                }
                directory.delete();
                System.out.println("Директория удалена, временные файлы удалены");
            } else {
                System.out.println("Директория не существует");
            }
        } else {
            System.out.println("Файл не был выбран!");
        }
        
        System.exit(0);
    }

    public static void externalSort(File inputFile, File outputFile, int chunkSize) throws IOException {
        ArrayList<Integer> chunk = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        String line;
        int chunkIndex = 0;

        // Анализ исходного файла
        while ((line = br.readLine()) != null) {
            try {
                int number = Integer.parseInt(line);
                chunk.add(number);
                if (chunk.size() == chunkSize) {
                    Collections.sort(chunk);
                    writeChunkToFile(chunk, chunkIndex);
                    chunkIndex++;
                    chunk.clear();
                }
            } catch (Exception e) {
            }
        }

        if (!chunk.isEmpty()) {
            Collections.sort(chunk);
            writeChunkToFile(chunk, chunkIndex);
        }

        br.close();
        mergeChunks(outputFile, chunkIndex);
    }

    // Метод записи данных исходного файла во временные файлы
    private static void writeChunkToFile(ArrayList<Integer> chunk, int chunkIndex) throws IOException {
        File chunkFile = new File(
                Paths.get("").toAbsolutePath().toString() + "/directory/" + "chunk" + chunkIndex + ".txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(chunkFile));

        for (int number : chunk) {
            bw.write(String.valueOf(number));
            bw.newLine();
        }
        bw.close();
    }

    // Метод соединения временных файлов и сортировка их данных в конечный файл
    private static void mergeChunks(File outputFile, int chunkIndex) throws IOException {
        ArrayList<BufferedReader> readers = new ArrayList<>();

        for (int i = 0; i <= chunkIndex; i++) {
            readers.add(new BufferedReader(
                    new FileReader(Paths.get("").toAbsolutePath().toString() + "/directory/" + "chunk" + i + ".txt")));
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        ArrayList<Integer> numbers = new ArrayList<>(readers.size());

        // Сортировка данных пеерд записью в конечный файл
        while (!readers.isEmpty() || !numbers.isEmpty()) {
            for (int i = 0; i < readers.size(); i++) {
                String line = readers.get(i).readLine();
                if (line == null) {
                    readers.get(i).close();
                    readers.remove(i);
                } else {
                    int number = Integer.parseInt(line);
                    numbers.add(number);
                }
            }
            // Запись в конечный файл
            if (!numbers.isEmpty()) {
                Collections.sort(numbers);
                writer.write(String.valueOf(numbers.get(0)));
                writer.newLine();
                numbers.remove(0);
            }
        }

        writer.close();
    }
}