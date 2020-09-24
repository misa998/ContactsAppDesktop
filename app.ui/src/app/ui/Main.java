package app.ui;

import app.db.DataSource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    /*
    Class Application documentation about methods bellow
    https://docs.oracle.com/javase/8/javafx/api/javafx/application/Application.html
     */

    @Override
    public void start(Stage primaryStage) throws Exception{
//        //making sure that ui runs before query
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/ui/sample.fxml"));
//        Parent root = loader.load();
//        Controller controller = loader.getController();
//        controller.listContacts();
//
//        primaryStage.setTitle("Contacts");
////        primaryStage.initStyle(StageStyle.UNDECORATED);
//        primaryStage.setScene(new Scene(root, 400, 400));
//
//        primaryStage.getIcons().add(new Image("file:app.ui\\src\\icons8-contact-100.png"));
//        primaryStage.show();

//        Controller controller = new Controller();
//        ControllerPlusCScene controller = new ControllerPlusCScene();
//        controller.setPrevStage(primaryStage);
//        controller.handlingScenes();
        Controller.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        if(!DataSource.getInstance().openConnection()){
            System.out.println("Error in connection");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DataSource.getInstance().closeConnection();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
