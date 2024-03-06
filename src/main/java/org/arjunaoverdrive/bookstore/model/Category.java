package org.arjunaoverdrive.bookstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cat_seq_generator")
    @SequenceGenerator(name = "cat_seq_generator", allocationSize = 1)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "category", cascade = CascadeType.MERGE)
    private Set<Book> books = new HashSet<>();

    public void addBook(Book book){
        this.books.add(book);
        book.setCategory(this);
    }

    public void deleteBook(Book book){
        book.setCategory(null);
        this.books.remove(book);
    }
}
