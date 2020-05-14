package ch.epfl.rigel.bonus2;

import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;

public class CameraManager2 {

    private static final double CAMERA_INITIAL_DISTANCE = -4;

    private final Group cameraXform = new Group();
    private Rotate ry = new Rotate();
    private Rotate rx = new Rotate();
    private double mousePosX;
    private double mouseOldX;
    private double mouseDeltaX;
    private double mousePosY;
    private double mouseOldY;
    private double mouseDeltaY;

    private Camera camera;

    public CameraManager2(Camera cam, Pane pane) {
        camera = cam;

        pane.getChildren().add(cameraXform);
        cameraXform.getChildren().add(camera);

        ry.setAxis(Rotate.Y_AXIS);
        rx.setAxis(Rotate.X_AXIS);
        cameraXform.getTransforms().addAll(ry, rx);

        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);

        // Add mouse handler
        handleMouse(pane);
    }

    public Rotate getRy() {
        return ry;
    }

    private void handleMouse(Pane pane) {

        pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mousePosX = me.getSceneX();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseOldY = mousePosY;
                mousePosY = me.getSceneY();
                mouseDeltaY = (mousePosY - mouseOldY);
                if (me.isPrimaryButtonDown()) {
                    ry.setAngle(ry.getAngle() + 0.5*mouseDeltaX);
                    //rx.setAngle(rx.getAngle() + 0.5*mouseDeltaY);
                }
            }
        });
    }

}