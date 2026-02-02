$(document).ready(function () {
  // Lấy dữ liệu từ API
  $.ajax({
    url: "http://localhost:8080/api/v1/books",
    type: "GET",
    dataType: "json",
    success: function (data) {
      let trHTML = "";
      $.each(data, function (i, item) {
        trHTML =
          trHTML +
          '<tr id="book-' +
          item.id +
          '">' +
          "<td>" +
          item.id +
          "</td>" +
          "<td>" +
          item.title +
          "</td>" +
          "<td>" +
          item.author +
          "</td>" +
          "<td>" +
          item.price +
          "</td>" +
          "<td>" +
          (item.categoryName ? item.categoryName : "None") +
          "</td>" +
          // LƯU Ý: Dòng này viết trong JS sẽ KHÔNG ẩn hiện được theo quyền.
          // Bạn cần dùng: if (isAdmin) { ... } như hướng dẫn trước.
          "<td sec:authorize=\"hasAnyAuthority('ADMIN')\">" +
          '<a href="/books/edit/' +
          item.id +
          '" class="text-primary">Edit</a> | ' +
          '<a href="#" class="text-danger" onclick="apiDeleteBook(' +
          item.id +
          '); return false;" sec:authorize="hasAnyAuthority(\'ADMIN\')">Delete</a>' +
          "</td>" +
          "</tr>";
      });
      $("#book-table-body").append(trHTML);
    },
  });
});

// Hàm xoá sách bằng API
function apiDeleteBook(id) {
  if (confirm("Are you sure you want to delete this book?")) {
    $.ajax({
      url: "http://localhost:8080/api/v1/books/" + id,
      type: "DELETE",
      success: function () {
        alert("Book deleted successfully!");
        $("#book-" + id).remove();
      },
    });
  }
}
