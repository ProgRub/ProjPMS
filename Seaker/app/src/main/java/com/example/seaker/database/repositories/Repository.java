package com.example.seaker.database.repositories;

import com.example.seaker.database.specifications.ISpecification;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class Repository<T> {
    protected static final String ip = ; //erro propositadamente, para n se esquecerem de alterar :P
    abstract void add(T item);
    abstract Iterable<T> getAll();
    abstract T getById(long itemId);
    abstract Iterable<T> find(ISpecification specification);
    abstract void update(T oldItem,T newItem);
    abstract void remove(T itemToRemove);
    abstract void removeById(long itemId);
    public static boolean isInternetWorking(){
        try {
            HttpURLConnection.setFollowRedirects(false);
            URL url = new URL("http://" + ip + "/seaker/getallboats.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(1000);
            connection.connect();
            return (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (java.net.SocketTimeoutException e) {
            return false;
        }
        catch (IOException e) {
            return false;
        }
    }
}
