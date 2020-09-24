package app.ui;

import app.common.Contact;
import app.db.DataSource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.crypto.Cipher;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TableView<Contact> contactsTableView;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label update;

    public void listContacts(){
        Task<ObservableList<Contact>> task = new GetAllContactsTask();
        contactsTableView.itemsProperty().bind(task.valueProperty());
        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.setVisible(true);
        update.setVisible(true);
        task.setOnSucceeded(e -> {
            progressBar.setVisible(false);
            update.setVisible(false);
        });
        task.setOnFailed(event -> {
            progressBar.setVisible(false);
            update.setVisible(false);
        });

        new Thread(task).start();
        System.out.println("Thread list cont started");
    }
    @FXML
    public void showDialog() throws Exception {
//        controllerScene.setPrevStage(prevStage);
        ControllerScene.show("add", null);

//        controllerScene.handlingScenes("add", null);
//        controllerScene.handlingScenes();
//        Dialog<ButtonType> dialog = new Dialog<>();
//        dialog.initOwner(mainBorderPane.getScene().getWindow());
//        dialog.setTitle("Add new Contact");
//        FXMLLoader fxmlLoader = new FXMLLoader();                                       //loading fxml in steps bellow
//        fxmlLoader.setLocation(getClass().getResource("dialog.fxml"));
//        try {
//            dialog.getDialogPane().setContent(fxmlLoader.load());
//        } catch (IOException e) {
//            System.out.println("Couldn't load the dialog");
//            e.printStackTrace();
//            return;
//        }
//        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
//        ButtonBar buttonBar = (ButtonBar) dialog.getDialogPane().lookup(".button-bar");
//        buttonBar.setStyle("-fx-background-color: #262626;");
//
//        Optional<ButtonType> result = dialog.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            ControllerDialog controller = fxmlLoader.getController();
////            controller.processResults();
//            controller.processResultsForUpdate(0, "INSERT");
//        }
//        contactsTableView.getItems().clear();
//        contactsTableView.getItems().addAll(FXCollections.observableArrayList(DataSource.getInstance().contactsQueryMethod()));
    }
    public void updateTable(){
        contactsTableView.getItems().clear();
        contactsTableView.getItems().addAll(FXCollections.observableArrayList(DataSource.getInstance().contactsQueryMethod()));
    }

    @FXML
    public void showEditDialog() throws Exception {
        Contact selected = contactsTableView.getSelectionModel().getSelectedItem();

        if(selected == null){
            handleAlert("No contact selected",null,"Please select the contact you want to edit", Alert.AlertType.INFORMATION);
            return;
        }

//        Dialog<ButtonType> dialog = new Dialog<>();
//        dialog.initOwner(mainBorderPane.getScene().getWindow());
//        dialog.setTitle("Edit Contact");
//
//        FXMLLoader fxmlLoader = new FXMLLoader();                                       //loading fxml in steps bellow
//        fxmlLoader.setLocation(getClass().getResource("dialog.fxml"));
//        try {
//            dialog.getDialogPane().setContent(fxmlLoader.load());
//        } catch (IOException e) {
//            System.out.println("Couldn't load the dialog");
//            e.printStackTrace();
//            return;
//        }
//
//        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
//        ButtonBar buttonBar = (ButtonBar) dialog.getDialogPane().lookup(".button-bar");
//        buttonBar.setStyle("-fx-background-color: #262626;");

//        controllerScene.setPrevStage(prevStage);
        ControllerScene.show("edit", selected);

//        controllerScene.firstname.setText(selected.getFirstname());
//        controllerScene.processEdit(selected);
//        controllerScene.handlingScenes("edit", selected);

//
//        Optional<ButtonType> result = dialog.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//        .
////            Task task = new Task(){
////                @Override
////                protected Object call() throws Exception {
//////                    return DataSource.getInstance().contactUpdate(contact.getID(), contact.getFirstname(), contact.getLastname(), contact.getPhone(), contact.getNote());
////                    return DataSource.getInstance().contactUpdate(controller.processResultsForUpdate(contact.getID()));
////                }
////            };
////            contactsTableView.refresh();
////            new Thread(task).start();
//            controllerScene.processResultsForUpdate(selected.getID(), "UPDATE");

//        updateTable();
//        updating table view
    }
    @FXML
    public void handleDelete(){
        Contact selected = contactsTableView.getSelectionModel().getSelectedItem();
        if(selected == null){
            handleAlert("No contact selected",null,"Please select the contact you want to delete", Alert.AlertType.INFORMATION);
        } else {
            Optional<ButtonType> result = handleAlert("Delete Contact", null, "Are you sure?", Alert.AlertType.CONFIRMATION);
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                Task<Boolean> task = new Task<>() {
                    @Override
                    protected Boolean call() throws Exception {
                       return DataSource.getInstance().deleteContact(selected.getID());
                    }
                };
                progressBar.progressProperty().bind(task.progressProperty());
                progressBar.setVisible(true);
                task.setOnSucceeded(e -> {
                    progressBar.setVisible(false);
                    new Thread(){
                        public void run(){
                            Controller.getInstance().listContacts();
                        }
                    }.start();
                });
                task.setOnFailed(event -> progressBar.setVisible(false));

                new Thread(task).start();
                }

        }
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) throws Exception {
        Contact selectedContact = contactsTableView.getSelectionModel().getSelectedItem();
        if(selectedContact != null){
            if(keyEvent.getCode().equals(KeyCode.DELETE)){
                handleDelete();
            }else if(keyEvent.getCode().equals(KeyCode.ENTER)){
                showEditDialog();
            }
        }
    }

    @FXML
    public void exit(){
        DataSource.getInstance().closeConnection();
        Platform.exit();
        System.exit(0);
    }

    public Optional<ButtonType> handleAlert(String title, String header, String content, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.lookup(".content.label").setStyle("-fx-background-color: #262626; -fx-text-fill: white");
        GridPane grid = (GridPane)dialogPane.lookup(".header-panel");
        grid.setStyle("-fx-background-color: #262626;");
        alert.setHeaderText(header);
        ButtonBar buttonBar = (ButtonBar)alert.getDialogPane().lookup(".button-bar");
        buttonBar.setStyle("-fx-background-color: #262626;");

        return alert.showAndWait(); //later is this checked for pressed button
    }

