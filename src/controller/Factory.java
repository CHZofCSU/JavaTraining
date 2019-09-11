package controller;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;

public class Factory {

    public Stage createStage(String title, int width, int height, String file) throws IOException {

        URL url = Factory.class.getClassLoader().getResource(file);
        Parent root = FXMLLoader.load(url);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root, width, height));

        stage.setResizable(false);
        //设置在stage关闭时需要进行的操作
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //此处当stage关闭时，同时结束程序，避免stage关闭后，程序界面关闭了，但后台线程却依然运行的问题
                System.exit(0);
            }
        });
        //初始化Stage时将该实例添加进StageManager的容器中，方便管理
        Context.stageManager.addStage(title, stage);
        return stage;
    }

}
