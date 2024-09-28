package kr.co.polycube.backendtest.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "lottos")
public class Lotto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number1;
    private int number2;
    private int number3;
    private int number4;
    private int number5;
    private int number6;
}