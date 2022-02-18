package dev.jonathandlab.com.Coyn.server.model.entity.account;

import dev.jonathandlab.com.Coyn.server.model.entity.institution.InstitutionEntity;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private InstitutionEntity institution;

    @OneToOne
    private AccountBalanceEntity balance;

    private String accountId;

    private String mask;

    private String name;

    private String officialName;

    private String type;

    private String subType;

    private String verificationStatus;

}
