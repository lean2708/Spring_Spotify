# Spring My Music
Spring My Music là một dịch vụ Restful API được phát triển bởi Spring Boot, nhằm cung cấp các chức năng quản lý và phát nhạc tương tự như Spotify.
## Tính Năng
- Quản lý người dùng: Đăng ký (thành công thông báo qua gmail), đăng nhập, quên mật khẩu(xác thực bằng mã xác nhận qua gmail), quản lý tài khoản, đăng kí Premium với tích hợp VNPAY(thông báo qua mail)
- Quản lý Playlist: Tạo (kèm upload avatar), sửa, xóa danh sách phát
- Quản lý Artist: Tạo (kèm upload avatar), sửa, xóa, lấy thông tin tất cả nghệ sĩ
- Quản lý Album: Tạo (kèm upload avatar), sửa, xóa, lấy thông tin các Album
- Quản lý Song : Tạo (kèm upload avatar, file song), sửa, xóa, lấy thông tin các Song
- Tìm kiếm: Tìm kiếm theo độ ưu tiên tên artist, playlist, album, song
- Upload / Download file lên AWS
- Kiểm tra ứng dụng bằng UnitTest
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
- Cấu hình thông tin email khi bạn cần sử dụng email để thông báo :
```java
spring.mail.username=<email>
spring.mail.password=<mật khẩu ứng dụng của email>
```
- Cấu hình thông tin AWS :
```java
aws.bucket.name= <your-bucket>
aws.accessKey= <your-access-key>
aws.secretKey= <your-secret-key>
spring.profiles.active=${PROFILE:local}
spring.servlet.multipart.max-file-size= <kích thước max 1 file>
spring.servlet.multipart.max-request-size= <kích thước max 1 request>
```
- Cấu hình thông tin VNPAY :
```java
payment.vnPay.url=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
payment.vnpay.tmnCode= <your-tmn-code>
payment.vnpay.secretKey= <your-secret-key>
payment.vnPay.returnUrl= <URL callback>
payment.vnpay.version= <version VNPAY>
payment.vnpay.command=pay
payment.vnpay.orderType=other
```
**3. Hướng Dẫn Sử Dụng Swagger :**
- **Sau khi chạy ứng dụng, bạn có thể truy cập Swagger UI tại:**
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
  
**4. Mẫu thông tin thanh toán VNPAY (Test) :**

| Ngân hàng             | NCB                      |
|-----------------------|--------------------------|
| Số thẻ                | 9704198526191432198      |
| Tên chủ thẻ           | NGUYEN VAN A             |
| Ngày phát hành        | 07/15                    |
| Mật khẩu OTP          | 123456                   |

**5. Hướng Dẫn Sử Dụng JaCoCo để đo lường mức độ bao phủ mã trong khi chạy UnitTest :**
**- Mở Terminal và chạy lệnh :** 
```java
./mvnw test jacoco:report
```
- Lệnh này sẽ xóa sạch thư mục target, chạy các UnitTest và tạo báo cáo JaCoCo. Báo cáo này sẽ được lưu tại **target/site/jacoco/index.html**
- Sau khi chạy lệnh trên, bạn có thể tìm thấy báo cáo JaCoCo tại folder trong dự án:
```java
target/site/jacoco/index.html
```
- Mở file **index.html** trong trình duyệt để xem báo cáo độ bao phủ mã chi tiết