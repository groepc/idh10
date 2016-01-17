package edu.avans.hartigehap.service.testutil;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

// http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/
// 5.6 Testing Improvements
// @Rollback may now be used to configure class-level default rollback semantics.
// Consequently, @TransactionConfiguration is now deprecated and will be removed in 
// a subsequent release.

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-root-context.xml")
@Transactional
public abstract class AbstractTransactionRollbackTest extends AbstractTransactionalJUnit4SpringContextTests {

}
