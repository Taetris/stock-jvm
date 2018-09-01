package application

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import java.util.*

object ResourceLoader {

    val bundle = PropertyResourceBundle(ResourceLoader::class.java.classLoader.getResource("bundles/default.properties").openStream())

    fun <T> loader(clazz: Class<T>, resource: String): FXMLLoader {
        return FXMLLoader(clazz.classLoader.getResource(resource), bundle)
    }

    fun <T> load(clazz: Class<T>, resource: String): Parent {
        return FXMLLoader.load<Parent>(clazz.classLoader.getResource(resource), bundle)
    }
}
