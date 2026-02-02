package fit_hutech_spring.controllers;

import fit_hutech_spring.daos.Item;
import fit_hutech_spring.entities.Book;
import fit_hutech_spring.services.BookService;
import fit_hutech_spring.services.CategoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        public String addBook(
                        @Valid @ModelAttribute("book") Book book,
                        @NotNull BindingResult bindingResult,
                        Model model) {
                if (bindingResult.hasErrors()) {
                        var errors = bindingResult.getAllErrors()
                                        .stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .toArray(String[]::new);
                        model.addAttribute("errors", errors);
                        model.addAttribute("categories",
                                        categoryService.getAllCategories());
                        return "book/add";
                }
                bookService.addBook(book);
                return "redirect:/books";
        }

        // --- UPDATE (Sửa) ---
        @GetMapping("/edit/{id}")
        public String editBookForm(@NotNull Model model, @PathVariable long id) {
                var book = java.util.Optional.ofNullable(bookService.getBookById(id));

                model.addAttribute("book", book.orElseThrow(() -> new IllegalArgumentException("Book not found")));
                model.addAttribute("categories", categoryService.getAllCategories());

                return "book/edit";
        }

        @PostMapping("/edit")
        public String editBook(@Valid @ModelAttribute("book") Book book,
                        @NotNull BindingResult bindingResult,
                        Model model) {
                if (bindingResult.hasErrors()) {
                        var errors = bindingResult.getAllErrors()
                                        .stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .toArray(String[]::new);
                        model.addAttribute("errors", errors);
                        model.addAttribute("categories",
                                        categoryService.getAllCategories());
                        return "book/edit";
                }
                bookService.updateBook(book);
                return "redirect:/books";
        }

        // --- DELETE (Xóa) ---
        @GetMapping("/delete/{id}")
        public String deleteBook(@PathVariable long id) {
                Book book = bookService.getBookById(id);

                if (book != null) {
                        bookService.deleteBook(id);
                } else {
                        throw new IllegalArgumentException("Book not found");
                }

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

        // --- SEARCH (Tìm kiếm sách) ---

        @GetMapping("/search")
        public String searchBook(
                        @NotNull Model model,
                        @RequestParam String keyword,
                        @RequestParam(defaultValue = "0") Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize,
                        @RequestParam(defaultValue = "id") String sortBy) {
                model.addAttribute("books", bookService.searchBook(keyword));
                model.addAttribute("currentPage", pageNo);
                model.addAttribute("totalPages",
                                bookService
                                                .getAllBooks(pageNo, pageSize, sortBy)
                                                .size() / pageSize);
                model.addAttribute("categories",
                                categoryService.getAllCategories());
                return "book/list";
        }
}