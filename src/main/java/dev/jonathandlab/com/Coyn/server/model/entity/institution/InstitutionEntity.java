package dev.jonathandlab.com.Coyn.server.model.entity.institution;

import dev.jonathandlab.com.Coyn.server.model.entity.account.AccountEntity;
import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserEntity;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
//@Table(name = "Institution")
public class InstitutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String generalId;

    private String name;

    private String encryptedAccessToken;

    @ManyToOne
    private AppUserEntity appUser;

    @OneToMany
//    @OneToMany(orphanRemoval = true, mappedBy = "institution")
    @ToString.Exclude
    private Set<AccountEntity> accounts = new HashSet<>();

}
