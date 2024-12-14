package spotify.spring_spotify.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spotify.spring_spotify.dto.response.ApiResponse;
import spotify.spring_spotify.dto.response.PremiumResponse;
import spotify.spring_spotify.dto.response.SongResponse;
import spotify.spring_spotify.dto.response.VNPayResponse;
import spotify.spring_spotify.service.PaymentService;

@RestController
@RequestMapping("/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    @GetMapping("/vn-pay")
    public ApiResponse<VNPayResponse> pay(@RequestParam @NotBlank String premiumType, HttpServletRequest request) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Tạo thành công URL thanh toán VNPay",
                paymentService.createVnPayPayment(premiumType, request));
    }

    @Hidden
    @GetMapping("/vn-pay-callback")
    public ApiResponse<PremiumResponse> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            return new ApiResponse<>(1000, "Thanh toán thành công",
                    paymentService.updatePremium(request));
        } else {
            log.error("Thanh toán không thành công với mã phản hồi: " + status);
            return new ApiResponse<>(4000, "Thanh toán thất bại", null);
        }
    }

    @GetMapping("/premium-status")
    public ApiResponse<PremiumResponse> checkPremiumStatus(HttpServletRequest request){
        return ApiResponse.<PremiumResponse>builder()
                .code(HttpStatus.OK.value())
                .result(paymentService.checkPremiumStatus(request))
                .message("Premium Status")
                .build();
    }
}
