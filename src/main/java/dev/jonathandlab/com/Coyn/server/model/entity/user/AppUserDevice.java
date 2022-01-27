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
public class AppUserDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private AppUser appUser;

    private String deviceDetails;

    private String location;

    private Date lastLoggedIn;

}
