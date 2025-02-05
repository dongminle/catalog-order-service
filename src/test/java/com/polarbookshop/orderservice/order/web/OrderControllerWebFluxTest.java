package com.polarbookshop.orderservice.order.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.polarbookshop.orderservice.order.domain.Order;
import com.polarbookshop.orderservice.order.domain.OrderService;
import com.polarbookshop.orderservice.order.domain.OrderStatus;

import reactor.core.publisher.Mono;

@WebFluxTest(OrderController.class)
class OrderControllerWebFluxTest {

    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private OrderService orderService;

    @Test
    void whenBookNotAvailableThenRejectOrder() {
        var orderRequest = new OrderRequest("1234567890", 3);
        var expectedOrder = Order.buildRejectedOrder(orderRequest.isbn(), orderRequest.quantity());

        given(
            orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity())
        ).willReturn(Mono.just(expectedOrder));

        webClient
            .post()
            .uri("/orders")
            .bodyValue(orderRequest)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(Order.class)
            .value(actualOrder -> {
                assertNotNull(actualOrder);
                assertEquals(actualOrder.status(), OrderStatus.REJECTED);
            });
    }
}
