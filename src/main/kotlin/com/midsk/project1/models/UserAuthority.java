//package com.midsk.project1.models;
//
//import org.springframework.security.core.GrantedAuthority;
//
//import javax.persistence.*;
//
//@Entity
//public class UserAuthority implements GrantedAuthority {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(unique = true,nullable = false,length = 35)
//    private String role;
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public UserAuthority(String role) {
//        this.role = role;
//    }
//
//    public UserAuthority() {
//    }
//
//    @Override
//    public String getAuthority() {
//        return role;
//    }
//
//}
