package th.ac.chula.cafetps;

import javafx.fxml.FXMLLoader;

import java.util.Objects;

public class Utility {

    public static FXMLLoader loadResource(Class<?> rootClass, String resourceName){
        return new FXMLLoader(rootClass.getResource("/th/ac/chula/cafetps/" + resourceName + ".fxml"));
    }

    public static String loadStyleSheet(Class<?> rootClass){
        return Objects.requireNonNull(
                rootClass.getResource("/th/ac/chula/cafetps/stylesheet.css")).toExternalForm();
    }
}
