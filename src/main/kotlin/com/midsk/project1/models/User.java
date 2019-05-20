//package com.midsk.project1.models;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import javax.persistence.*;
//import javax.validation.constraints.Min;
//import java.io.Serializable;
//import java.util.Collection;
//import java.util.HashSet;
//
//@Entity
//public class User implements Serializable, UserDetails {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(unique = true,nullable = false,length = 35)
//    private String username;
//    @Column(nullable = false,length = 128)
//    private String password;
//
//    @ManyToMany(targetEntity = UserAuthority.class)
//    private Collection<GrantedAuthority> authorities=new HashSet<>();
//
//    public User( String username,  String password) {
//        this.username = username;
//        this.password = password;
//    }
//
//    public User() {
//    }
//
//    @Override
//    public Collection<GrantedAuthority> getAuthorities() {
//        return authorities==null?new HashSet<>():authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return false;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public void setAuthorities(Collection<GrantedAuthority> authorities) {
//        this.authorities = authorities;
//    }
//
//    public boolean addAuthority(GrantedAuthority authority){
//        return authorities.add(authority);
//    }
//    public boolean removeAuthority(GrantedAuthority authority){
//        return authorities.remove(authority);
//    }
//}
