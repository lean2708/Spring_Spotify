# Sring Spotify
Spring Spotify là một dịch vụ Restful API được phát triển bởi Spring Boot, nhằm cung cấp các chức năng quản lý và phát nhạc tương tự như Spotify.
## Tính Năng
- Quản lý người dùng: Đăng ký (thành công thông báo qua gmail), đăng nhập, quản lý tài khoản
- Quản lý Playlist: Tạo, sửa, xóa và chia sẻ danh sách phát
- Quản lý Artist: Tạo, sửa, xóa, lấy thông tin tất cả nghệ sĩ
- Quản lý Album: Tạo, sửa, xóa, lấy thông tin các Album
- Tìm kiếm: Tìm kiếm bài hát theo tên, nghệ sĩ, hoặc thể loại
## Công Nghệ Sử Dụng
- **Công cụ build**: Maven >= 3.9.5
- **Java**: 17
- **Framework**: Spring Boot 3.2.x
- **Database**: MySQL
## Hướng dẫn sử dụng 
**1. Clone Repository :**
```java
git clone https://github.com/lean2708/Spring_Spotify.git  
cd Spring_Spotify
```
**2. Cấu hình file application.properties :**
- Cấu hình cơ sở dữ liệu MySQL:
```java
spring.datasource.url=jdbc:mysql://localhost:3306/spotify  
spring.datasource.username=<tên người dùng>  
spring.datasource.password=<mật khẩu>
```
- Sửa đường dẫn path cục bộ tương ứng với vị trí bạn muốn lưu file khi upload và download :
```java
anb52.upload-file.base-uri=<đường dẫn>
```
- Cấu hình thông tin email khi bạn cần sử dụng email để thông báo :
```java
spring.mail.username=<email>
spring.mail.password=<mật khẩu ứng dụng cảu email>
```
**3. Hướng Dẫn Sử Dụng Swagger :**
- Sau khi chạy ứng dụng, bạn có thể truy cập Swagger UI tại:
```java
http://localhost:8080/spotify/swagger-ui/index.html
```
- Với URL mặc định để lấy tài liệu API ở dạng JSON của Swagger **(phần Explore)**:
```java
/spotify/v3/api-docs
```
Swagger UI sẽ hiển thị các API và cho phép thử nghiệm các chức năng của ứng dụng
- **Cấu Hình JWT Authentication**
- Để sử dụng các API yêu cầu xác thực, bạn cần thêm JWT token :
- Mở Swagger UI và nhấp vào **Authorize** ở góc trên bên phải
- Nhập token có được sau khi login
- Nhấn **Authorize** để thực hiện được API yêu cầu xác thực
  
**4. Thông tin thanh toán VNPAY :**

| Ngân hàng             | NCB                      |
|-----------------------|--------------------------|
| Số thẻ                | 9704198526191432198      |
| Tên chủ thẻ           | NGUYEN VAN A             |
| Ngày phát hành        | 07/15                    |
| Mật khẩu OTP          | 123456                   |
