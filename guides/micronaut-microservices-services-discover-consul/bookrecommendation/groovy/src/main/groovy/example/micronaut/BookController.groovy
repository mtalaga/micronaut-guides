package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

@CompileStatic
@Controller("/books") // <1>
class BookController {

    private final BookCatalogueOperations bookCatalogueOperations
    private final BookInventoryOperations bookInventoryOperations

    BookController(BookCatalogueOperations bookCatalogueOperations,
                   BookInventoryOperations bookInventoryOperations) { // <2>
        this.bookCatalogueOperations = bookCatalogueOperations
        this.bookInventoryOperations = bookInventoryOperations
    }

    @Get // <3>
    Publisher<BookRecommendation> index() {
        Flux.from(bookCatalogueOperations.findAll())
                .flatMap(b -> Flux.from(bookInventoryOperations.stock(b.isbn))
                        .filter(Boolean::booleanValue)
                        .map(rsp -> b)
                ).map(book -> new BookRecommendation(book.name))
    }
}
