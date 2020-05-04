package ch.epfl.rigel.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class select {
    public static void main(String args[]){
        ObjectProperty<NamedTimeAccelerator> p1 = new SimpleObjectProperty<>(NamedTimeAccelerator.TIMES_1);
        ObjectProperty<TimeAccelerator> p2 =
                new SimpleObjectProperty<>();

        p2.addListener((p, o, n) -> {
            System.out.printf("old: %s  new: %s%n", o, n);
        });

        p2.bind(Bindings.select(p1, "accelerator"));
        p1.set(NamedTimeAccelerator.TIMES_30);
    }

}
