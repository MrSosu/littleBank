package com.example.littleBank.controllers;

import com.example.littleBank.entities.Cliente;
import com.example.littleBank.entities.Conto;
import com.example.littleBank.exceptions.ClienteNotFoundException;
import com.example.littleBank.request.CreateContoRequest;
import com.example.littleBank.response.GenericResponse;
import com.example.littleBank.response.GetClienteResponse;
import com.example.littleBank.security.AuthenticationService;
import com.example.littleBank.services.ClienteService;
import jakarta.mail.Multipart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/get/{id}")
    public GetClienteResponse getClienteById(@PathVariable Long id) throws ClienteNotFoundException {
        return clienteService.getClienteById(id);
    }

    @GetMapping("/all")
    @Secured("ADMIN")
    public List<GetClienteResponse> getAllClienti() {
        return clienteService.getAll();
    }

    @PostMapping("/create")
    public GetClienteResponse createCliente(@RequestBody Cliente cliente) {
        return clienteService.createCliente(cliente);
    }

    @PutMapping("/update/{id}")
    public GetClienteResponse updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteService.updateCliente(id, cliente);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCliente(@PathVariable Long id) {
        clienteService.deleteClienteById(id);
    }

    @PostMapping("/open_conto")
    public ResponseEntity<?> openConto(@RequestBody CreateContoRequest createContoRequest) {
        try {
            return new ResponseEntity<>(clienteService.createConto(createContoRequest), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/role")
    @Secured("SUPERADMIN")
    public ResponseEntity<String> updateRole(@RequestParam Long id, @RequestParam String new_role) throws ClienteNotFoundException {
        clienteService.updateRole(id, new_role);
        return new ResponseEntity<>("Ruolo aggiornato con successo", HttpStatus.CREATED);
    }

    @PostMapping("/logout/{id}")
    public void logout(HttpServletRequest httpRequest, @PathVariable Long id) {
        authenticationService.logout(httpRequest, id);
    }

    @PutMapping("/upload_documento/{id}")
    public ResponseEntity<GenericResponse> uploadDocumento(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException, ClienteNotFoundException {
        Path filePath = Paths.get("src/main/resources/documents/" + file.getOriginalFilename());
        // copio il file nella directory
        Files.copy(file.getInputStream(), filePath);
        clienteService.uploadDocumento(id, filePath.toString());
        return new ResponseEntity<>(new GenericResponse("File caricato con successo"), HttpStatus.CREATED);
    }

    @GetMapping("/download_documento/{id}")
    public ResponseEntity<GenericResponse> downloadDocumento(@PathVariable Long id, HttpServletResponse response) throws IOException {
        String pathFile = clienteService.getPath(id);
        Path filePath = Path.of(pathFile);
        String mimeType = Files.probeContentType(filePath);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType); // setto l'header content-type
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filePath.getFileName().toString() + "\"");
        response.setContentLength((int) Files.size(filePath));
        Files.copy(filePath, response.getOutputStream());
        return new ResponseEntity<>(new GenericResponse("file scaricato con successo"), HttpStatus.OK);
    }

}
