package br.com.applogin.applogin.controller;

import br.com.applogin.applogin.dto.ChamadoDto;
import br.com.applogin.applogin.model.Chamado;
import br.com.applogin.applogin.repository.ChamadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tecnico")
public class TecnicoApiController {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @GetMapping("/chamados")
    public List<ChamadoDto> listarTodosOsChamados() {
        List<Chamado> chamados = (List<Chamado>) chamadoRepository.findAll();
        // Converte a lista de Chamado para uma lista de ChamadoDto
        return chamados.stream()
                .map(ChamadoDto::new)
                .collect(Collectors.toList());
    }
}