package fit_hutech_spring.services;

import fit_hutech_spring.entities.Book;
import fit_hutech_spring.repositories.IBookRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { Exception.class, Throwable.class })
public class BookService {
    private final IBookRepository bookRepository;

    public List<Book> getAllBooks(Integer pageNo,
            Integer pageSize,
            String sortBy) {
        return bookRepository.findAllBooks(pageNo, pageSize, sortBy);
    }

    // 1. Lấy sách theo ID (dùng cho Edit)
    public Book getBookById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        return optionalBook.orElse(null);
    }

    // 2. Thêm mới sách
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    // 3. Cập nhật sách
    public void updateBook(Book book) {
        bookRepository.save(book); // Hàm save trong JPA tự động hiểu là update nếu ID đã tồn tại
    }

    // 4. Xóa sách
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}