package edu.avans.hartigehap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.avans.hartigehap.repository.*;
import edu.avans.hartigehap.service.*;
import edu.avans.hartigehap.domain.*;

@Service("billService")
@Repository
@Transactional(rollbackFor = StateException.class)
public class BillServiceImpl implements BillService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BillRepository billRepository;

    @Transactional(readOnly = true)
    public Bill findById(Long billId) {
        return billRepository.findOne(billId);
    }

    public void billHasBeenPaid(Bill bill) throws StateException {
        bill.paid();
    }

    @Transactional(readOnly = true)
    public List<Bill> findSubmittedBillsForRestaurant(Restaurant restaurant) {
        // a query created using a repository method name
        return billRepository.findByBillStatusAndDiningTableRestaurant(
                Bill.BillStatus.SUBMITTED, restaurant, new Sort(Sort.Direction.ASC, "submittedTime"));
    }
}
