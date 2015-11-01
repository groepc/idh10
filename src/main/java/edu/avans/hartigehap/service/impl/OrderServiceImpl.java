package edu.avans.hartigehap.service.impl;

import java.util.List;
import java.util.ListIterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.avans.hartigehap.service.*;
import edu.avans.hartigehap.domain.*;
import edu.avans.hartigehap.repository.OrderRepository;

@Service("orderService")
@Repository
@Transactional(rollbackFor = StateException.class)
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Order findById(Long orderId) {
        return orderRepository.findOne(orderId);
    }

    // find all submitted orders (so for complete restaurant), ordered by submit
    // time
    // this method serves as an example of:
    // * a named query (using entityManager)
    // * a query created using a repository method name
    // * a repository with a custom method implementation
    @Transactional(readOnly = true)
    public List<Order> findSubmittedOrdersForRestaurant(Restaurant restaurant) {

        // a repository with a custom method implementation
        // the custom method implementation uses a named query which is
        // invoked using an entityManager
        List<Order> submittedOrdersList = orderRepository.findSubmittedOrdersForRestaurant(restaurant);

        log.info("findSubmittedOrdersForRestaurant using named query");
        ListIterator<Order> it = submittedOrdersList.listIterator();
        while (it.hasNext()) {
            Order order = it.next();
            log.info("submittedOrder = " + order.getId() + ", for table = " + order.getBill().getDiningTable().getId()
                    + ", submitted time = " + order.getSubmittedTime());
        }

        // a query created using a repository method name
        List<Order> submittedOrdersListAlternative = orderRepository.findByOrderStatusAndBillDiningTableRestaurant(
                Order.OrderStatus.SUBMITTED, restaurant, new Sort(Sort.Direction.ASC, "submittedTime"));

        log.info("findSubmittedOrdersForRestaurant using query created using repository method name");
        ListIterator<Order> italt = submittedOrdersListAlternative.listIterator();
        while (italt.hasNext()) {
            Order order = italt.next();
            log.info("submittedOrder = " + order.getId() + ", for table = " + order.getBill().getDiningTable().getId()
                    + ", submitted time = " + order.getSubmittedTime());
        }

        return submittedOrdersList;
    }

    @Transactional(readOnly = true)
    public List<Order> findPlannedOrdersForRestaurant(Restaurant restaurant) {
        // a query created using a repository method name
        return orderRepository.findByOrderStatusAndBillDiningTableRestaurant(
                Order.OrderStatus.PLANNED, restaurant, new Sort(Sort.Direction.ASC, "plannedTime"));
    }

    @Transactional(readOnly = true)
    public List<Order> findPreparedOrdersForRestaurant(Restaurant restaurant) {
        // a query created using a repository method name
        return orderRepository.findByOrderStatusAndBillDiningTableRestaurant(
                Order.OrderStatus.PREPARED, restaurant, new Sort(Sort.Direction.ASC, "preparedTime"));
    }

    public void planOrder(Order order) throws StateException {
        order.plan();
    }

    public void orderPrepared(Order order) throws StateException {
        order.prepared();
    }

    public void orderServed(Order order) throws StateException {
        order.served();
    }
}
