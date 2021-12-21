package com.example.seaker.database.specifications;

public interface ISpecification<T>{
    boolean isTrue(T item);
}
