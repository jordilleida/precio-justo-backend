package edu.uoc.tfg.auction.infrastructure.repository.jpa;

public interface DomainTranslatable<T> {

    T toDomain();

}

