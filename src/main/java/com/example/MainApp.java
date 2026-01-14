package com.example;

import com.example.util.DataFileUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 初始化数据文件
        try {
            DataFileUtil.initDataFiles();
        } catch (IOException e) {
            System.err.println("初始化数据文件失败: " + e.getMessage());

            throw new RuntimeException("无法初始化数据文件", e);
        }

        // 加载登录界面
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("下北泽大学成绩管理系统 - 登录");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
