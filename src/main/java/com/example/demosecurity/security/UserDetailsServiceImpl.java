package com.example.demosecurity.security;

import com.example.demosecurity.models.UserEntity;
import com.example.demosecurity.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    // Inyectamos nuestro repository para
    // buscar usuarios en la base de datos
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // Busca el usuario por username
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "El usuario " + username + " no existe."
                ));

        // Convierte los roles del usuario en authorities
        // Ejemplo: ROLE_ADMIN, ROLE_USER
        List<SimpleGrantedAuthority> authorities = userEntity.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(
                        "ROLE_" + role.getRoleName().name()
                ))
                .toList();

        // El metodo debe devolver un UserDetails
        //
        // Nosotros tenemos nuestro usuario representado
        // con la entidad UserEntity
        //
        // Una forma simple de cumplir con el tipo de retorno
        // es utilizar la clase User de Spring Security,
        // que ya implementa la interface UserDetails
        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                authorities
        );
    }
}

/*
    ¿Qué es UserDetails?

    UserDetails es una interface de Spring Security
    que representa a un usuario autenticado.

    Define métodos como:

        getUsername()
        getPassword()
        getAuthorities()

    Spring Security utiliza esos métodos para:

    - obtener el username
    - obtener la contraseña
    - obtener los roles/permisos del usuario

    La clase User de Spring Security ya implementa
    esta interface.

    Por eso podemos usarla directamente.

    También podríamos crear nuestra propia clase
    CustomUserDetails si necesitáramos atributos
    extra, por ejemplo:

    - id
    - email
    - nombre completo
*/