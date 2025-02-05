package com.polarbookshop.orderservice.order.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import com.polarbookshop.orderservice.book.Book;

@Table("orders")
public record Order(

    @Id
    Long id,

    String bookIsbn,
    String bookName,
    Double bookPrice,
    Integer quantity,
    OrderStatus status,

    @CreatedDate
    Instant createdDate,

    @LastModifiedDate
    Instant lastModifiedDate,

    @Version
    int version
) {
    public static Order of(
        String bookIsbn, String bookName, Double bookPrice, Integer quantity, OrderStatus status
    ) {
        return new Order(
            null, bookIsbn, bookName, bookPrice, quantity, status, null, null, 0
        );
    }

    public static Order buildAcceptedOrder(Book book, int quantity) {
        return Order.of(book.isbn(), book.title() + "-" + book.author(),
            book.price(), quantity, OrderStatus.ACCEPTED);
    }

    public static Order buildRejectedOrder(String bookIsbn, int quantity) {
        return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
    }
}
