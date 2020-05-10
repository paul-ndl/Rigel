package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public final class Main extends Application {

    private final ObserverLocationBean observerLocationBean = new ObserverLocationBean();
    private final DateTimeBean dateTimeBean = new DateTimeBean();
    private final ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
    private final TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);

    private StarCatalogue.Builder builder = new StarCatalogue.Builder();

    InputStream fontStream = getClass().getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf");
    private Font FONT_AWESOME = Font.loadFont(fontStream,15);

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws IOException {
        try (InputStream hs = resourceStream("/hygdata_v3.csv")) {
            builder = builder.loadFrom(hs, HygDatabaseLoader.INSTANCE);
        }
        try (InputStream as = resourceStream("/asterisms.txt")) {
            StarCatalogue catalogue = builder
                    .loadFrom(as, AsterismLoader.INSTANCE)
                    .build();

            observerLocationBean.setCoordinates(GeographicCoordinates.ofDeg(6.57, 46.52));

            ZonedDateTime when = ZonedDateTime.now(ZoneId.systemDefault());
            dateTimeBean.setZonedDateTime(when);

            viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(180.000000000001, 15));
            viewingParametersBean.setFieldOfViewDeg(70d);

            SkyCanvasManager canvasManager = new SkyCanvasManager(
                    catalogue,
                    dateTimeBean,
                    observerLocationBean,
                    viewingParametersBean);

            Canvas sky = canvasManager.canvas();
            Pane skyPane = new Pane(sky);

            primaryStage.setTitle("Rigel");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);

            HBox panel = ControlPanel();
            BorderPane infoPanel = InfoPanel(canvasManager);

            BorderPane root = new BorderPane(skyPane, panel, null, infoPanel, null);
            sky.widthProperty().bind(skyPane.widthProperty());
            sky.heightProperty().bind(skyPane.heightProperty());
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }

    private HBox ControlPanel(){
        HBox panel = new HBox();
        panel.setStyle("-fx-spacing: 4; -fx-padding: 4;");

        //first Hbox
        HBox coordinates = new HBox();
        coordinates.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        Label lonLabel = new Label("Longitude (°) :");
        TextField lonField = new TextField();
        TextFormatter<Number> lonTextFormatter = lonTextFormatter();
        lonField.setTextFormatter(lonTextFormatter);
        lonTextFormatter.valueProperty().bindBidirectional(observerLocationBean.lonDegProperty());
        observerLocationBean.setLonDeg(6.57);
        lonField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        Label latLabel = new Label("Latitude (°) :");
        TextField latField = new TextField();
        TextFormatter<Number> latTextFormatter = latTextFormatter();
        latField.setTextFormatter(latTextFormatter);
        latTextFormatter.valueProperty().bindBidirectional(observerLocationBean.latDegProperty());
        observerLocationBean.setLatDeg(42d);
        latField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

        Separator firstSep = new Separator();
        firstSep.setOrientation(Orientation.VERTICAL);

        coordinates.getChildren().addAll(lonLabel, lonField, latLabel, latField, firstSep);

        //second Hbox
        HBox time = new HBox();
        time.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        Label dateLabel = new Label("Date : ");
        DatePicker dateField = new DatePicker();
        dateField.valueProperty().bindBidirectional(dateTimeBean.dateProperty());
        dateField.setStyle("-fx-pref-width: 120");

        Label hourLabel = new Label("Heure : ");
        TextField hourField = new TextField();
        TextFormatter<LocalTime> timeFormatter = timeFormatter();
        timeFormatter.valueProperty().bindBidirectional(dateTimeBean.timeProperty());
        hourField.setTextFormatter(timeFormatter);
        hourField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");

        ComboBox<String> zoneId = new ComboBox<>();
        ObservableList<String> zoneList = FXCollections.observableArrayList(ZoneId.getAvailableZoneIds().stream().sorted().collect(Collectors.toUnmodifiableList()));
        zoneId.setItems(zoneList);
        zoneId.valueProperty().bind(Bindings.createObjectBinding(() -> dateTimeBean.getZoneId().toString(), dateTimeBean.zoneIdProperty()));

        Bindings.createObjectBinding(() -> ZoneId.of(zoneId.getValue()), zoneId.valueProperty());

        zoneId.setStyle("-fx-pref-width: 180;");

        Separator secondSep = new Separator();
        secondSep.setOrientation(Orientation.VERTICAL);

        time.getChildren().addAll(dateLabel, dateField, hourLabel, hourField, zoneId, secondSep);


        //third Hbox
        HBox animation = new HBox();
        animation.setStyle("-fx-spacing: inherit;");

        ChoiceBox<NamedTimeAccelerator> accelerator = new ChoiceBox<>();
        ObservableList<NamedTimeAccelerator> acceleratorList = FXCollections.observableArrayList(NamedTimeAccelerator.values());
        accelerator.setItems(acceleratorList);
        accelerator.setValue(NamedTimeAccelerator.TIMES_300);
        timeAnimator.acceleratorProperty().bind(Bindings.select(accelerator.valueProperty(), "accelerator"));

        Button resetButton = new Button("\uf0e2");
        resetButton.setFont(FONT_AWESOME);
        resetButton.setOnAction(actionEvent -> dateTimeBean.setZonedDateTime(ZonedDateTime.now(ZoneId.systemDefault())));

        Button playButton = new Button("\uf04b");
        playButton.setFont(FONT_AWESOME);

        playButton.setOnAction(actionEvent -> {
            if (timeAnimator.getRunning()) {
                playButton.setText("\uf04b");
                timeAnimator.stop();
                dateLabel.setDisable(false);
                dateField.setDisable(false);
                hourLabel.setDisable(false);
                hourField.setDisable(false);
                zoneId.setDisable(false);
                accelerator.setDisable(false);
                resetButton.setDisable(false);
            } else {
                playButton.setText("\uf04c");
                dateLabel.setDisable(true);
                dateField.setDisable(true);
                hourLabel.setDisable(true);
                hourField.setDisable(true);
                zoneId.setDisable(true);
                accelerator.setDisable(true);
                resetButton.setDisable(true);
                timeAnimator.start();
            }
        });

        animation.getChildren().addAll(accelerator, resetButton, playButton);

        panel.getChildren().addAll(coordinates, time, animation);

        return panel;
    }

    private BorderPane InfoPanel(SkyCanvasManager canvasManager){
        Text fov = new Text();
        fov.textProperty().bind(Bindings.format(Locale.ROOT, "Champ de vue : %.1f°", viewingParametersBean.fieldOfViewDegProperty()));
        Text celestialObjectText = new Text();
        StringBinding celestialObject = Bindings.createStringBinding(
                () -> {
                    return canvasManager.getObjectUnderMouse()==null ? "" : canvasManager.getObjectUnderMouse().toString();
                }, canvasManager.objectUnderMouseProperty()
        );
        celestialObjectText.textProperty().bind(Bindings.format(Locale.ROOT, "%s", celestialObject));
        Text position = new Text();
        position.textProperty().bind(Bindings.format(Locale.ROOT, "Azimut : %.2f° hauteur : %.2f°", canvasManager.mouseAzDegProperty(), canvasManager.mouseAltProperty()));
        BorderPane info = new BorderPane(celestialObjectText, null, position, null, fov);
        info.setStyle("-fx-padding: 4; -fx-background-color: white;");
        return info;
    }

    private TextFormatter<Number> lonTextFormatter(){
        NumberStringConverter stringConverter = new NumberStringConverter("#0.00");

        UnaryOperator<TextFormatter.Change> lonFilter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newLonDeg =
                        stringConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLonDeg(newLonDeg)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        return new TextFormatter<>(stringConverter, 6.57, lonFilter);
    }

    private TextFormatter<Number> latTextFormatter(){
        NumberStringConverter stringConverter = new NumberStringConverter("#0.00");

        UnaryOperator<TextFormatter.Change> lonFilter = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newLatDeg =
                        stringConverter.fromString(newText).doubleValue();
                return GeographicCoordinates.isValidLatDeg(newLatDeg)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        return new TextFormatter<>(stringConverter, 42, lonFilter);
    }

    private TextFormatter<LocalTime> timeFormatter(){
        DateTimeFormatter hmsFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter =
                new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        return new TextFormatter<>(stringConverter);
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }


}
