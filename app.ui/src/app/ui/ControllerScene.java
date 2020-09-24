package app.ui;

import app.common.Contact;
import app.db.DataSource;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerScene implements Initializable {
    @FXML
    public BorderPane borderPane;
    @FXML
    public GridPane gridPane;
    @FXML
    public TextField firstname;
    @FXML
    public Label labelFN;
    @FXML
    private TextField lastname;
    @FXML
    private TextField phone;
    @FXML
    private TextArea notes;
    @FXML
    private Button addBtn;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label update;

    private static String action;

    Controller controller = new Controller();

    private static final String EDIT = "edit";
    private static final String ADD = "add";

    private static Contact selected;

    public void processResultsForUpdate(int id, String assignment) throws InterruptedException {
        String fname = firstname.getText().trim();
        String lname = lastname.getText().trim();
        String pnumber = phone.getText().trim();
        String contactNotes = notes.getText().trim();
        if (fname.isEmpty() || pnumber.isEmpty()) {
            controller.handleAlert("No first name",null,"Contact has to have First name and number", Alert.AlertType.INFORMATION);
            return;
        } else {
            if(assignment.equals("UPDATE")){
                Task<Boolean> task = new Task<>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return DataSource.getInstance().contactUpdate(id, fname, lname, pnumber, contactNotes);
                    }
                };
                progressBar.progressProperty().bind(task.progressProperty());
                progressBar.setVisible(true);
                update.setVisible(true);
                task.setOnSucceeded(e -> {
                    progressBar.setVisible(false);
                    update.setVisible(false);
//                    ControllerScene.getInstance().cleaning();
//                    Controller.getInstance().updateTable();
//                    handlingSceneChange();
                });
                task.setOnFailed(event ->{
                    progressBar.setVisible(false);
                    update.setVisible(false);
//                    ControllerScene.getInstance().cleaning();
//                    Controller.getInstance().updateTable();
//                    handlingSceneChange();
                });

                new Thread(task).start();


            } else if(assignment.equals("INSERT")){
                Task<Boolean> task = new Task<>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return DataSource.getInstance().contactInsertMethod(fname, lname, pnumber, contactNotes);

                    }
                };
                progressBar.progressProperty().bind(task.progressProperty());
                progressBar.setVisible(true);
                update.setVisible(true);
                task.setOnSucceeded(e -> {
                    progressBar.setVisible(false);
                    update.setVisible(false);
//                    ControllerScene.getInstance().cleaning();
//                    Controller.getInstance().updateTable();
//                    handlingSceneChange();
                });
                task.setOnFailed(event -> {
                    progressBar.setVisible(false);
                    update.setVisible(false);
//                    ControllerScene.getInstance().cleaning();
//                    Controller.getInstance().updateTable();
//                    handlingSceneChange();
                });

                new Thread(task).start();
            }
        }
    }


    public void processEdit(){
        firstname.setText(selected.getFirstname());
        lastname.setText(selected.getLastname());
        phone.setText(selected.getPhone());
        notes.setText(selected.getNote());
    }

//    static Stage prevStage;
//    @Override
//    public void start(Stage primaryStage){
//        Image image = new Image("icons8-edit-profile-30.png");
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
//    public void setPrevStage(Stage stage){
//        this.prevStage = stage;
//    }

    private static Stage stage2 = null;
    private static ControllerScene instance = null;
    private static Scene scene = null;
    private static final URL location = ControllerScene.class.getResource("/app/ui/dialog.fxml");
    private static final FXMLLoader loader = new FXMLLoader();

    public static synchronized void show(String assignment, Contact selectedItem) throws IOException{
        if(stage2 == null) {
            //things that only need to be done once
            loader.setLocation(location);
            scene = new Scene((Pane) loader.load(location.openStream()), 340, 400);

            //by now, initialize method has been called
            //and a reference to this controller being stored in instance variable
            stage2 = Controller.getStage();
            stage2.setScene(scene);
//            stage2.initModality(Modality.WINDOW_MODAL);
//            stage1.getIcons().add();
//            stage2.initOwner(Controller.stage1.getScene().getWindow());
            checkAssignment(assignment, selectedItem);
        }else {
            checkAssignment(assignment, selectedItem);
            stage2.setScene(scene);
        }
        stage2.show();

        // code to return the state of the stage and it's controls to a known state

//        stage2.show();
//        stage2.toFront();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb){
        instance = this;
        //one time initializations to controls happen here
        //this is called during the loader.load()
        //only first time
    }
    public static ControllerScene getInstance(){
        return instance;
    }


    public void handlingScenes(String assignment, Contact selectedItem) {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/ui/dialog.fxml"));
//        Node root = null;
//        try {
//            root = loader.load();
//        }catch (IOException e){
//            System.out.println(e.getMessage());
//        }
//
//        prevStage.setTitle("Contacts");
//        prevStage.getIcons().add(new Image("file:app.ui\\src\\icons8-contact-100.png"));
//        prevStage.setScene(new Scene((Parent) root, 340, 400));
//
//        if(assignment.equals(EDIT)){
//            action = "EDIT";
//            ControllerScene controllerScene = loader.getController();
//            selected = selectedItem;
//            controllerScene.processEdit();
//            prevStage.show();
//
//        } else if(assignment.equals(ADD)){
//            action = "ADD";
//            prevStage.show();
//        }
    }
    @FXML
    public void handlingSceneChange(){
        try {
            ControllerScene.getInstance().cleaning();
            Controller.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAdd() throws InterruptedException {
        if (action.equals("ADD")) {
            processResultsForUpdate(0, "INSERT");
//            handlingSceneChange();
        }
        else if(action.equals("EDIT")){
            processResultsForUpdate(selected.getID(), "UPDATE");
//            handlingSceneChange();
        }
    }

    private static void checkAssignment(String assignment, Contact selectedItem){
        if(assignment.equals(EDIT)){
            action = "EDIT";
            ControllerScene controllerScene = loader.getController();
            selected = selectedItem;
            controllerScene.processEdit();
        } else if(assignment.equals(ADD)){
            action = "ADD";
        }
    }

    public void cleaning(){
        firstname.setText("");
        lastname.setText("");
        phone.setText("");
        notes.setText("");
    }
}

