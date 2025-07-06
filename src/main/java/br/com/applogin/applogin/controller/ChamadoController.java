package br.com.applogin.applogin.controller;

import br.com.applogin.applogin.model.*;
import br.com.applogin.applogin.repository.AnexoRepository;
import br.com.applogin.applogin.repository.ChamadoRepository;
import br.com.applogin.applogin.repository.UsuarioRepository;
import br.com.applogin.applogin.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/chamados")
public class ChamadoController {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AnexoRepository anexoRepository;

    @Autowired
    private FileStorageService fileStorageService;

    // METODO PARA A PÁGINA "MEUS CHAMADOS"
    @GetMapping("/meus")
    public String listarMeusChamados(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario cliente = usuarioRepository.findByEmail(email);
        List<Chamado> chamados = chamadoRepository.findByCliente(cliente);
        model.addAttribute("chamados", chamados);
        return "meus-chamados";
    }

    // METODO PARA A PÁGINA DE DETALHES
    @GetMapping("/{id}")
    public String verDetalhesDoChamado(@PathVariable("id") Long id, Model model) {
        Optional<Chamado> chamadoOpt = chamadoRepository.findById(id);
        if (chamadoOpt.isEmpty()) {
            return "redirect:/chamados/meus";
        }

        Chamado chamado = chamadoOpt.get();
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!chamado.getCliente().getEmail().equals(emailUsuarioLogado)) {
            return "redirect:/chamados/meus?error=acesso_negado";
        }
        model.addAttribute("chamado", chamado);
        return "detalhes-chamado";
    }

    // METODO PARA MOSTRAR O FORMULÁRIO DE NOVO CHAMADO
    @GetMapping("/novo")
    public String mostrarFormularioNovoChamado(Model model) {
        model.addAttribute("chamado", new Chamado());
        model.addAttribute("prioridades", PrioridadeChamado.values());
        return "novo-chamado";
    }

    // METODO PARA SALVAR O NOVO CHAMADO COM ANEXOS
    @PostMapping
    public String salvarNovoChamado(@ModelAttribute Chamado chamado, @RequestParam("anexosFile") MultipartFile[] anexosFile, RedirectAttributes redirectAttributes) {
        try {
            String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
            Usuario cliente = usuarioRepository.findByEmail(emailUsuarioLogado);
            chamado.setCliente(cliente);
            chamado.setDataCriacao(LocalDateTime.now());
            chamado.setStatus(StatusChamado.ABERTO);
            Chamado chamadoSalvo = chamadoRepository.save(chamado);

            for (MultipartFile file : anexosFile) {
                if (file != null && !file.isEmpty()) {
                    String nomeUnico = fileStorageService.storeFile(file);
                    Anexo anexo = new Anexo();
                    anexo.setNomeArquivo(file.getOriginalFilename());
                    anexo.setTipoArquivo(file.getContentType());
                    anexo.setNomeUnico(nomeUnico);
                    anexo.setChamado(chamadoSalvo);
                    anexoRepository.save(anexo);
                }
            }
            redirectAttributes.addFlashAttribute("success_message", "Chamado #" + chamadoSalvo.getId() + " aberto com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error_message", "Erro ao abrir o chamado: " + e.getMessage());
        }
        return "redirect:/chamados/meus";
    }

    // MÉTODOS PARA EDITAR O CHAMADO (sem anexos por enquanto)
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicao(@PathVariable("id") Long id, Model model) {
        Optional<Chamado> chamadoOpt = chamadoRepository.findById(id);
        if (chamadoOpt.isEmpty()) { return "redirect:/chamados/meus"; }

        Chamado chamado = chamadoOpt.get();
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!chamado.getCliente().getEmail().equals(emailUsuarioLogado)) {
            return "redirect:/chamados/meus?error=acesso_negado";
        }
        model.addAttribute("chamado", chamado);
        model.addAttribute("prioridades", PrioridadeChamado.values());
        return "editar-chamado";
    }

    // Em ChamadoController.java

    @PostMapping("/{id}/editar")
    public String salvarEdicaoChamado(@PathVariable("id") Long id,
                                      @ModelAttribute("chamado") Chamado chamadoAtualizado,
                                      @RequestParam("anexosFile") MultipartFile[] anexosFile, // Parâmetro adicionado
                                      RedirectAttributes redirectAttributes) {

        Optional<Chamado> chamadoOpt = chamadoRepository.findById(id);
        if (chamadoOpt.isEmpty()) {
            return "redirect:/chamados/meus";
        }

        Chamado chamadoOriginal = chamadoOpt.get();
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!chamadoOriginal.getCliente().getEmail().equals(emailUsuarioLogado)) {
            return "redirect:/chamados/meus?error=acesso_negado";
        }

        // Atualiza os campos de texto
        chamadoOriginal.setTitulo(chamadoAtualizado.getTitulo());
        chamadoOriginal.setDescricao(chamadoAtualizado.getDescricao());
        chamadoOriginal.setPrioridade(chamadoAtualizado.getPrioridade());

        // Salva as alterações de texto primeiro
        chamadoRepository.save(chamadoOriginal);

        // Loop para processar e salvar os NOVOS anexos
        for (MultipartFile file : anexosFile) {
            if (file != null && !file.isEmpty()) {
                String nomeUnico = fileStorageService.storeFile(file);
                Anexo anexo = new Anexo();
                anexo.setNomeArquivo(file.getOriginalFilename());
                anexo.setTipoArquivo(file.getContentType());
                anexo.setNomeUnico(nomeUnico);
                anexo.setChamado(chamadoOriginal); // Associa ao chamado existente
                anexoRepository.save(anexo);
            }
        }

        redirectAttributes.addFlashAttribute("success_message", "Chamado #" + id + " atualizado com sucesso!");
        return "redirect:/chamados/" + id;
    }
    // NOVO METODO PARA LIDAR COM DOWNLOAD DE ANEXOS
    @GetMapping("/anexos/{filename:.+}")
    @ResponseBody // Retorna os dados do arquivo diretamente, não uma página
    public ResponseEntity<Resource> downloadAnexo(@PathVariable String filename) {
        Resource file = fileStorageService.loadFileAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping("/{id}/adicionar-anexos")
    public String adicionarAnexos(@PathVariable("id") Long chamadoId,
                                  @RequestParam("anexosFile") MultipartFile[] anexosFile,
                                  RedirectAttributes redirectAttributes) {

        Optional<Chamado> chamadoOpt = chamadoRepository.findById(chamadoId);
        if (chamadoOpt.isEmpty()) {
            return "redirect:/chamados/meus?error=nao_encontrado";
        }

        // Lógica de segurança para garantir que só o dono pode anexar
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!chamadoOpt.get().getCliente().getEmail().equals(emailUsuarioLogado)){
            return "redirect:/chamados/meus?error=acesso_negado";
        }

        for (MultipartFile file : anexosFile) {
            if (file != null && !file.isEmpty()) {
                String nomeUnico = fileStorageService.storeFile(file);
                Anexo anexo = new Anexo();
                anexo.setNomeArquivo(file.getOriginalFilename());
                anexo.setTipoArquivo(file.getContentType());
                anexo.setNomeUnico(nomeUnico);
                anexo.setChamado(chamadoOpt.get());
                anexoRepository.save(anexo);
            }
        }

        redirectAttributes.addFlashAttribute("success_message", "Novos arquivos anexados com sucesso!");
        return "redirect:/chamados/" + chamadoId;
    }

}