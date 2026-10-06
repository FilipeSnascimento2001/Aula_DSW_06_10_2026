package br.senac.sp.tads.dsw.exemplo5.Controller;

import br.senac.sp.tads.dsw.exemplo5.model.Departamento;
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

    }

    @Test
    void deveRetornarErro400AoCriarFuncionarioComDataFutura() throws Exception {

    }

    @Test
    void deveListarTodosOsFuncionarios() throws Exception {

    }

    @Test
    void deveBuscarFuncionarioPorId() throws Exception {

    }

    @Test
    void deveAtualizarFuncionario() throws Exception {

    }

    @Test
    void deveApagarFuncionario() throws Exception {

    }
}
