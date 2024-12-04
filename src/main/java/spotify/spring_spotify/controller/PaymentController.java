package spotify.spring_spotify.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
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
    public ApiResponse<VNPayResponse> pay(@RequestParam String premiumType, HttpServletRequest request) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Successfully generated VNPay payment URL",
                paymentService.createVnPayPayment(premiumType, request));
    }

    @Hidden
    @GetMapping("/vn-pay-callback")
    public ApiResponse<PremiumResponse> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            return new ApiResponse<>(1000, "Payment successful",
                    paymentService.updatePremium(request));
        } else {
            log.error("Payment failed with response code: " + status);
            return new ApiResponse<>(4000, "Payment failed", null);
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
