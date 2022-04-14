package org.xy.passportScanner;

import javafx.scene.image.Image;
import org.xy.passportScanner.config.GlobalData;
import org.xy.passportScanner.config.Settings;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import java.io.InputStream;

public class Main extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception {

        //加载配置文件中的设置
        loadSettings();

        //加载界面文件
        Parent load = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        //设置窗口大小
        Scene scene = new Scene(load, 1024, 500);
        primaryStage.setScene(scene);
        //设置标题和logo
        primaryStage.setTitle("PassportScanner");
        primaryStage.getIcons().add(new Image("images/passport.png"));
        //显示页面
        primaryStage.show();
        //关闭事件处理
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    /**
     * 加载配置文件
     */
    private void loadSettings() {
        try {
            Yaml yaml = new Yaml(new Constructor(Settings.class));
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("settings.yaml");
            Settings settings = yaml.load(inputStream);
            GlobalData.settings = settings;
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
