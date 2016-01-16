package edu.avans.hartigehap.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import edu.avans.hartigehap.service.testutil.AbstractTransactionRollbackTest;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import edu.avans.hartigehap.domain.Owner;
import edu.avans.hartigehap.domain.Restaurant;

public class OwnerServiceTest extends AbstractTransactionRollbackTest {
	private static final String OWNER1 = "Anna";
	private static final String OWNER2 = "Benny";
	private static final String HARTIGEHAP = "HartHap";
	private static final String HMMBURGER = "HmmmBurg";
	private static final String PITPANKOEK = "PitPankoek";

	@Autowired private OwnerService ownerService;
	@Autowired private RestaurantService restaurantService;

	@Test
	public void dummy() {
		// empty - tests configuration of test context.
	}

	@Test
	public void create() {
		// execute
		Owner owner = createOwner(OWNER1);

		// verify
		List<Owner> owners = ownerService.findAll();
		assertNotNull(owners);
		assertTrue("created owner in list", owners.contains(owner));
	}

	@Test
	public void findByName() {
		// prepare
		Owner owner = createOwner(OWNER1);

		// execute
		List<Owner> owners = ownerService.findByName(OWNER1);

		// verify
		assertNotNull(owners);
		assertTrue("created owner in findByName", owners.contains(owner));
		for (Owner o : owners) {
			assertEquals("name", o.getName(), OWNER1);
		}
	}

	@Test
	public void delete() {
		// prepare
		Owner owner = createOwner(OWNER1);
		List<Owner> owners = ownerService.findAll();
		assertNotNull(owners);
		assertTrue("created owner in list", owners.contains(owner));

		// execute
		ownerService.delete(owner.getId());

		// verify
		List<Owner> owners2 = ownerService.findAll();
		assertNotNull(owners2);
		assertFalse("deleted owner not in the list", owners2.contains(owner));
	}

	@Test
	public void update() {
		Owner owner = createOwner(OWNER1);
		owner.setName(OWNER2);

		Owner owner2 = ownerService.findById(owner.getId());
		assertEquals("name", OWNER2, owner2.getName());
	}
	
	@Test
	public void findByRestaurant() {
		// prepare
		Owner owner1 = createOwner(OWNER1);
		Owner owner2 = createOwner(OWNER2);
		Restaurant restaurant1 = createRestaurant(HARTIGEHAP);
		Restaurant restaurant2 = createRestaurant(HMMBURGER);
		Restaurant restaurant3 = createRestaurant(PITPANKOEK);
		addRestaurantToOwner(restaurant1, owner1);
		addRestaurantToOwner(restaurant1, owner2);
		addRestaurantToOwner(restaurant2, owner1);
		addRestaurantToOwner(restaurant3, owner2);
		
		// execute
		List<Owner> rest1_owners = ownerService.findByRestaurant(restaurant1.getId());
		
		// verify
		assertEquals("AnnaBenny", rest1_owners.get(0).getName() + rest1_owners.get(1).getName());
	}
	
	@Test
	public void addRestaurantToOwner() {
		// prepare
		Owner owner1 = createOwner(OWNER1);
		Owner owner2 = createOwner(OWNER2);
		Restaurant restaurant1 = createRestaurant(HARTIGEHAP);
		Restaurant restaurant2 = createRestaurant(HMMBURGER);
		
		// execute
		ownerService.addRestaurantToOwner(restaurant1.getId(), owner1);
		ownerService.addRestaurantToOwner(restaurant1.getId(), owner2);
		
		// verify
		List<Owner> rest1_owners = ownerService.findByRestaurant(restaurant1.getId());
		List<Owner> rest2_owners = ownerService.findByRestaurant(restaurant2.getId());
		assertEquals("AnnaBenny", rest1_owners.get(0).getName()+rest1_owners.get(1).getName());
		assertEquals("restaurant 2 owners", "[]", "" + rest2_owners);
	}
	
	@Test
	public void removeRestaurantFromOwner() {
		// prepare
		Owner owner1 = createOwner(OWNER1);
		Owner owner2 = createOwner(OWNER2);
		Restaurant restaurant1 = createRestaurant(HARTIGEHAP);
		Restaurant restaurant2 = createRestaurant(HMMBURGER);
		Restaurant restaurant3 = createRestaurant(PITPANKOEK);
		addRestaurantToOwner(restaurant1, owner1);
		addRestaurantToOwner(restaurant2, owner1);
		addRestaurantToOwner(restaurant1, owner2);
		addRestaurantToOwner(restaurant3, owner2);

		// execute
		ownerService.removeRestaurantFromOwner(owner1.getId(), restaurant1.getId());
		
		// verify
		List<Owner> rest1_owners = ownerService.findByRestaurant(restaurant1.getId());
		assertEquals("restaurant1 owners", "Benny", rest1_owners.get(0).getName());
		List<Owner> rest2_owners = ownerService.findByRestaurant(restaurant2.getId());
		assertEquals("restaurant2 owners", "Anna", rest2_owners.get(0).getName());
		List<Owner> rest3_owners = ownerService.findByRestaurant(restaurant3.getId());
		assertEquals("restaurant3 owners", "Benny", rest3_owners.get(0).getName());
		assertEquals("owner1 restaurants", "[Restaurant(super=DomainObjectNaturalId(id=HmmmBurg, version=0), menu=Menu(super=DomainObject(id=2, version=0), meals=[], drinks=[], foodCategories=[]), diningTables=[], customers=[], owners=[Owner(super=DomainObject(id=4, version=0), name=Anna)])]", "" + owner1.getRestaurants());
		assertEquals("owner2 restaurants", "[Restaurant(super=DomainObjectNaturalId(id=HartHap, version=0), menu=Menu(super=DomainObject(id=1, version=0), meals=[], drinks=[], foodCategories=[]), diningTables=[], customers=[], owners=[Owner(super=DomainObject(id=4, version=0), name=Anna), Owner(super=DomainObject(id=5, version=0), name=Benny)]), Restaurant(super=DomainObjectNaturalId(id=PitPankoek, version=0), menu=Menu(super=DomainObject(id=3, version=0), meals=[], drinks=[], foodCategories=[]), diningTables=[], customers=[], owners=[Owner(super=DomainObject(id=5, version=0), name=Benny)])]", "" + owner2.getRestaurants());
	}

	private Owner createOwner(String name) {
		Owner owner = new Owner();
		owner.setName(name);
		owner = ownerService.save(owner);
		assertNotNull(owner);
		assertNotNull(owner.getId());
		assertEquals("name", name, owner.getName());
		return owner;
	}
	
	private Restaurant createRestaurant(String name) {
		Restaurant restaurant = new Restaurant(name, "");
		restaurant = restaurantService.save(restaurant);
		assertNotNull(restaurant);
		return restaurant;
	}
	
	private void addRestaurantToOwner(Restaurant restaurant, Owner owner) {
		// verify that owner has been saved
		assertNotNull(owner.getId());
		
		owner.getRestaurants().add(restaurant);
		restaurant.getOwners().add(owner);
		
	}
}