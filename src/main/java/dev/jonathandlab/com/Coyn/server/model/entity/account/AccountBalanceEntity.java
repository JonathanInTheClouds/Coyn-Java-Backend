package dev.jonathandlab.com.Coyn.server.model.entity.account;

import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class AccountBalanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private AccountEntity account;

    private Double available;

    private Double current;

    private String isoCurrencyCode;

    private String unofficialCurrencyCode;

    private OffsetDateTime lastUpdatedDatetime;

}
