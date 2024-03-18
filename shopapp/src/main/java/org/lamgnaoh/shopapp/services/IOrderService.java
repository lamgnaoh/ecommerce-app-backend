package org.lamgnaoh.shopapp.services;

import org.lamgnaoh.shopapp.dtos.OrderDTO;
import org.lamgnaoh.shopapp.exceptions.DataNotFoundException;
import org.lamgnaoh.shopapp.models.Order;
import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    Order getOrder(Long id);
    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId);
}
