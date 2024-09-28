package kr.co.polycube.backendtest.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "winners")
public class Winner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lotto_id")
    private Lotto lotto;

    private int rank;
}