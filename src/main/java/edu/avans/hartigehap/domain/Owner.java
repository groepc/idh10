package edu.avans.hartigehap.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 
 * @author Frans Dubois
 */
@Entity
@Table(name = "OWNERS")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id") 
@Getter @Setter
@ToString(callSuper=true, includeFieldNames=true, of= {"name"})
public class Owner extends DomainObject {
	private static final long serialVersionUID = 1L;

	private String name;
	
	@ManyToMany
	private Collection<Restaurant> restaurants = new ArrayList<Restaurant>();
	
}