//    static Stage prevStage;
//    @Override
//    public void start(Stage primaryStage){
//        setPrimaryStage(primaryStage);
//        prevStage = primaryStage;
//    }
//    public static Stage getPrimaryStage(){
//        return prevStage;
//    }
//    public void setPrimaryStage(Stage stage){
//        Controller.prevStage = stage;
//    }
//
//    public void setPrevStage(Stage stage) throws Exception {
//        this.prevStage = stage;
//    }
    private static Stage stage1 = null;
    private static Controller instance = null;
    private static Scene scene = null;

    public static synchronized void show() throws IOException{
        if(stage1 == null){
            //things that only need to be done once
            URL location = Controller.class.getResource("/app/ui/sample.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            scene = new Scene((BorderPane) loader.load(location.openStream()), 340, 400);

            System.out.println("List contacts method called");
            Controller.getInstance().listContacts();

            //by now, initialize method has been called
            //and a reference to this controller being stored in instance variable
            stage1 = new Stage();
            stage1.setScene(scene);
            stage1.setTitle("Contacts");
            stage1.initModality(Modality.WINDOW_MODAL);
            stage1.getIcons().add(new Image("file:app.ui\\src\\icons8-contacts-100.png"));
//            stage1.initOwner(ControllerScene.stage2.getScene().getWindow());
            stage1.show();
        } else if(stage1 != null){
            System.out.println("List contacts method called");
            Controller.getInstance().listContacts();
            stage1.setScene(scene);
            stage1.show();
        }
        // code to return the state of the stage and it's controls to a known state
        stage1.toFront();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb){
        instance = this;
        //one time initializations to controls happen here
        //this is called during the loader.load()
        //only first time
    }
    public static Controller getInstance(){
        return instance;
    }
    public static Stage getStage(){
        return stage1;
    }




    public void handlingScenes() throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/ui/sample.fxml"));
//        Parent root = loader.load();
////        try {
////            root = loader.load();
////        }catch (IOException e){
////            System.out.println(e.getMessage());
////        }
//
//        /*
//        To allow access to instance data, you just have to have a reference to the instance.
//        The FXMLLoader has a getController() method that allows you to retrieve a reference to the controller.
//         */
//        Controller controller = loader.getController();
//        controller.listContacts();
//
//        prevStage.setTitle("Contacts");
//        prevStage.getIcons().add(new Image("file:app.ui\\src\\icons8-contacts-100.png"));
//        prevStage.setScene(new Scene(root, 340, 400));
//        prevStage.show();
        stage1.show();
    }

}
class GetAllContactsTask extends Task<ObservableList<Contact>> {
    @Override
    public ObservableList<Contact> call() {
        return FXCollections.observableArrayList(DataSource.getInstance().contactsQueryMethod());
    }
}


