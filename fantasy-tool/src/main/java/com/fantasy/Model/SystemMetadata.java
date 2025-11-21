package com.fantasy.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "system_metadata")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SystemMetadata {
    
    @Id
    @Column (name = "key")
    private String key;

    @Column (name = "value")
    private String value;


    @Override
    public String toString() {
        return String.format("SystemMetadata{key=%s, value=%s}", key, value);
    }
}
