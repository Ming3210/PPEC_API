package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.PaymentRequestDTO;
import com.ra.base_spring_boot.dto.response.PaymentResponseDTO;
import com.paypal.base.rest.PayPalRESTException;

public interface IPayPalService {
    PaymentResponseDTO createPaypalPayment(PaymentRequestDTO dto) throws PayPalRESTException;

    String executePaypalPayment(String paymentId, String payerId) throws PayPalRESTException;
}
