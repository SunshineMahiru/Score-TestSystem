package com.example.controller;

import com.example.util.DataFileUtil;
import com.example.util.MD5Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class LoginController {
    // 使用DataFileUtil获取文件路径
    private static final String USER_FILE = DataFileUtil.getFilePath("users.txt");

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;

    // 确保文件存在
    private void ensureFileExists() throws IOException {
        Path filePath = Paths.get(USER_FILE);
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("学号或密码不能为空！");
            return;
        }

        try {
            ensureFileExists();
            List<String> lines = Files.readAllLines(Paths.get(USER_FILE));
            String hashedPassword = MD5Util.md5(password);

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(hashedPassword)) {
                    // 登录成功，跳转到主界面
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("下北泽大学成绩管理系统");
                    stage.setScene(new Scene(root));
                    stage.show();

                    // 关闭登录窗口
                    ((Stage) usernameField.getScene().getWindow()).close();
                    return;
                }
            }

            statusLabel.setText("学号或密码错误！");
        } catch (IOException e) {
            statusLabel.setText("系统错误: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("学号或密码不能为空！");
            return;
        }

        try {
            ensureFileExists();
            List<String> lines = Files.readAllLines(Paths.get(USER_FILE));

            // 检查用户是否已存在
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(username)) {
                    statusLabel.setText("学号已存在！");
                    return;
                }
            }

            // 注册新用户
            String hashedPassword = MD5Util.md5(password);
            String userRecord = username + "," + hashedPassword;
            Files.write(Paths.get(USER_FILE), (userRecord + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

            statusLabel.setText("注册成功！");
        } catch (IOException e) {
            statusLabel.setText("系统错误: " + e.getMessage());
        }
    }
}
