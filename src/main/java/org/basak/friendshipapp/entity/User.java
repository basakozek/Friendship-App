package org.basak.friendshipapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //@ToString, @EqualsAndHashCode, @Getter, @Setter, @RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true, nullable = false)
    String username;
    String password;
    String email;
    String profilePic;
    Gender gender;
    String phone;
    String address;
    Integer age;
    Integer height;
    Integer weight;
    @Column(columnDefinition = "integer default 0")
    Integer followerCount;
    @Column(columnDefinition = "integer default 0")
    Integer followingCount;
    Boolean isActive;
}
