package com.tickets.viajes.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tickets.viajes.clients.ClienteClient;
import com.tickets.viajes.clients.ConductorClient;
import com.tickets.viajes.clients.ParaderoClient;
import com.tickets.viajes.clients.UserClient;
import com.tickets.viajes.constants.Constants;
import com.tickets.viajes.dto.Cliente;
import com.tickets.viajes.dto.Conductor;
import com.tickets.viajes.dto.ViajeRequest;
import com.tickets.viajes.dto.ViajeResponse;
import com.tickets.viajes.entities.Boleto;
import com.tickets.viajes.entities.Precio;
import com.tickets.viajes.entities.Viaje;
import com.tickets.viajes.event.ViajeParaConductor;
import com.tickets.viajes.event.ViajePlacedEvent;
import com.tickets.viajes.models.ParaderosRequest;
import com.tickets.viajes.models.ResponseService;
import com.tickets.viajes.models.ViajeStatus;
import com.tickets.viajes.repositories.BoletoRepository;
import com.tickets.viajes.repositories.PrecioRepository;
import com.tickets.viajes.repositories.ViajeRepository;
import com.tickets.viajes.services.ViajeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
@RefreshScope
public class ViajeServiceImpl implements ViajeService {
    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private BoletoRepository boletoRepository;

    @Autowired
    private PrecioRepository precioRepository;

    @Autowired
    private final ParaderoClient paraderoClient;

    @Autowired
    private final ClienteClient clienteClient;

    @Autowired
    private final ConductorClient conductorClient;

