package com.kamar.issuemanagementsystem.ticket.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.security.SecureRandom;

/**
 * sequences for ticket id.
 * @author kamar baraka.*/

@Entity(name = "sequences")
@Data
public class Sequences {

    @Transient
    private final SecureRandom random = new SecureRandom();

    @Id
    @Column(name = "seq_name")
    private String seqName = "Tims";
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sequence")
    private long sequence;
}
