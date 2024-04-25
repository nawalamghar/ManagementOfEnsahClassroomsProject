module GestionSalles {
	requires javafx.controls;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
	
	opens controllers to javafx.fxml; 
	requires java.sql;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	exports controllers; 
}
