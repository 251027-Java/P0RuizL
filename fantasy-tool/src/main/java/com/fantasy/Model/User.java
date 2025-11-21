package com.fantasy.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id_num")  
    private long userId;

    @Column(name = "display_name")
    private String displayName;

    // TODO: update this to have team name

    @Override
    public String toString() {
        return "User [userId=" + userId + ", displayName=" + displayName + "]";
    }


}
