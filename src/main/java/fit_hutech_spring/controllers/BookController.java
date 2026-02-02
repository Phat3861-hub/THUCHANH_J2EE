package fit_hutech_spring.controllers;

import fit_hutech_spring.daos.Item;
import fit_hutech_spring.entities.Book;
import fit_hutech_spring.services.BookService;
import fit_hutech_spring.services.CategoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
        private final BookService bookService;
        private final CategoryService categoryService;
        private final fit_hutech_spring.services.CartService cartService;

        // --- READ (Xem danh sách) ---
        @GetMapping
        public String showAllBooks(
                        @NotNull Model model,
                        @RequestParam(defaultValue = "0") Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize,
                        @RequestParam(defaultValue = "id") String sortBy) {
                model.addAttribute("books", bookService.getAllBooks(pageNo,
                                pageSize, sortBy));
                model.addAttribute("currentPage", pageNo);
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("totalPages",
                                bookService.getAllBooks(pageNo, pageSize, sortBy).size() / pageSize);
                return "book/list";
        }

        // --- CREATE (Thêm mới) ---
        @GetMapping("/add")
        public String addBookForm(Model model) {
                model.addAttribute("book", new Book());
                model.addAttribute("categories", categoryService.getAllCategories()); // Để chọn danh mục
                return "book/add";
        }

        @PostMapping("/add")
        public String addBook(@ModelAttribute("book") Book book) {
                bookService.addBook(book);
                return "redirect:/books";
        }

        // --- UPDATE (Sửa) ---
        @GetMapping("/edit/{id}")
        public String editBookForm(@PathVariable("id") Long id, Model model) {
                Book book = bookService.getBookById(id);
                if (book != null) {
                        model.addAttribute("book", book);
                        model.addAttribute("categories", categoryService.getAllCategories());
                        return "book/edit";
                }
                return "redirect:/books";
        }

        @PostMapping("/edit")
        public String editBook(@ModelAttribute("book") Book book) {
                bookService.updateBook(book);
                return "redirect:/books";
        }

        // --- DELETE (Xóa) ---
        @GetMapping("/delete/{id}")
        public String deleteBook(@PathVariable("id") Long id) {
                bookService.deleteBook(id);
                return "redirect:/books";
        }

        // --- ADD TO CART ---

        @PostMapping("/add-to-cart")
        public String addToCart(HttpSession session,
                        @RequestParam long id,
                        @RequestParam String name,
                        @RequestParam double price,
                        @RequestParam(defaultValue = "1") int quantity) {
                var cart = cartService.getCart(session);
                cart.addItems(new Item(id, name, price, quantity));
                cartService.updateCart(session, cart);
                return "redirect:/books";
        }
}