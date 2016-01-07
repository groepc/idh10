package edu.avans.hartigehap.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.avans.hartigehap.domain.*;
import edu.avans.hartigehap.repository.*;
import edu.avans.hartigehap.service.*;
import com.google.common.collect.Lists;

@Service("foodService")
@Repository
@Transactional
public class BaseFoodServiceImpl implements BaseFoodService {

    @Autowired
    private BaseFoodRepository foodRepository;

    @Transactional(readOnly = true)
    public List<BaseFood> findAll() {
        // MySQL and H2 return the restaurants of findAll() in different order
        // sorting the result makes the behavior less database vendor dependent
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        return Lists.newArrayList(foodRepository.findAll(sort));
    }

}
