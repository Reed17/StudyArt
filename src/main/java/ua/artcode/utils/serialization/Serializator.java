package ua.artcode.utils.serialization;

/**
 * Created by serhii on 28.10.15.
 */
public interface Serializator<T> {

    void save(String path, T object);

    T load(String path);


}
