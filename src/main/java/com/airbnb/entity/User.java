package com.airbnb.entity;

import java.util.List;

//import java.util.List;
import com.airbnb.enums.Role;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
//@Data
@Table(name = "Users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("uId")
	@JsonAlias({"uid", "UId"})
	private Long uId;
	
	@JsonProperty("uName")
	@Column(nullable = false, unique = true)
	private String uName;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String mobile;
	
	@Column(nullable = false)
	private String location;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(nullable = false)
	private String oriPassword;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(nullable = false)
	private String conPassword;
	
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<Property> properties;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column(nullable = false)
	private boolean active = true;

	public Long getuId() {
		return uId;
	}

	public void setuId(Long uId) {
		this.uId = uId;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOriPassword() {
		return oriPassword;
	}

	public void setOriPassword(String oriPassword) {
		this.oriPassword = oriPassword;
	}

	public String getConPassword() {
		return conPassword;
	}

	public void setConPassword(String conPassword) {
		this.conPassword = conPassword;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}

