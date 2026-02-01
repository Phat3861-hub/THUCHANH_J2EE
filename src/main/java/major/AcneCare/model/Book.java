package major.AcneCare.model;

// sử dụng lombok để tự động tạo getter, setter, constructor, toString
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Data 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {
    // thành phần một quyển sách: mã sách, tên sách, tác giả, nhà xuất bản, năm xuất
    // bản, giá tiền
    private String bookId;
    private String title;
    private String author;
    private String publisher;
    private int yearPublished;
    private double price;
}