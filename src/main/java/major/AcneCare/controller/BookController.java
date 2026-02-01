package major.AcneCare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import major.AcneCare.model.Book;
import major.AcneCare.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    // Lấy danh sách tất cả sách
    @GetMapping
    public Object getAllBooks() {
        return bookService.getAllBooks();
    }

    // Lấy danh sách theo id
    @GetMapping("/{id}")
    public Object getBookById(@PathVariable String id) {
        return bookService.getBookById(id);
    }

    // Thêm sách mới
    @PostMapping("/add")
    public String addBook(@RequestBody Book book) {
        bookService.addBook(book);
        return "Thêm sách thành công";
    }

    // Xóa sách theo id
    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return "Xóa sách thành công";
    }
}
