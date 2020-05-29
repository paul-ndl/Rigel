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
import java.io.UncheckedIOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Programme principal
 *
 * @author Paul Nadal (300843)
 * @author Alexandre Brun (302477)
 */
public final class Main extends Application {

    private final ObserverLocationBean observerLocationBean = new ObserverLocationBean();
    private final DateTimeBean dateTimeBean = new DateTimeBean();
    private final ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
    private final TimeAnimator timeAnimator = new TimeAnimator(dateTimeBean);
    private final StarCatalogue.Builder builder = new StarCatalogue.Builder();

    private static final String TITLE = "Rigel";
    private static final double MIN_WIDTH = 800;
    private static final double MIN_HEIGHT = 600;

    private static final String HYG_FILE = "/hygdata_v3.csv";
    private static final String AST_FILE = "/asterisms.txt";

    private static final double INIT_LON = 6.57;
    private static final double INIT_LAT = 46.52;
    private static final double INIT_AZ = 180.000000000001;
    private static final double INIT_ALT = 15;
    private static final double INIT_FOV = 70;

    private static final String RESET_ICON = "\uf0e2";
    private static final String PLAY_ICON = "\uf04b";
    private static final String PAUSE_ICON = "\uf04c";
    private static final String FONT_FILE = "/Font Awesome 5 Free-Solid-900.otf";
    private static final double FONT_SIZE = 15;
    private final static Font FONT_AWESOME = fontLoader();


    /**
     * Lance le programme principal avec les arguments données
     *
     * @param args les arguments
     * @see Application#launch(String...)
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Lance l'application
     *
     * @param primaryStage la scène
     * @throws IOException en cas d'erreur entrée/sortie
     * @see Application#start(Stage)
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        try (InputStream hs = resourceStream(HYG_FILE); InputStream as = resourceStream(AST_FILE)) {
            StarCatalogue catalogue = builder.loadFrom(hs, HygDatabaseLoader.INSTANCE)
                    .loadFrom(as, AsterismLoader.INSTANCE)
                    .build();

            observerLocationBean.setCoordinates(GeographicCoordinates.ofDeg(INIT_LON, INIT_LAT));
            dateTimeBean.setZonedDateTime(ZonedDateTime.now(ZoneId.systemDefault()));
            viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(INIT_AZ, INIT_ALT));
            viewingParametersBean.setFieldOfViewDeg(INIT_FOV);

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
            primaryStage.setTitle(TITLE);
            primaryStage.setMinWidth(MIN_WIDTH);
            primaryStage.setMinHeight(MIN_HEIGHT);
            primaryStage.show();
        }
    }

    private HBox controlPanel() {
        //first HBox
        HBox coordinates = new HBox();
        coordinates.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        Label lonLabel = new Label("Longitude (°) :");
        TextField lonField = new TextField();
        lonField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        TextFormatter<Number> lonTextFormatter = coordTextFormatter(true, INIT_LON);
        lonField.setTextFormatter(lonTextFormatter);
        lonTextFormatter.valueProperty().bindBidirectional(observerLocationBean.lonDegProperty());

        Label latLabel = new Label("Latitude (°) :");
        TextField latField = new TextField();
        latField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        TextFormatter<Number> latTextFormatter = coordTextFormatter(false, INIT_LAT);
        latField.setTextFormatter(latTextFormatter);
        latTextFormatter.valueProperty().bindBidirectional(observerLocationBean.latDegProperty());

        coordinates.getChildren().addAll(lonLabel, lonField, latLabel, latField);

        //second HBox
        HBox time = new HBox();
        time.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        time.disableProperty().bind(timeAnimator.runningProperty());

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
        accelerator.disableProperty().bind(timeAnimator.runningProperty());
        timeAnimator.acceleratorProperty().bind(Bindings.select(accelerator.valueProperty(), "accelerator"));

        Button resetButton = new Button(RESET_ICON);
        resetButton.setFont(FONT_AWESOME);
        resetButton.setOnAction(actionEvent -> dateTimeBean.setZonedDateTime(ZonedDateTime.now(ZoneId.systemDefault())));
        resetButton.disableProperty().bind(timeAnimator.runningProperty());

        Button playPauseButton = new Button(PLAY_ICON);
        playPauseButton.setFont(FONT_AWESOME);

        playPauseButton.setOnAction(actionEvent -> {
            if (timeAnimator.getRunning()) {
                timeAnimator.stop();
                playPauseButton.setText(PLAY_ICON);
            } else {
                timeAnimator.start();
                playPauseButton.setText(PAUSE_ICON);
            }
        });

        animation.getChildren().addAll(accelerator, resetButton, playPauseButton);

        //final HBox
        Separator sepCoordVsTime = new Separator(Orientation.VERTICAL);
        Separator sepTimeVsAnim = new Separator(Orientation.VERTICAL);
        HBox panel = new HBox();
        panel.setStyle("-fx-spacing: 4; -fx-padding: 4;");
        panel.getChildren().addAll(coordinates, sepCoordVsTime, time, sepTimeVsAnim, animation);
        return panel;
    }

    private BorderPane InfoPanel(SkyCanvasManager canvasManager) {
        Text fov = new Text();
        fov.textProperty().bind(Bindings.format(Locale.ROOT, "Champ de vue : %.1f°", viewingParametersBean.fieldOfViewDegProperty()));
        Text celestialObjectText = new Text();
        StringBinding celestialObject = Bindings.createStringBinding(
                () -> canvasManager.getObjectUnderMouse() == null ? "" : canvasManager.getObjectUnderMouse().toString(),
                canvasManager.objectUnderMouseProperty()
        );
        celestialObjectText.textProperty().bind(Bindings.format(Locale.ROOT, "%s", celestialObject));
        Text position = new Text();
        position.textProperty().bind(Bindings.format(Locale.ROOT, "Azimut : %.2f°, hauteur : %.2f°", canvasManager.mouseAzDegProperty(), canvasManager.mouseAltProperty()));
        BorderPane info = new BorderPane(celestialObjectText, null, position, null, fov);
        info.setStyle("-fx-padding: 4; -fx-background-color: white;");
        return info;
    }

    private TextFormatter<Number> coordTextFormatter(boolean lon, double defaultValue) {
        NumberStringConverter stringConverter = new NumberStringConverter("#0.00");

        UnaryOperator<TextFormatter.Change> filter = (change -> {
            try {
                String newText = change.getControlNewText();
                double newDeg = stringConverter.fromString(newText).doubleValue();
                if (lon) {
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

    private TextFormatter<LocalTime> timeFormatter() {
        DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        return new TextFormatter<>(stringConverter);
    }

    private static Font fontLoader() {
        try (InputStream fontStream = Main.class.getResourceAsStream(FONT_FILE)) {
            return Font.loadFont(fontStream, FONT_SIZE);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }

}
