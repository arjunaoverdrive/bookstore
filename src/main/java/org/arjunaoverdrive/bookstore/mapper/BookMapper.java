package org.arjunaoverdrive.bookstore.mapper;

import org.arjunaoverdrive.bookstore.model.Book;
import org.arjunaoverdrive.bookstore.web.dto.BookListResponse;
import org.arjunaoverdrive.bookstore.web.dto.BookResponse;
import org.arjunaoverdrive.bookstore.web.dto.UpsertBookRequest;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.UUID;

@DecoratedWith(BookMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CategoryMapper.class})
public interface BookMapper {

    BookResponse toDto(Book book);

    Book toBook(UpsertBookRequest request);

    Book toBook(UUID id, UpsertBookRequest request);

    default BookListResponse toListResponse(List<Book> bookList){
        BookListResponse response = new BookListResponse();

        List<BookResponse> books = bookList.stream()
                .map(this::toDto)
                .toList();
        response.setBooks(books);
        return response;
    };


}
