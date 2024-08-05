module by.poltavetsav.artifactsguiproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires java.net.http;
    requires org.json;
    requires jdk.compiler;

    opens by.poltavetsav.artifactsguiproject to javafx.fxml;
    exports by.poltavetsav.artifactsguiproject;
}