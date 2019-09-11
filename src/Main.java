import controller.Context;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage stage = Context.factory.createStage("LoginStage",600,450,"fxml/LoginStage.fxml");
        stage.getIcons().add(new Image("images/logo.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
