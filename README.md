# 📝 VPP Management System

**Hệ quản lý cửa hàng Văn phòng phẩm (VPP)** *Ứng dụng Desktop được xây dựng trên nền tảng Java Swing và cơ sở dữ liệu MySQL.*

---

## 🚀 Tổng quan dự án
Ứng dụng cung cấp giải pháp quản lý toàn diện cho một cửa hàng văn phòng phẩm, từ khâu quản lý kho (Sản phẩm), vận hành bán hàng đến thống kê doanh thu. Hệ thống sử dụng kiến trúc phân tách giữa giao diện (View) và xử lý dữ liệu (Database/Logic).

### ✨ Tính năng chính
- **Xác thực:** Đăng nhập, Đăng ký người dùng.
- **Bán hàng:** Giao diện chọn hàng linh hoạt, tính toán giỏ hàng và thanh toán.
- **Kho hàng:** Quản lý danh mục sản phẩm (Thêm, Sửa, Xóa, Tìm kiếm).
- **Hóa đơn:** Lưu trữ và truy xuất lịch sử giao dịch chi tiết.
- **Thống kê:** Biểu diễn số liệu doanh thu và hiệu quả kinh doanh.

---

## 🛠 Công nghệ sử dụng
| Thành phần | Công nghệ |
| :--- | :--- |
| **Ngôn ngữ** | Java (JDK 8, 11 hoặc 17) |
| **Giao diện** | Java Swing, AWT |
| **Cơ sở dữ liệu** | MySQL 8.x |
| **Kết nối** | JDBC (MySQL Connector/J) |
| **Công cụ** | NetBeans Project / Ant |

---

## 📐 Kiến trúc Layout & Giao diện
Hệ thống được thiết kế theo cấu trúc **Single-Frame Interface**, giúp người dùng không bị rối mắt bởi quá nhiều cửa sổ con.



### 1. Phân cấp Layout (Layout Hierarchy)
* **MainUI (JFrame):** Sử dụng `BorderLayout` làm khung xương chính.
    * **Vùng WEST (Sidebar):** Sử dụng `GridLayout` để sắp xếp các nút Menu đều nhau theo chiều dọc.
    * **Vùng CENTER (Content Area):** Một `JPanel` trung tâm đóng vai trò là "sân khấu" để hiển thị các chức năng.
* **Các Panel Chức năng (JPanel):**
    * **Sanphampanel / Hoadonpanel:** Kết hợp `BorderLayout`. Phía `NORTH` là thanh tìm kiếm, `CENTER` là `JScrollPane` chứa `JTable`, và `SOUTH` là các nút thao tác.
    * **Banhangpanel:** Sử dụng `GridBagLayout` hoặc kết hợp nhiều `JPanel` để chia vùng chọn sản phẩm và vùng giỏ hàng.

### 2. Cơ chế Chuyển đổi Panel (Dynamic Content)
Thay vì mở cửa sổ mới, `MainUI` thực hiện hoán đổi nội dung thông qua phương thức `chuyenPanel(JPanel panelMoi)`:
1.  Gọi `containerPanel.removeAll()`.
2.  `containerPanel.add(panelMoi, BorderLayout.CENTER)`.
3.  Gọi `revalidate()` và `repaint()` để cập nhật UI ngay lập tức.

---

## ⚙️ Cài đặt & Cấu hình

### 1. Chuẩn bị
* Cài đặt **MySQL Server** và tạo tài khoản quản trị.
* Thêm file `mysql-connector-j-*.jar` vào Libraries của project.

### 2. Cấu hình Database
* Mở tệp: `src/VPP/database/ketnoidb.java`.
* Thay đổi `username` và `password` phù hợp với máy cá nhân.
* **Cơ chế Tự động:** Khi ứng dụng khởi chạy, `static block` sẽ gọi `khoiTaoDB()` để tự động tạo Database `vppmanager` và các bảng (`Products`, `Account`, `HoaDon`...) cùng dữ liệu mẫu nếu chưa tồn tại.

### 3. Khởi chạy
* **EntryPoint:** `src/VPP/main/Main.java` -> Mở màn hình đăng nhập.
* **Tài khoản mặc định:** (Sau khi chạy lần đầu, hệ thống sẽ tự chèn một số tài khoản mẫu vào bảng `account`).

---

## 📂 Cấu trúc thư mục
```
src/
 └─ VPP/
     ├─ main/          # Entry point (Main.java)
     ├─ database/      # Quản lý JDBC và các lớp thực thi SQL (ketnoidb, DBLogin...)
     ├─ View/          # Toàn bộ giao diện (GUI)
     │   ├─ login/     # LoginFrame, SignUpFrame
     │   └─ mainUI/    # MainUI và các Panel con (Trangchu, Sanpham, Banhang...)
     └─ image/         # Icon và hình ảnh minh họa cho giao diện
```
## 🔄 Luồng xử lý chính (Workflow)

Hệ thống vận hành dựa trên sự phối hợp chặt chẽ giữa giao diện người dùng và lớp cơ sở dữ liệu:

1. **Xác thực người dùng:** - `LoginFrame` tiếp nhận thông tin -> Chuyển dữ liệu qua `DBLogin` để kiểm tra với MySQL -> Nếu khớp, giải phóng bộ nhớ `LoginFrame` và khởi tạo `MainUI`.

2. **Quản lý hàng hóa (CRUD):** - Người dùng thao tác trên `JTable` (Thêm/Sửa/Xóa) -> Gọi các phương thức thực thi SQL trong lớp Database -> Sau khi DB phản hồi thành công, gọi `loadData()` để cập nhật lại `DefaultTableModel`.

3. **Bán hàng & Thanh toán:**
   - **Chọn hàng:** Sản phẩm được chọn từ danh sách sẽ được thêm vào một mảng tạm (Giỏ hàng) hiển thị trên UI.
   - **Xác nhận thanh toán:** - Tạo một bản ghi mới trong bảng `HoaDon` (lấy ID hóa đơn vừa tạo).
     - Duyệt giỏ hàng để tạo hàng loạt bản ghi tương ứng trong bảng `ChiTietHoaDon`.
     - **Cập nhật kho:** Hệ thống tự động thực hiện lệnh `UPDATE` để trừ số lượng tồn kho (`soluong`) trong bảng `Products`.

---

## ⚠️ Lưu ý & Xử lý sự cố

> [!IMPORTANT]
> **Kết nối cơ sở dữ liệu:** Nếu ứng dụng báo lỗi kết nối, hãy đảm bảo dịch vụ MySQL đang chạy và Port mặc định là **3306**. Kiểm tra lại thông tin `user` và `password` trong file `ketnoidb.java`.

