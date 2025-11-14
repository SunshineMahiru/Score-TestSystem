package com.example.controller;

import com.example.model.Score;
import com.example.util.DataFileUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    // 使用DataFileUtil获取文件路径
    private static final String SCORE_FILE = DataFileUtil.getFilePath("scores.txt");

    @FXML
    private TableView<Score> scoreTable;
    @FXML
    private TableColumn<Score, String> studentIdColumn;
    @FXML
    private TableColumn<Score, String> studentNameColumn;
    @FXML
    private TableColumn<Score, String> courseColumn;
    @FXML
    private TableColumn<Score, Double> scoreColumn;

    private ObservableList<Score> scoreData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // 设置表格列与Score属性的映射
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        // 加载数据
        loadScoreData();
    }

    // 加载成绩数据
    private void loadScoreData() {
        try {
            Path filePath = Paths.get(SCORE_FILE);
            if (Files.exists(filePath)) {
                List<String> lines = Files.readAllLines(filePath);
                scoreData.clear();

                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        Score score = new Score(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
                        scoreData.add(score);
                    }
                }

                scoreTable.setItems(scoreData);
            }
        } catch (IOException e) {
            showAlert("错误", "加载成绩数据失败: " + e.getMessage());
        }
    }

    @FXML
    private void handleExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出成绩数据");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV文件", "*.csv"));

        // 生成文件名（带时间戳）
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        fileChooser.setInitialFileName("scores_" + timestamp + ".csv");

        java.io.File file = fileChooser.showSaveDialog(scoreTable.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                // 写入表头
                writer.println("学号,姓名,课程,成绩");

                // 写入数据
                for (Score score : scoreData) {
                    writer.println(score.getStudentId() + "," +
                            score.getStudentName() + "," +
                            score.getCourse() + "," +
                            score.getScore());
                }

                showAlert("成功", "数据已成功导出到: " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("错误", "导出失败: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleAddScore() {
        // 在实际应用中，这里应该打开一个添加成绩的对话框
        // 这里简化为添加一个示例成绩
        Score newScore = new Score("2023001", "张三", "高等数学", 95.5);
        scoreData.add(newScore);
        saveScoreData();
    }

    // 保存成绩数据到文件
    private void saveScoreData() {
        try {
            String content = scoreData.stream()
                    .map(score -> score.getStudentId() + "," +
                            score.getStudentName() + "," +
                            score.getCourse() + "," +
                            score.getScore())
                    .collect(Collectors.joining(System.lineSeparator()));

            Files.write(Paths.get(SCORE_FILE), content.getBytes());
        } catch (IOException e) {
            showAlert("错误", "保存成绩数据失败: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
