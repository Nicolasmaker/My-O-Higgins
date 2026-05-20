package com.cahuinlabs.autenticacion.models.entities.usuarios;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data //esta wea genera getters, setters, toString, equals y hashCode automaticamente
@Table(name = "usuarios")
@Inheritance(strategy = jakarta.persistence.InheritanceType.JOINED) //esto es para que las clases hijas tengan su propia tabla y se unan con esta tabla por la clave primaria
public class Usuario implements UserDetails{

    @Id
    @Column(name = "usu_rut")
    private Integer usuRut;

    @Column(name = "usu_dv_rut", nullable = false)
    private Character usuDvRut;

    @Column(name = "usu_p_nombre", nullable = false, length = 100)
    private String usuPNombre;

    @Column(name = "usu_s_nombre", length = 100)
    private String usuSNombre;

    @Column(name = "usu_ape_pat", nullable = false, length = 50)
    private String usuApePat;

    @Column(name = "usu_ape_mat", nullable = false, length = 50)
    private String usuApeMat;

    @Column(name = "usu_email", nullable = false, length = 100)
    private String usuEmail;

    @Column(name = "usu_password", nullable = false, length = 90)
    @JsonIgnore
    private String usuPassword;

    @Column(name = "usu_tel", nullable = false, length = 13)
    private String usuTel;

    @Column(name = "usu_estado_actividad", nullable = false)
    private Boolean usuEstadoActividad;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Rol rol; 

 //============================================================================
 //                METODOS DE USERDETAILS DEL SPRING SECURITY
 //============================================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       
        if(this.rol == null){
            return List.of(); //Si el usuario no tiene rol asignado, se devuelve una lista vacia para q no explote el sistema
        }

        return List.of(new SimpleGrantedAuthority(this.rol.getRolNombre())); //Se devuelve una lista con el rol del usuario para que Spring Security pueda manejar la autorizacion
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return usuPassword;
    }

    @Override
    public String getUsername() {
        return usuEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; //Se asume que las cuentas no expiran
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; //Las cuentas no se bloquean por intentos fallidos
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; //La contraseña no caduca
    }

    @Override
    public boolean isEnabled() {
        return usuEstadoActividad; //El estado ahora se conecta con Spring Security para activar o desactivar la cuenta
    }
}
