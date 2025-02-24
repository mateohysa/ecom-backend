package com.mateo.ecom.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true)
  private Long id;

  @Column(name = "address_line_1", nullable = false)
  private String addressLine1;

  @Column(name = "address_line_2")
  private String addressLine2;

  @Column(name = "city", nullable = false)
  private String city;

  @Column(name = "country", nullable = false)
  private String country;

  @Column(name = "post_code", nullable = false)
  private String postCode;

  @JsonIgnore
  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private AppUser user;

  public AppUser getUser() {
    return user;
  }

  public void setUser(AppUser user) {
    this.user = user;
  }

  public String getPostCode() {
    return postCode;
  }

  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Address{" +
            "id=" + id +
            ", addressLine1='" + addressLine1 + '\'' +
            ", addressLine2='" + addressLine2 + '\'' +
            ", city='" + city + '\'' +
            ", country='" + country + '\'' +
            ", postCode='" + postCode + '\'' +
            ", user=" + user +
            '}';
  }
}
