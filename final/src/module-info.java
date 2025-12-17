
module Final {
    requires javafx.swt;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    requires jfx.incubator.input;
    requires jfx.incubator.richtext;
    exports main to javafx.graphics;
    exports controlador to javafx.fxml;
    opens controlador to javafx.fxml;
    opens modelo.entidades to javafx.base;
}
