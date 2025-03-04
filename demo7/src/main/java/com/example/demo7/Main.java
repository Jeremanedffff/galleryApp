package com.example.demo7;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX Image Gallery Application
 * - Displays a grid of image thumbnails with names below each image.
 * - Clicking a thumbnail opens a full-size image.
 * - "Back", "Next", and "Previous" buttons allow navigation in full image view.
 * - ScrollPane added to the gallery for better navigation.
 */
public class Main extends Application {
    private List<File> imageFiles = new ArrayList<>();
    private BorderPane galleryPane = new BorderPane();
    private GridPane gridPane = new GridPane();
    private int currentIndex = 0; // Track current image index
    private Scene galleryScene; // Store the gallery scene

    @Override
    public void start(Stage primaryStage) {
        loadImages();
        createGalleryView(primaryStage);

        galleryScene = new Scene(galleryPane, 800, 600); // Store initial scene
        galleryScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setTitle("My Image Gallery");
        primaryStage.setScene(galleryScene);
        primaryStage.show();
    }

    private void loadImages() {
        File imageDir = new File("src/main/resources/com/example/demo7/images");
        if (imageDir.exists() && imageDir.isDirectory()) {
            for (File file : imageDir.listFiles()) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".jpg")) {
                    imageFiles.add(file);
                }
            }
        }
    }

    private void createGalleryView(Stage primaryStage) {
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.getStyleClass().add("grid-pane");
        int columns = 3;
        int row = 0, col = 0;

        for (int i = 0; i < imageFiles.size(); i++) {
            File file = imageFiles.get(i);
            String imagePath = file.toURI().toString();
            String imageName = file.getName();

            ImageView thumbnail = new ImageView(new Image(imagePath));
            thumbnail.getStyleClass().add("image-view-thumbnail");
            thumbnail.setFitWidth(150);
            thumbnail.setFitHeight(150);

            Label imageLabel = new Label(imageName);
            imageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");

            VBox imageBox = new VBox(5, thumbnail, imageLabel);
            imageBox.setAlignment(Pos.CENTER);

            int index = i; // Capture index for lambda expression
            thumbnail.setOnMouseClicked(e -> openFullSizeView(index, primaryStage));

            gridPane.add(imageBox, col, row);
            col++;
            if (col >= columns) {
                col = 0;
                row++;
            }
        }

        gridPane.setAlignment(Pos.CENTER);

        // Adding ScrollPane for scrolling functionality
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        galleryPane.setCenter(scrollPane);
    }

    private void openFullSizeView(int index, Stage primaryStage) {
        currentIndex = index;
        BorderPane pane = new BorderPane();

        ImageView fullSizeImage = new ImageView(new Image(imageFiles.get(currentIndex).toURI().toString()));
        fullSizeImage.setFitWidth(400);
        fullSizeImage.setFitHeight(300);
        fullSizeImage.getStyleClass().add("full-size");
        fullSizeImage.setPreserveRatio(true);

        Button prevButton = new Button("Previous");
        prevButton.setOnAction(e -> {
            if (currentIndex > 0) {
                currentIndex--;
                fullSizeImage.setImage(new Image(imageFiles.get(currentIndex).toURI().toString()));
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(galleryScene)); // Use stored scene

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> {
            if (currentIndex < imageFiles.size() - 1) {
                currentIndex++;
                fullSizeImage.setImage(new Image(imageFiles.get(currentIndex).toURI().toString()));
            }
        });

        HBox controls = new HBox(10, backButton, prevButton, nextButton);
        controls.setAlignment(Pos.CENTER);

        pane.setCenter(fullSizeImage);
        pane.setBottom(controls);

        Scene fullImageScene = new Scene(pane, 700, 500);
        primaryStage.setScene(fullImageScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}