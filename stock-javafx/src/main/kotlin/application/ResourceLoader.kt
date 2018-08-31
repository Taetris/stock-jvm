package application

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import java.util.*

object ResourceLoader {

    fun <T> loader(clazz: Class<T>, resource: String): FXMLLoader {
        val bundle = PropertyResourceBundle(clazz.classLoader.getResource("bundles/default.properties").openStream())
        return FXMLLoader(clazz.classLoader.getResource(resource), bundle)
    }

    fun <T> load(clazz: Class<T>, resource: String): Parent {
        val bundle = PropertyResourceBundle(clazz.classLoader.getResource("bundles/default.properties").openStream())
        return FXMLLoader.load<Parent>(clazz.classLoader.getResource(resource), bundle)
    }
}
