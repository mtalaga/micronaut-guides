package example.micronaut.domain

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable

import javax.validation.constraints.NotNull

@CompileStatic
@Introspected
class Book {

    @Nullable
    Long id

    @NonNull
    @NotNull
    String name

    @NonNull
    @NotNull
    String isbn

    Genre genre

    Book(@NonNull @NotNull String isbn,
         @NonNull @NotNull String name,
         Genre genre) {
        this.isbn = isbn
        this.name = name
        this.genre = genre
    }

    @Override
    String toString() {
        "Book{id=$id, name='$name', isbn='$isbn', genre=$genre}"
    }
}