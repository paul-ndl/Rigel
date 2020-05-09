package ch.epfl.rigel.bonus;

import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;

public class CameraManager {

    private static final double CAMERA_INITIAL_DISTANCE = -4;
    private static final double CAMERA_INITIAL_X_ANGLE = 0.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 0.0;

    private final Group cameraXform = new Group();
    private Rotate rx = new Rotate();
    private Rotate ry = new Rotate();
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    private Camera camera;

    public CameraManager(Camera cam, Pane pane, Group root) {
        camera = cam;

        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(camera);

        rx.setAxis(Rotate.X_AXIS);
        ry.setAxis(Rotate.Y_AXIS);
        cameraXform.getTransforms().addAll(ry, rx);

        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        rx.setAngle(CAMERA_INITIAL_X_ANGLE);

        // Add mouse handler
        handleMouse(pane);
    }

    private void handleMouse(Pane pane) {

        pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);
                if (me.isPrimaryButtonDown()) {
                    ry.setAngle(ry.getAngle() + 0.5*mouseDeltaX);
                    //rx.setAngle(rx.getAngle() - mouseDeltaY);
                }
            }
        });
    }

}