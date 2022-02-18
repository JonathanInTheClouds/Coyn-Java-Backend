package dev.jonathandlab.com.Coyn.server.model.entity.user;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDeviceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private AppUserEntity appUser;

    private String deviceDetails;

    private String location;

    private Date lastLoggedIn;

}
