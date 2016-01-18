package edu.avans.hartigehap.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import edu.avans.hartigehap.domain.OrderStatus.OrderStatusId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Erco
 */
@Entity
@NamedQuery(name = "Order.findSubmittedOrders", query = "SELECT o FROM Order o "
		+ "WHERE o.orderStatus = edu.avans.hartigehap.domain.Order$OrderStatus.SUBMITTED "
		+ "AND o.bill.diningTable.restaurant = :restaurant " + "ORDER BY o.submittedTime")
// to prevent collision with MySql reserved keyword
@Table(name = "ORDERS")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true, of = { "orderStatus", "orderItems" })

public class Order extends DomainObject {
	private static final long serialVersionUID = 1L;

	//public enum OrderStatus {
	//	CREATED, SUBMITTED, PLANNED, PREPARED, SERVED
	//}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ORDER_ID")
	private Long id;
	
	// unidirectional one-to-many relationship.
	@OneToMany(cascade = javax.persistence.CascadeType.ALL)
	// additional control over database column names (optional) 
	@JoinColumn(name = "ORDERITEM_ORDER_ID", referencedColumnName = "ORDER_ID")
	private Collection<OrderItem> orderItemsList = new ArrayList<OrderItem>();
	
	@Enumerated(EnumType.ORDINAL)
	// represented in database as integer
	private OrderStatus orderStatus;

	@Temporal(TemporalType.TIMESTAMP)
	private Date submittedTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date plannedTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date preparedTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date servedTime;

	// unidirectional one-to-many relationship.
	@OneToMany(cascade = javax.persistence.CascadeType.ALL)
	private Collection<BaseOrderItem> orderItems = new ArrayList<BaseOrderItem>();

	@ManyToOne()
	private Bill bill;

	public Order() {
		orderStatus = new OrderCreatedState(this);
	}

	/* business logic */

	@Transient
	public boolean isSubmittedOrSuccessiveState() {
		return orderStatus.getOrderStatusId() != OrderStatus.OrderStatusId.CREATED;
	}

	// transient annotation, because methods starting with are recognized by JPA
	// as properties
	@Transient
	public boolean isEmpty() {
		return orderItems.isEmpty();
	}

	public void addOrderItem(MenuItem menuItem) {
		Iterator<BaseOrderItem> orderItemIterator = orderItems.iterator();
		boolean found = false;
		while (orderItemIterator.hasNext()) {
			BaseOrderItem orderItem = orderItemIterator.next();
			if (orderItem.getMenuItem().equals(menuItem)) {
				orderItem.incrementQuantity();
				found = true;
				break;
			}
		}
		if (!found) {
			OrderItem orderItem = new OrderItem(menuItem, 1);
			orderItems.add(orderItem);
		}
	}

	public OrderItem addOnlineOrderItem(MenuItem menuItem) {
		OrderItem orderItem = new OrderItem(menuItem, 1);
		orderItems.add(orderItem);

		return orderItem;
	}

	public BaseOrderItem addOnlineOrderOption(BaseOrderItem orderItem, MenuItem orderOption) {
		BaseOrderItem newOrderOption = new OrderOption(orderItem, orderOption, 1);
		orderItems.add(newOrderOption);

		return newOrderOption;
	}

	public void deleteOrderItem(MenuItem menuItem) {
		Iterator<BaseOrderItem> orderItemIterator = orderItems.iterator();
		boolean found = false;
		while (orderItemIterator.hasNext()) {
			BaseOrderItem orderItem = orderItemIterator.next();
			if (orderItem.getMenuItem().equals(menuItem)) {
				found = true;
				if (orderItem.getQuantity() > 1) {
					orderItem.decrementQuantity();
				} else {
					// orderItem.getQuantity() == 1
					orderItemIterator.remove();
				}
				break;
			}
		}
		if (!found) {
			// do nothing
		}
	}

	public void submit() throws StateException {
		orderStatus.submit();
	}

	public void plan() throws StateException {
		orderStatus.plan();
	}

	public void prepared() throws StateException {
		orderStatus.prepare();
	}

	public void served() throws StateException {
		orderStatus.serve();
	}

	@Transient
	public int getPrice() {
		int price = 0;
		Iterator<BaseOrderItem> orderItemIterator = orderItems.iterator();
		while (orderItemIterator.hasNext()) {
			price += orderItemIterator.next().getPrice();
		}
		return price;
	}

}