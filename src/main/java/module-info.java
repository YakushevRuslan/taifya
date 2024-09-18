module local.linux.tfy_curs {
    requires javafx.controls;
    requires javafx.fxml;


    opens local.linux.tfy_curs to javafx.fxml;
    exports local.linux.tfy_curs;
    exports local.linux.tfy_curs.util;
    opens local.linux.tfy_curs.util to javafx.fxml;
    exports local.linux.tfy_curs.controller;
    opens local.linux.tfy_curs.controller to javafx.fxml;
    exports local.linux.tfy_curs.model;
    opens local.linux.tfy_curs.model to javafx.fxml;
}