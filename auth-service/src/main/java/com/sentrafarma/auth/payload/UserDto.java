package com.sentrafarma.auth.payload;

import com.sentrafarma.auth.entity.Role;
import java.time.LocalDateTime;

public class UserDto {

    private Long id;
    private String email;
    private String namaLengkap;
    private Role role;
    private String noTelepon;
    private LocalDateTime createdAt;

    public UserDto() {}

    public UserDto(Long id, String email, String namaLengkap, Role role, String noTelepon, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.namaLengkap = namaLengkap;
        this.role = role;
        this.noTelepon = noTelepon;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static UserDtoBuilder builder() { return new UserDtoBuilder(); }

    public static class UserDtoBuilder {
        private Long id;
        private String email;
        private String namaLengkap;
        private Role role;
        private String noTelepon;
        private LocalDateTime createdAt;

        public UserDtoBuilder id(Long id) { this.id = id; return this; }
        public UserDtoBuilder email(String email) { this.email = email; return this; }
        public UserDtoBuilder namaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; return this; }
        public UserDtoBuilder role(Role role) { this.role = role; return this; }
        public UserDtoBuilder noTelepon(String noTelepon) { this.noTelepon = noTelepon; return this; }
        public UserDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public UserDto build() {
            return new UserDto(id, email, namaLengkap, role, noTelepon, createdAt);
        }
    }
}
