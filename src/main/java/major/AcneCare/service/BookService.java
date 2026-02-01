package major.AcneCare.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import major.AcneCare.model.Book;

@Service
public class BookService {
    private List<Book> books = new ArrayList<>();

    // Thêm sẵn một số quyển sách vào danh sách
    public BookService() {
        books.add(new Book("B001", "Clean Code", "Robert C. Martin", "Prentice Hall", 2008, 32.99));
        books.add(new Book("B002", "The Pragmatic Programmer", "Andrew Hunt", "Addison-Wesley", 1999, 25.50));
        books.add(new Book("B003", "Design Patterns", "Erich Gamma", "Addison-Wesley", 1994, 45.00));
    }

    // Hàm lấy danh sách tất cả quyển sách
    public List<Book> getAllBooks() {
        return books;
    }

    // Hàm thêm sách mới
    public void addBook(Book book) {
        books.add(book);
    }

    // Hàm xóa sách theo mã sách
    public void deleteBook(String bookId) {
        books.removeIf(book -> book.getBookId().equals(bookId));
    }

    // Hàm tìm sách theo mã sách
    public Book getBookById(String bookId) {
        return books.stream()
                .filter(book -> book.getBookId().equals(bookId))
                .findFirst()
                .orElse(null);
    }

    // Hàm cập nhật thông tin sách
    public void updatedBook(String bookId, Book updatedBook) {
        books.stream()
                .filter(b -> b.getBookId().equals(bookId))
                .findFirst()
                .ifPresent(b -> {
                    b.setTitle(updatedBook.getTitle());
                    b.setAuthor(updatedBook.getAuthor());
                    b.setPublisher(updatedBook.getPublisher());
                    b.setYearPublished(updatedBook.getYearPublished());
                    b.setPrice(updatedBook.getPrice());
                });
    }
}
