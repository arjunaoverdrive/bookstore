package org.arjunaoverdrive.bookstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cat_seq_generator")
    @SequenceGenerator(name = "cat_seq_generator", allocationSize = 1)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book){
        this.books.add(book);
    }

    public void deleteBook(Book book){
        book.setCategory(null);
        this.books.remove(book);
    }
}
