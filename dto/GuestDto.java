package com.Backend.Projects.AirBnb.dto;

import com.Backend.Projects.AirBnb.entities.User;
import com.Backend.Projects.AirBnb.entities.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
public class GuestDto {

    private Long id;
    private User user;
    private String name;
    private Gender gender;
    private Integer age;
}
