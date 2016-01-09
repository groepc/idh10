package edu.avans.hartigehap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import edu.avans.hartigehap.domain.DiningTable;
import edu.avans.hartigehap.domain.EmptyBillException;
import edu.avans.hartigehap.domain.MenuItem;
import edu.avans.hartigehap.domain.StateException;
import edu.avans.hartigehap.repository.DiningTableRepository;
import edu.avans.hartigehap.repository.MenuItemRepository;
import edu.avans.hartigehap.service.DiningTableService;
import lombok.extern.slf4j.Slf4j;

@Service("diningTableService")
@Repository
@Transactional(rollbackFor = StateException.class)
@Slf4j
public class DiningTableServiceImpl implements DiningTableService {

    @Autowired
    private DiningTableRepository diningTableRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Transactional(readOnly = true)
    public List<DiningTable> findAll() {
        return Lists.newArrayList(diningTableRepository.findAll());
    }

    @Transactional(readOnly = true)
    public DiningTable findById(Long id) {
        return diningTableRepository.findOne(id);
    }

    public DiningTable save(DiningTable diningTable) {
        return diningTableRepository.save(diningTable);
    }

    public void delete(Long id) {
        diningTableRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public Page<DiningTable> findAllByPage(Pageable pageable) {
        return diningTableRepository.findAll(pageable);
    }

    // to be able to follow associations outside the context of a transaction,
    // prefetch the associated entities by traversing the associations
    @Transactional(readOnly = true)
    public DiningTable fetchWarmedUp(Long id) {
        log.info("(fetchWarmedUp) diningTable id: " + id);

        // finding an item using find
        DiningTable diningTable = diningTableRepository.findOne(id);

        // the following code will deliberately cause a null pointer exception,
        // if something is wrong
        log.info("diningTable = " + diningTable.getId());

        diningTable.warmup();

        return diningTable;
    }

    public void addOrderItem(DiningTable diningTable, String menuItemName) {
        MenuItem menuItem = menuItemRepository.findOne(menuItemName);
        diningTable.getCurrentBill().getCurrentOrder().addOrderItem(menuItem);
    }

    public void deleteOrderItem(DiningTable diningTable, String menuItemName) {
        MenuItem menuItem = menuItemRepository.findOne(menuItemName);
        diningTable.getCurrentBill().getCurrentOrder().deleteOrderItem(menuItem);
    }

    public void submitOrder(DiningTable diningTable) throws StateException {
        diningTable.getCurrentBill().submitOrder();

        // for test purposes: to cause a rollback, throw new
        // StateException("boe")
    }

    public void submitBill(DiningTable diningTable) throws StateException, EmptyBillException {
        diningTable.submitBill();
    }
}
