package com.senac.aulaFull.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Column(unique = true)
    private String CPF;
    private String senha;
    @Column(unique = true)
    private String email;
    private String telefone;

    private String role;

    @Column(name = "nome_instituicao")
    private String nomeInstituicao;

    private String codigoResetSenha;
    private LocalDateTime codExpiracao;
    private LocalDateTime dataCadastro;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Token> tokens;

    @OneToMany(mappedBy = "adminResponsavel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Curso> cursosCriados = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_curso",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "curso_id"))
    @ToString.Exclude
    private Set<Curso> cursosMatriculados = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if ("ROLE_ADMIN".equals(this.role)) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override public String getPassword() { return this.senha; }
    @Override public String getUsername() { return this.email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}