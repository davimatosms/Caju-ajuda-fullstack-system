package br.com.applogin.applogin.repository;

import br.com.applogin.applogin.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    Optional<Usuario> findById(Long id);

    @Query(value = "select * from applogin.usuario where email = :email and senha = :senha", nativeQuery = true)
    public Usuario login(String email, String senha);

    Usuario findByEmail(String email);
}
