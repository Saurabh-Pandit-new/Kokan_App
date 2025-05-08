package com.kokanapp.user_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private boolean sellerRequest = false;

    @Enumerated(EnumType.STRING)
    private SellerRequestStatus sellerStatus = SellerRequestStatus.NONE;

    // Added the missing isSeller field
    private boolean isSeller;
    /**
     * 
     */
    private String sellerId;


    public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	// Constructors
    public User() {}

    public User(Long id, String name, String email, String password, String phone,
                Role role, boolean sellerRequest, SellerRequestStatus sellerStatus, boolean isSeller) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.sellerRequest = sellerRequest;
        this.sellerStatus = sellerStatus;
        this.isSeller = isSeller;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isSellerRequest() {
        return sellerRequest;
    }

    // Corrected method name to match the field
    public void setSellerRequest(boolean sellerRequest) {
        this.sellerRequest = sellerRequest;
    }

    public SellerRequestStatus getSellerStatus() {
        return sellerStatus;
    }

    public void setSellerStatus(SellerRequestStatus sellerStatus) {
        this.sellerStatus = sellerStatus;
    }

    // Getter and Setter for isSeller
    public boolean isSeller() {
        return isSeller;
    }

    public void setIsSeller(boolean isSeller) {
        this.isSeller = isSeller;
    }
}
