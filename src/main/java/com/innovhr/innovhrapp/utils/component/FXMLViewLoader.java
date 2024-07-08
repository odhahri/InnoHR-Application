package com.innovhr.innovhrapp.utils.component;

import com.innovhr.innovhrapp.InnovhrApplication;
import com.innovhr.innovhrapp.utils.constants.ViewPresets;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import java.io.IOException;

public class FXMLViewLoader {

    public static void loadLoginScene(Stage stage, String title, String resource)  throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InnovhrApplication.class.getResource(resource));
        Scene scene = new Scene(fxmlLoader.load(), ViewPresets.visualBounds.getWidth(), ViewPresets.visualBounds.getHeight());
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }



    public void loadLoginScene(Stage stage, String title, String resource, Rectangle2D visualBounds)  throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InnovhrApplication.class.getResource(resource));
        Scene scene = new Scene(fxmlLoader.load(), visualBounds.getWidth(), visualBounds.getHeight());
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
    public void loadLoginScene(Stage stage, String title, String resource, double width, double height)  throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InnovhrApplication.class.getResource(resource));
        Scene scene = new Scene(fxmlLoader.load(), width,height );
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
    public void loadScene(String title, String resource, Rectangle2D visualBounds)  throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InnovhrApplication.class.getResource(resource));
        Scene scene = new Scene(fxmlLoader.load(), visualBounds.getWidth(), visualBounds.getHeight());
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
    public void loadScene(String title, String resource, double width, double height)  throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InnovhrApplication.class.getResource(resource));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
    public static void loadScene(String title, String resource)  throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InnovhrApplication.class.getResource(resource));
        Scene scene = new Scene(fxmlLoader.load(),ViewPresets.visualBounds.getWidth(), ViewPresets.visualBounds.getHeight());
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static Object loadSceneContainer(AnchorPane mainContainer, String resource) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InnovhrApplication.class.getResource(resource));
        Node view = fxmlLoader.load();
        mainContainer.getChildren().setAll(view);
        return fxmlLoader.getController();

    }
    public void reloadScene(){

    }
    public static void closeScene(VBox SceneBox){
        SceneBox.getScene().getWindow().hide();
    }

}
