package ch.epfl.rigel.bonus;

import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;

public class CameraManager {

    private static final double CAMERA_INITIAL_DISTANCE = -4;

    private final Group cameraXform = new Group();
    private Rotate ry = new Rotate();
    private double mousePosX;
    private double mouseOldX;
    private double mouseDeltaX;

    private Camera camera;

    public CameraManager(Camera cam, Pane pane) {
        camera = cam;

        pane.getChildren().add(cameraXform);
        cameraXform.getChildren().add(camera);

        ry.setAxis(Rotate.Y_AXIS);
        cameraXform.getTransforms().add(ry);

        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);

        // Add mouse handler
        handleMouse(pane);
    }

    private void handleMouse(Pane pane) {

        pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mousePosX = me.getSceneX();
                mouseDeltaX = (mousePosX - mouseOldX);
                if (me.isPrimaryButtonDown()) {
                    ry.setAngle(ry.getAngle() + 0.5*mouseDeltaX);
                }
            }
        });
    }

}