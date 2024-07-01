package com.example.littleBank;

import com.example.littleBank.entities.Cliente;
import com.example.littleBank.entities.Conto;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import com.example.littleBank.exceptions.ClienteNotFoundException;
import com.example.littleBank.exceptions.UserNotConformedException;
import com.example.littleBank.repositories.ClienteRepository;
import com.example.littleBank.request.AuthenticationRequest;
import com.example.littleBank.request.CreateContoRequest;
import com.example.littleBank.request.RegistrationRequest;
import com.example.littleBank.response.AuthenticationResponse;
import com.example.littleBank.response.CreateContoResponse;
import com.example.littleBank.security.AuthenticationService;
import com.example.littleBank.security.JwtService;
import com.example.littleBank.services.ClienteService;
import com.example.littleBank.services.ContoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LittleBankApplicationTests {

	@Autowired
	private ClienteService clienteService;
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private ContoService contoService;

	@Test
	public void dummyTest() {
		System.out.println("sono uno stupido test");

	}

	@Test
	public void testClienteRegister() throws UserNotConformedException {
		RegistrationRequest request = RegistrationRequest.builder()
				.nome("Marco")
				.cognome("Adriani")
				.email("ciccio@gmail.com")
				.codiceFiscale("ABABABABABAfhuufhdufwhufwfw")
				.dataNascita(LocalDate.now())
				.telefono("33365874125")
				.indirizzo("via fasulla 123")
				.comune("Springfield")
				.password("ntoodico")
				.build();
		authenticationService.register(request);
		Cliente cliente = clienteService.findByEmail("ciccio@gmail.com");
		Assertions.assertNotNull(cliente, "il cliente non è null");
		AuthenticationResponse response = authenticationService.authenticate(new AuthenticationRequest("ciccio@gmail.com", "ntoodico"));
		Assertions.assertNotNull(response);
		Assertions.assertTrue(() -> jwtService.isTokenValid(response.getToken()));
	}

	@Test
	@Transactional
	public void testCreateConto() {
		Cliente cliente = clienteService.findByEmail("ciccio@gmail.com");
		List<Long> clienti = new ArrayList<>();
		clienti.add(cliente.getId());
		CreateContoRequest request = CreateContoRequest.builder()
						.costo(7.50)
				        .cash(150000.0)
				        .clienti(clienti)
						.build();
		CreateContoResponse response = clienteService.createConto(request);
		List<Cliente> clientes = response.getId_clienti().stream().map(ciccio -> clienteRepository.findById(ciccio).get()).toList();
		Conto conto = Conto.builder()
				.id(response.getId_conto())
				.clientiConto(clientes)
				.dataConto(response.getDataConto())
				.cash(response.getCash())
				.costo(response.getCosto())
				.build();
		Conto myConto = contoService.getContoById(response.getId_conto());
		Assertions.assertTrue(conto.equals(myConto));
		List<Long> id_clienti_conto1 = conto.getClientiConto().stream().map(Cliente::getId).toList();
		List<Long> id_clienti_conto2 = myConto.getClientiConto().stream().map(Cliente::getId).toList();
		Assertions.assertArrayEquals(id_clienti_conto1.toArray(), id_clienti_conto2.toArray());
	}

}
