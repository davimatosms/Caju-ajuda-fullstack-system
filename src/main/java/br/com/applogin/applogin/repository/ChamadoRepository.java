package br.com.applogin.applogin.repository;

import br.com.applogin.applogin.model.Chamado;
import br.com.applogin.applogin.model.Usuario;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ChamadoRepository extends CrudRepository<Chamado, Long> {

    List<Chamado> findByCliente(Usuario cliente);

}