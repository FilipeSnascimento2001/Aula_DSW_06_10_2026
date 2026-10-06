package br.senac.sp.tads.dsw.exemplo5.Controller;

import br.senac.sp.tads.dsw.exemplo5.model.Departamento;
import br.senac.sp.tads.dsw.exemplo5.repository.DepartamentoRepository;
import jakarta.transaction.Transactional;
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


@SpringBootTest
@AutoConfigureMockMvc
@Transactional


public class DepartamentoControllerTest {



    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartamentoRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarDepartamentoComSucesso() throws Exception {
        // 1 - Criar um objeto que queremos enviar
        Departamento departamento = new Departamento();
        departamento.setNome( "Turma B - TADS");
        departamento.setOrcamento(3600.00);

        // 2 - Coverte objeto JAVA para JSON
        String json = objectMapper.writeValueAsString(departamento);

        // 3 - Enviar o Post para a API
        mockMvc.perform(
                         post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                // 4 - Checar se a resposta é o que foi enviado
                .andExpect(status().isCreated()) // 201
                .andExpect(jsonPath("$.id").exists()) // verificar se o id existe
                .andExpect(jsonPath("$.nome").value("Turma B - TADS"))
                .andExpect(jsonPath("$.orcamento").value(3600.00));
    }
    @Test
    void deveListarTodosOsDepartamento() throws Exception {

        Departamento departamento = new Departamento();
        departamento.setNome( "Turma B - TADS");
        departamento.setOrcamento(3600.00);
        repository.save(departamento);

        mockMvc.perform(
                get("/api/departamentos"))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$[0]").exists()); // esta verificando se o valor exite dentro da memoria
    }

    @Test
    void deveBuscarDepartamentoPorId() throws Exception {

        Departamento departamento = new Departamento();
        departamento.setNome( "Turma B - TADS");
        departamento.setOrcamento(3600.00);
        Departamento departamentoSalvo = repository.save(departamento);

        mockMvc.perform(get("/api/departamentos/" + departamentoSalvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(departamento.getId()))
                .andExpect(jsonPath("$.nome").value("Turma B - TADS"))
                .andExpect(jsonPath("$.orcamento").value(3600.00));

    }

    @Test
    void deveApagarDepartamento() throws Exception {
        Departamento departamento = new Departamento();
        departamento.setNome( "Turma B - TADS");
        departamento.setOrcamento(3600.00);
        Departamento departamentoSalvo = repository .save(departamento);

    mockMvc.perform(delete("/api/departamentos/" + departamentoSalvo.getId()))
            .andExpect(status().isNoContent());

    mockMvc.perform(delete("/api/departamentos/" + departamentoSalvo.getId()))
            .andExpect(status().isNoContent());


    }

}
