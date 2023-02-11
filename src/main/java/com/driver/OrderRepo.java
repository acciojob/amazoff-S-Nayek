package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepo {
     private HashMap<String,Order> oMap;
     private HashMap<String,DeliveryPartner> pMap;
     private HashMap<String, List<String>> o_P_pair;
     private HashMap<String,Order> unAssignOrder;

    public OrderRepo(HashMap<String, Order> oMap, HashMap<String, DeliveryPartner> pMap, HashMap<String, List<String>> o_P_pair,HashMap<String,Order> unAssignOrder) {
        this.oMap = new HashMap<>();
        this.pMap = new HashMap<>();
        this.o_P_pair = new HashMap<>();
        this.unAssignOrder =new HashMap<>();
    }

    public void addOrder(Order order) {
        String oid=order.getId();
        oMap.put(oid,order);
        unAssignOrder.put(oid,order);
    }

    public void addPartner(DeliveryPartner partner) {
        String id= partner.getId();
        pMap.put(id,partner);
        
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(oMap.containsKey(orderId) && pMap.containsKey(partnerId)){
            List<String> orders= new ArrayList<>();
            if(o_P_pair.containsKey(partnerId))
            {
                orders= o_P_pair.get(partnerId);
            }
            orders.add(orderId);
            o_P_pair.put(partnerId,orders);

            //Incrementing noOfOrders of delivery partner
            DeliveryPartner partner= pMap.get(partnerId);
            partner.setNumberOfOrders(partner.getNumberOfOrders()+1);

            //Removing order from unassigned orders db
            if(unAssignOrder.containsKey(orderId))
                unAssignOrder.remove(orderId);
        }
    }

    public Order getOrderById(String orderId) {
        Order order= oMap.get(orderId);
        return order;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        DeliveryPartner partner=pMap.get(partnerId);
        return partner;
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        List<String> orders= new ArrayList<>();
        if(o_P_pair.containsKey(partnerId))
        {
            orders= o_P_pair.get(partnerId);
        }
        return orders.size();
    }

    public void deletePartnerById(String partnerId) {
        List<String> orders= new ArrayList<>();
        //adding all orders of delivery partners in unassigned orders db
        if(o_P_pair.containsKey(partnerId))
        {
            orders= o_P_pair.get(partnerId);
            for(int i=0; i<orders.size(); i++)
            {
                Order order= oMap.get(orders.get(i));
                String id= order.getId();
                unAssignOrder.put(id,order);
            }
        }
        //removing delivery partner from order delivery partner pair db
        o_P_pair.remove(partnerId);

        //removing delivery partner from delivery partner db
        pMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        if(oMap.containsKey(orderId))
            oMap.remove(orderId);

        //Removing order from delivery partner order pair
        for(String partnerId: o_P_pair.keySet())
        {
            List<String> orders= o_P_pair.get(partnerId);
            if(orders.contains(orderId))
            {
                orders.remove(orderId);
                return;
            }
        }

        //if order is not assigned then removing from unassigned db
        if(unAssignOrder.containsKey(orderId))
        {
            unAssignOrder.remove(orderId);
        }
    }

    public List<String> getAllOrders() {
        List<String> orders= new ArrayList<>();
        for(Order order: oMap.values())
        {
            orders.add(order.getId());
        }
        return orders;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> orders= new ArrayList<>();
        if(o_P_pair.containsKey(partnerId))
        {
            orders= o_P_pair.get(partnerId);
        }
        return orders;
    }

    public Integer getCountOfUnassignedOrders() {
        return unAssignOrder.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int deliveryTime, String partnerId) {
        List<String> orders= new ArrayList<>();
        int count=0;
        if(o_P_pair.containsKey(partnerId))
        {
            orders= o_P_pair.get(partnerId);
            for(int i=0; i<orders.size(); i++)
            {
                Order order= oMap.get(orders.get(i));
                if(order.getDeliveryTime()>deliveryTime)
                    count++;
            }
        }
        return count;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> orders= new ArrayList<>();
        int max_time=0;
        if(o_P_pair.containsKey(partnerId))
        {
            orders= o_P_pair.get(partnerId);
            for(int i=0; i<orders.size(); i++)
            {
                Order order= oMap.get(orders.get(i));
                if(order.getDeliveryTime()>max_time)
                    max_time=order.getDeliveryTime();
            }
        }
        return max_time;
    }
}
