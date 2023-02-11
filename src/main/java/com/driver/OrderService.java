package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepo orderRepo;

    public void addOrder(Order order) {
        orderRepo.addOrder(order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner partner =new DeliveryPartner(partnerId);
        orderRepo.addPartner(partner);
    }

    public void addPartnerOrderPair(String orderId, String partnerId) {
        orderRepo.addOrderPartnerPair(orderId,partnerId);
    }

    public Order getOrderById(String orderId) {

              return  orderRepo.getOrderById(orderId);

    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return orderRepo.getPartnerById(partnerId);

    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return orderRepo.getOrderCountByPartnerId(partnerId);
    }

    public List<String> getAllOrders() {
        return orderRepo.getAllOrders();
    }

    public Integer getCountOfUnassignedOrders() {
        return orderRepo.getCountOfUnassignedOrders();

    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int deliveryTime= Integer.parseInt(time.substring(0,2))*60+Integer.parseInt(time.substring(3,5));
        return orderRepo.getOrdersLeftAfterGivenTimeByPartnerId(deliveryTime, partnerId);

    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int deliveryTime= orderRepo.getLastDeliveryTimeByPartnerId(partnerId);
        String hours= Integer.toString(deliveryTime/60);
        if(hours.length()==1)
            hours='0'+hours;
        String mins= Integer.toString(deliveryTime%60);
        if(mins.length()==1)
            mins='0'+mins;
        String time= hours+':'+mins;
        return time;
    }

    public void deletePartnerById(String partnerId) {
        orderRepo.deletePartnerById(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderRepo.deleteOrderById(orderId);
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return orderRepo.getOrdersByPartnerId(partnerId);

    }
}
