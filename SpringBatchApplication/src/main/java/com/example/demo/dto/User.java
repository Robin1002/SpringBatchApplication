package com.example.demo.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="UserDetails")
public class User {
    
    @Id
    private int isserMemberId;
    private String oldPan;
    private String oldExpirationDate;
    private String newPan;
    private String newExpirationDate;
    private String reasonCode;
    private String effectiveDate;
    

}
