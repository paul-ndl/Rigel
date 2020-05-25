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
import java.util.List;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public final class Main extends Application {

    private final ObserverLocationBean observerLocationBean = new ObserverLocationBean();
    private final DateTimeBean dateTimeBean = new DateTimeBean();
    private final ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
    private final TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);

    private StarCatalogue.Builder builder = new StarCatalogue.Builder();

    private final InputStream fontStream = resourceStream("/Font Awesome 5 Free-Solid-900.otf");
    private final Font FONT_AWESOME = Font.loadFont(fontStream,15);
    private static final String RESET_ICON = "\uf0e2";
    private static final String PLAY_ICON = "\uf04b";
    private static final String PAUSE_ICON = "\uf04c";

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws IOException {
        try (InputStream hs = resourceStream("/hygdata_v3.csv")) {
            builder.loadFrom(hs, HygDatabaseLoader.INSTANCE);
        }
        try (InputStream as = resourceStream("/asterisms.txt")) {
            StarCatalogue catalogue = builder
                    .loadFrom(as, AsterismLoader.INSTANCE)
                    .build();

            observerLocationBean.setCoordinates(GeographicCoordinates.ofDeg(6.57, 46.52));
            dateTimeBean.setZonedDateTime(ZonedDateTime.now(ZoneId.systemDefault()));
            viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(180.000000000001, 15));
            viewingParametersBean.setFieldOfViewDeg(70d);

            SkyCanvasManager canvasManager = new SkyCanvasManager(catalogue,
                                                                  dateTimeBean,
                                                                  observerLocationBean,
                                                                  viewingParametersBean);
            Canvas sky = canvasManager.canvas();
            Pane skyPane = new Pane(sky);
            sky.widthProperty().bind(skyPane.widthProperty());
            sky.heightProperty().bind(skyPane.heightProperty());

            HBox panel = controlPanel();
            BorderPane infoPanel = InfoPanel(canvasManager);
            BorderPane root = new BorderPane(skyPane, panel, null, infoPanel, null);
            
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Rigel");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.show();
        }
    }

    private HBox controlPanel(){
        HBox panel = new HBox();
        panel.setStyle("-fx-spacing: 4; -fx-padding: 4;");

        //first HBox
        HBox coordinates = new HBox();
        coordinates.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        Label lonLabel = new Label("Longitude (°) :");
        TextField lonField = new TextField();
        lonField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        TextFormatter<Number> lonTextFormatter = coordTextFormatter(true, 6.57);
        lonField.setTextFormatter(lonTextFormatter);
        lonTextFormatter.valueProperty().bindBidirectional(observerLocationBean.lonDegProperty());

        Label latLabel = new Label("Latitude (°) :");
        TextField latField = new TextField();
        latField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        TextFormatter<Number> latTextFormatter = coordTextFormatter(false, 46.52);
        latField.setTextFormatter(latTextFormatter);
        latTextFormatter.valueProperty().bindBidirectional(observerLocationBean.latDegProperty());

        coordinates.getChildren().addAll(lonLabel, lonField, latLabel, latField);

        //second HBox
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

        List<ZoneId> zoneList = ZoneId.getAvailableZoneIds().stream().sorted().map(ZoneId::of).collect(Collectors.toUnmodifiableList());
        ComboBox<ZoneId> zoneId = new ComboBox<>(FXCollections.observableList(zoneList));
        zoneId.setStyle("-fx-pref-width: 180;");
        zoneId.valueProperty().bindBidirectional(dateTimeBean.zoneIdProperty());

        time.getChildren().addAll(dateLabel, dateField, hourLabel, hourField, zoneId);

        //third Hbox
        HBox animation = new HBox();
        animation.setStyle("-fx-spacing: inherit;");

        ChoiceBox<NamedTimeAccelerator> accelerator = new ChoiceBox<>();
        ObservableList<NamedTimeAccelerator> acceleratorList = FXCollections.observableArrayList(NamedTimeAccelerator.values());
        accelerator.setItems(acceleratorList);
        accelerator.setValue(NamedTimeAccelerator.TIMES_300);
        timeAnimator.acceleratorProperty().bind(Bindings.select(accelerator.valueProperty(), "accelerator"));

        Button resetButton = new Button(RESET_ICON);
        resetButton.setFont(FONT_AWESOME);
        resetButton.setOnAction(actionEvent -> dateTimeBean.setZonedDateTime(ZonedDateTime.now(ZoneId.systemDefault())));

        Button playPauseButton = new Button(PLAY_ICON);
        playPauseButton.setFont(FONT_AWESOME);

        playPauseButton.setOnAction(actionEvent -> {
            if (timeAnimator.getRunning()) {
                playPauseButton.setText(PLAY_ICON);
                timeAnimator.stop();
                dateLabel.setDisable(false);
                dateField.setDisable(false);
                hourLabel.setDisable(false);
                hourField.setDisable(false);
                zoneId.setDisable(false);
                accelerator.setDisable(false);
                resetButton.setDisable(false);
            } else {
                playPauseButton.setText(PAUSE_ICON);
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

        animation.getChildren().addAll(accelerator, resetButton, playPauseButton);

        //final HBox
        Separator sepCoordVsTime = new Separator(Orientation.VERTICAL);
        Separator sepTimeVsAnim = new Separator(Orientation.VERTICAL);
        panel.getChildren().addAll(coordinates, sepCoordVsTime, time, sepTimeVsAnim, animation);
        return panel;
    }

    private BorderPane InfoPanel(SkyCanvasManager canvasManager){
        Text fov = new Text();
        fov.textProperty().bind(Bindings.format(Locale.ROOT, "Champ de vue : %.1f°", viewingParametersBean.fieldOfViewDegProperty()));
        Text celestialObjectText = new Text();
        StringBinding celestialObject = Bindings.createStringBinding(
                () ->  canvasManager.getObjectUnderMouse()==null ? "" : canvasManager.getObjectUnderMouse().toString(), 
                canvasManager.objectUnderMouseProperty()
        );
        celestialObjectText.textProperty().bind(Bindings.format(Locale.ROOT, "%s", celestialObject));
        Text position = new Text();
        position.textProperty().bind(Bindings.format(Locale.ROOT, "Azimut : %.2f°, hauteur : %.2f°", canvasManager.mouseAzDegProperty(), canvasManager.mouseAltProperty()));
        BorderPane info = new BorderPane(celestialObjectText, null, position, null, fov);
        info.setStyle("-fx-padding: 4; -fx-background-color: white;");
        return info;
    }

    private TextFormatter<Number> coordTextFormatter(boolean lon, double defaultValue){
        NumberStringConverter stringConverter = new NumberStringConverter("#0.00");

        UnaryOperator<TextFormatter.Change> filter = (change -> {
            try {
                String newText = change.getControlNewText();
                double newDeg = stringConverter.fromString(newText).doubleValue();
                if(lon){
                    return GeographicCoordinates.isValidLonDeg(newDeg) ? change : null;
                } else {
                    return GeographicCoordinates.isValidLatDeg(newDeg) ? change : null;
                }
            } catch (Exception e) {
                return null;
            }
        });

        return new TextFormatter<>(stringConverter, defaultValue, filter);
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