    @Autowired
    private final UserClient userClient;

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.maintenance.createViaje}")
    private boolean createViaje;

    public ViajeServiceImpl(ParaderoClient paraderoClient, ClienteClient clienteClient, ConductorClient conductorClient, UserClient userClient, RabbitTemplate rabbitTemplate) {
        this.paraderoClient = paraderoClient;
        this.clienteClient = clienteClient;
        this.conductorClient = conductorClient;
        this.userClient = userClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    @Transactional
    public ResponseService createViaje(ViajeRequest request, String authorization, String userId, String userRoles) {
        if (!createViaje) {
            log.info("la creación de viajes está deshabilitada por configuración");
            return new ResponseService(Constants.RESPONSE_TYPE_ERROR, Constants.CREATE_VIAJE_DISABLED, null);
        }
        log.info("se revisa que existan el origen y destino mediante webclient");
        ParaderosRequest paraderosRequest = new ParaderosRequest();
        paraderosRequest.setOrigenId(request.getOrigenId());
        paraderosRequest.setDestinoId(request.getDestinoId());
        ResponseService responseParaderos = paraderoClient.existParaderos(paraderosRequest);
        if (!responseParaderos.getResponseType().equals(Constants.RESPONSE_TYPE_SUCCESS)) {
            log.info("algún paradero no existe");
            return new ResponseService(Constants.RESPONSE_TYPE_ERROR, Constants.PARADEROS_UNKNOWN, null);
        }
        log.info("se revisa que el usuario exista");
        ResponseService responseUser = userClient.getUser(request.getUsuarioId(), authorization, userId, userRoles);
        if (!responseUser.getResponseType().equals(Constants.RESPONSE_TYPE_SUCCESS)) {
            log.info("no existe el usuario, no se puede crear el viaje");
            return new ResponseService(Constants.RESPONSE_TYPE_ERROR, Constants.USER_UNKNOWN, null);
        }
        log.info("se revisa que el cliente exista");
        ResponseService responseCliente = clienteClient.getCliente(request.getClienteId());
        if (!responseCliente.getResponseType().equals(Constants.RESPONSE_TYPE_SUCCESS)) {
            log.info("no existe el cliente, no se puede crear el viaje");
            return new ResponseService(Constants.RESPONSE_TYPE_ERROR, Constants.CLIENT_UNKNOWN, null);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Cliente cliente = objectMapper.convertValue(responseCliente.getData(), Cliente.class);
        log.info("se revisa que el conductor exista");
        ResponseService conductorCliente = conductorClient.getConductor(request.getConductorId());
        if (!conductorCliente.getResponseType().equals(Constants.RESPONSE_TYPE_SUCCESS)) {
            log.info("no existe el conductor, no se puede crear el viaje");
            return new ResponseService(Constants.RESPONSE_TYPE_ERROR, Constants.DRIVER_UNKNOWN, null);
        }
        Conductor conductor = objectMapper.convertValue(conductorCliente.getData(), Conductor.class);
        log.info("se revisa que los lugares sean mayores a 0");
        if (request.getLugares() <= 0) {
            log.info("no se puede crear el viaje, los lugares deben ser mayores a 0");
            return new ResponseService(Constants.RESPONSE_TYPE_ERROR, Constants.PLACES_INVALID, null);
        }
        log.info("se revisa que el tipo de lugar exista");
        Optional<Precio> precioOptional = precioRepository.findById(1);
        if (precioOptional.isEmpty()) {
            log.info("no existe el tipo de lugar, no se puede crear el viaje");
            return new ResponseService(Constants.RESPONSE_TYPE_ERROR, Constants.PLACE_TYPE_UNKNOWN, null);
        }
        Precio precio = precioOptional.get();
        log.info("se comienza a guardar el viaje");
        Viaje viaje = Viaje.builder()
                .origenId(request.getOrigenId())
                .destinoId(request.getDestinoId())
                .lugares(request.getLugares())
                .creado(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("America/Mexico_City"))))
                .estatus(1)
                .usuarioId(request.getUsuarioId())
                .clienteId(request.getClienteId())
                .conductorId(request.getConductorId())
                .status(ViajeStatus.CREADO)
                .build();
        Viaje viajeSaved = viajeRepository.save(viaje);
        ViajeResponse viajeResponse = new ViajeResponse();
        viajeResponse.setId(viajeSaved.getId());
        viajeResponse.setOrigen(responseParaderos.getData().toString().split(";")[0]);
        viajeResponse.setDestino(responseParaderos.getData().toString().split(";")[1]);
        viajeResponse.setLugares(viajeSaved.getLugares());
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(viajeSaved.getCreado().getTime()), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        viajeResponse.setCreado(dateTime.format(formatter));
        viajeResponse.setCliente(cliente.getNombre() + " " + cliente.getApaterno() + " " + cliente.getAmaterno());
        viajeResponse.setConductor(conductor.getNombre() + " " + conductor.getApaterno() + " " + conductor.getAmaterno());
        log.info("se comienzan a guardar los boletos del viaje");
        int lugares = request.getLugares();
        int numero = 0;
        for  (int i = 0; i < lugares; i++) {
            Boleto boleto = Boleto.builder()
                    .numero(++numero)
                    .creado(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("America/Mexico_City"))))
                    .estatus(1)
                    .viaje(viajeSaved)
                    .precio(precio)
                    .build();
            boletoRepository.save(boleto);
        }
        log.info("se comienza a calcular el precio total del viaje");
        float precioTotal = request.getLugares() * Float.parseFloat(precio.getPrecio());
        log.info("se manda el precio al microservicio de pagos. Precio total {}", precioTotal);
        var viajePago = new ViajePlacedEvent(viajeResponse.getId(), request.getClienteId(), request.getUsuarioId(), String.valueOf(precioTotal));
        rabbitTemplate.convertAndSend("viaje-events", "viaje.creado", viajePago); //se establece al routingkey viaje.creado
        ViajeParaConductor mensajeDriver = new ViajeParaConductor(conductor.getId(), conductor.getNombre(), "Viaje creado para el conductor");
        rabbitTemplate.convertAndSend("viaje-events", "viaje.conductor", mensajeDriver);
        log.info("pago enviado a RabbitMQ, se regresa la respuesta");
        return new ResponseService(Constants.RESPONSE_TYPE_SUCCESS, Constants.VIAJE_CREATED, viajeResponse);
    }

    @Override
    @Transactional
    public void updateViaje(Integer idViaje, ViajeStatus status) {
        log.info("se actualiza el viaje debido al pago");
        Optional<Viaje> viajeOpt = viajeRepository.findById(idViaje);
        Viaje viajeSaved = viajeOpt.get();
        viajeSaved.setStatus(status);
        viajeRepository.save(viajeSaved);
        log.info("el viaje ha sido actualizado");
    }
}
