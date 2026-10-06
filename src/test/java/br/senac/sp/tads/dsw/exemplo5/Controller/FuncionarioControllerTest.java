package br.senac.sp.tads.dsw.exemplo5.Controller;

import br.senac.sp.tads.dsw.exemplo5.model.Departamento;
import br.senac.sp.tads.dsw.exemplo5.model.Funcionario;
import br.senac.sp.tads.dsw.exemplo5.repository.DepartamentoRepository;
import br.senac.sp.tads.dsw.exemplo5.repository.FuncionarioRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

public class FuncionarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Departamento departamentoPadrao;



    @BeforeEach
    void setUp() {
        Departamento departamento = new Departamento();
        departamento.setNome("Departamento Padrão");
        departamento.setOrcamento(10000.00);
        departamentoPadrao = departamentoRepository.save(departamento);
    }


    @Test
    void deveCriarFuncionarioComSucesso() throws Exception {

        // 1 - criando um objeto que queremos enviar
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Filipe Silva do Nascimento");
        funcionario.setDataContratacao(LocalDate.of(2026, 10, 6));
        funcionario.setTrabalhoRemoto(true);
        funcionario.setDepartamento(departamentoPadrao);

        // 2 - Convertendo Objeto JAVA em JSON
        String json = objectMapper.writeValueAsString(funcionario);

        // 3 - Enviando o Post para API
        mockMvc.perform(
            post("/api/funcionarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
        )
        // 4 - checar se a resposta esta sendo enviada com sucesso
        .andExpect(status().isCreated()) // 201
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.nome").value("Filipe Silva do Nascimento"))
        .andExpect(jsonPath("$.dataContratacao").value("2026-10-06"))
        .andExpect(jsonPath("$.trabalhoRemoto").value(true));



    }

    @Test
    void deveRetornarErro400AoCriarFuncionarioComDataFutura() throws Exception { 
        
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Filipe Silva do Nascimento");
        funcionario.setDataContratacao(LocalDate.of(2027, 1, 10));
        funcionario.setTrabalhoRemoto(true);
        funcionario.setDepartamento(departamentoPadrao);

        String json = objectMapper.writeValueAsString(funcionario);

        mockMvc.perform(
            post("/api/funcionarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
        )

        .andExpect(status().isBadRequest()); // 201
    }

    @Test
    void deveListarTodosOsFuncionarios() throws Exception {

        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Filipe Silva do Nascimento");
        funcionario.setDataContratacao(LocalDate.of(2026, 10, 6));
        funcionario.setTrabalhoRemoto(true);
        funcionario.setDepartamento(departamentoPadrao);
        funcionarioRepository.save(funcionario);

        mockMvc.perform(
            get("/api/funcionarios"))
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$[0]").exists()); // verificar se exisite        
    }

    @Test
    void deveBuscarFuncionarioPorId() throws Exception {

        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Filipe Silva do Nascimento");
        funcionario.setDataContratacao(LocalDate.of(2026, 10, 6));
        funcionario.setTrabalhoRemoto(true);
        funcionario.setDepartamento(departamentoPadrao);

        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        mockMvc.perform(get("/api/funcionarios/" + funcionarioSalvo.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.nome").value("Filipe Silva do Nascimento"))
        .andExpect(jsonPath("$.dataContratacao").value("2026-10-06"))
        .andExpect(jsonPath("$.trabalhoRemoto").value(true));

    }

    @Test
    void deveAtualizarFuncionario() throws Exception {
        
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Filipe Silva do Nascimento");
        funcionario.setDataContratacao(LocalDate.of(2026, 10, 6));
        funcionario.setTrabalhoRemoto(true);
        funcionario.setDepartamento(departamentoPadrao);
        funcionarioRepository.save(funcionario);

        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        funcionario.setNome("Filipe Silva Atualizado");
        funcionario.setDataContratacao(LocalDate.of(2024, 10, 6));
        funcionario.setTrabalhoRemoto(false);
        funcionario.setDepartamento(departamentoPadrao);
        funcionarioRepository.save(funcionario);

        String json = objectMapper.writeValueAsString(funcionarioSalvo);

        mockMvc.perform(
            put("/api/funcionarios/" + funcionarioSalvo.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)    
        )
            
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.id").value(funcionarioSalvo.getId()))
    .andExpect(jsonPath("$.nome").value("Filipe Atualizado"))
    .andExpect(jsonPath("$.dataContratacao").value("2024-10-06"))
    .andExpect(jsonPath("$.trabalhoRemoto").value(false));

}

    @Test
    void deveApagarFuncionario() throws Exception {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Filipe Silva do Nascimento");
        funcionario.setDataContratacao(LocalDate.of(2026, 10, 6));
        funcionario.setTrabalhoRemoto(true);
        funcionario.setDepartamento(departamentoPadrao);
        
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        mockMvc.perform(delete("/api/funcionarios/" + funcionarioSalvo.getId()))
        .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/funcionarios/" + funcionarioSalvo.getId()))
        .andExpect(status().isNoContent());



    }
}
