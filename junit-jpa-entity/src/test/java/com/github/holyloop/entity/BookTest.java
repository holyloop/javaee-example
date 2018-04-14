package com.github.holyloop.entity;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class BookTest extends BaseTest {

    @Test
    public void testAddBook() {
        Book book = new Book();
        book.setTitle("new book");
        book.setAuthor("new author");

        em.getTransaction().begin();
        em.persist(book);
        em.getTransaction().commit();

        @SuppressWarnings("rawtypes")
        List books = em.createQuery("select b from Book b").getResultList();
        assertEquals(2, books.size());
    }

    @Test
    public void testQueryBook() {
        Book book = em.find(Book.class, 1L);
        assertNotNull(book);
    }

}
