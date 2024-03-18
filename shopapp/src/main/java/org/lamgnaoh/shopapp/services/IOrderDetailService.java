package org.lamgnaoh.shopapp.services;

import org.lamgnaoh.shopapp.dtos.OrderDetailDTO;
import org.lamgnaoh.shopapp.exceptions.DataNotFoundException;
import org.lamgnaoh.shopapp.models.OrderDetail;
import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData)
            throws DataNotFoundException;
    void deleteById(Long id);
    List<OrderDetail> findByOrderId(Long orderId);


}
