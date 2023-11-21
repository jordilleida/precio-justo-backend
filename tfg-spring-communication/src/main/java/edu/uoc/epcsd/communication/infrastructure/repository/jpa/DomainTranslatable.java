package edu.uoc.epcsd.communication.infrastructure.repository.jpa;

public interface DomainTranslatable<T> {

    T toDomain();

}