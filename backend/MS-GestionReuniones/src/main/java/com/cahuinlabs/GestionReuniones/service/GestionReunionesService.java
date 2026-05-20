package com.cahuinlabs.GestionReuniones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cahuinlabs.GestionReuniones.models.entities.BitReunionApoderado;
import com.cahuinlabs.GestionReuniones.models.entities.BitReunionGeneral;
import com.cahuinlabs.GestionReuniones.models.entities.BitReunionIndividual;
import com.cahuinlabs.GestionReuniones.models.request.ActualizarApoderadoRequest;
import com.cahuinlabs.GestionReuniones.models.request.ActualizarFirmasRequest;
import com.cahuinlabs.GestionReuniones.models.request.BitReunionApoderadoRequest;
import com.cahuinlabs.GestionReuniones.models.request.ReunionGeneralRequest;
import com.cahuinlabs.GestionReuniones.models.request.ReunionIndividualRequest;

import jakarta.persistence.EntityNotFoundException;
import com.cahuinlabs.GestionReuniones.repository.BitReunionApoderadoRepository;
import com.cahuinlabs.GestionReuniones.repository.BitReunionGeneralRepository;
import com.cahuinlabs.GestionReuniones.repository.BitReunionIndividualRepository;

import jakarta.transaction.Transactional;



@Service
// encargado de toda la logica de negocio para gestionar reuniones
// crea, actualiza y consulta reuniones con apoderados base, individual y general
public class GestionReunionesService {

    @Autowired
    private BitReunionApoderadoRepository baseRepository;

    @Autowired
    private BitReunionIndividualRepository individualRepository;

    @Autowired
    private BitReunionGeneralRepository generalRepository;

    // crea una reunion base con apoderado, guarda fecha, compromisos, observaciones y docente
    @Transactional
    public BitReunionApoderado registrarReunionApoderado(BitReunionApoderadoRequest request) {
        BitReunionApoderado base = new BitReunionApoderado();
        base.setBitReuFec(request.getBitReuFec());
        base.setBitReuCompromisos(request.getBitReuCompromisos());
        base.setBitReuObs(request.getBitReuObs());
        base.setDocenteUsuRut(request.getDocenteUsuRut());
        return baseRepository.save(base);
    }

    // crea una reunion individual docente/inspector con apoderado
    // primero guarda la base, luego los detalles de la entrevista confidencial
    @Transactional
    public BitReunionIndividual registrarReunionIndividual(ReunionIndividualRequest request) {
        //  guardar la bitcora base
        BitReunionApoderado base = new BitReunionApoderado();
        base.setBitReuFec(request.getBitReuFec());
        base.setBitReuCompromisos(request.getBitReuCompromisos());
        base.setBitReuObs(request.getBitReuObs());
        base.setDocenteUsuRut(request.getDocenteUsuRut());
        base = baseRepository.save(base);

        //guardar el desglose confidencial de la entrevista individual
        BitReunionIndividual individual = new BitReunionIndividual();
        individual.setBitReuIndMotivReu(request.getBitReuIndMotivReu());
        individual.setBitReuIndTemTrat(request.getBitReuIndTemTrat());
        individual.setBitReuIndFirmaDoc(0); // Por defecto inicia sin firmar
        individual.setBitReuIndFirmaApo(0);
        individual.setBitReunionApoderado(base);

        return individualRepository.save(individual);
    }

    // crea una reunion general acta de coordinacion con toda la institucion
    // primero guarda la base, luego los detalles de acuerdos institucionales
    @Transactional
    public BitReunionGeneral registrarReunionGeneral(ReunionGeneralRequest request) {
    
        BitReunionApoderado base = new BitReunionApoderado();
        base.setBitReuFec(request.getBitReuFec());
        base.setBitReuCompromisos(request.getBitReuCompromisos());
        base.setBitReuObs(request.getBitReuObs());
        base.setDocenteUsuRut(request.getDocenteUsuRut());
        base = baseRepository.save(base);

        // guardar la bitacora general de acuerdos institucionales
        BitReunionGeneral general = new BitReunionGeneral();
        general.setBitReuGenTipReu(request.getBitReuGenTipReu());
        general.setBitReuGenComunicEmi(request.getBitReuGenComunicEmi());
        general.setBitReuGenAcuerTrat(request.getBitReuGenAcuerTrat());
        general.setBitReuGenObs(request.getBitReuGenObs());
        general.setBitReunionApoderado(base);

        return generalRepository.save(general);
    }

    // obtiene todas las reuniones base registradas con apoderados
    @Transactional
    public List<BitReunionApoderado> listarReunionesApoderado() {
        return baseRepository.findAll();
    }

    // obtiene una reunion base por su identificador
    public BitReunionApoderado obtenerApoderadoPorId(Long idBitReu) {
        return baseRepository.findById(idBitReu)
                .orElseThrow(() -> new EntityNotFoundException("Bitácora no encontrada: " + idBitReu));
    }

    // obtiene todas las reuniones individuales registradas
    @Transactional
    public List<BitReunionIndividual> listarIndividuales() {
        return individualRepository.findAll();
    }

    // obtiene todas las reuniones generales registradas
    @Transactional
    public List<BitReunionGeneral> listarGenerales() {
        return generalRepository.findAll();
    }

    // actualiza estado de firmas cambia de 0 (sin firmar) a 1 (firmado)
    @Transactional
    public BitReunionIndividual actualizarFirmas(Long idBitReuInd, ActualizarFirmasRequest request) {
        BitReunionIndividual individual = individualRepository.findById(idBitReuInd)
                .orElseThrow(() -> new EntityNotFoundException("Entrevista individual no encontrada: " + idBitReuInd));
        individual.setBitReuIndFirmaDoc(request.getFirmaDoc());
        individual.setBitReuIndFirmaApo(request.getFirmaApo());
        return individualRepository.save(individual);
    }

    // actualiza compromisos u observaciones de una reunion sin cambiar fecha ni autor
    @Transactional
    public BitReunionApoderado actualizarApoderado(Long idBitReu, ActualizarApoderadoRequest request) {
        BitReunionApoderado base = baseRepository.findById(idBitReu)
                .orElseThrow(() -> new EntityNotFoundException("Bitácora no encontrada: " + idBitReu));
        base.setBitReuCompromisos(request.getBitReuCompromisos());
        base.setBitReuObs(request.getBitReuObs());
        return baseRepository.save(base);
    }

    // obtiene todas las reuniones registradas por un funcionario/docente especifico por su rut
    public List<BitReunionApoderado> listarPorFuncionario(Long funcionarioRut) {
        return baseRepository.findByDocenteUsuRut(funcionarioRut);
    }

    // obtiene los detalles completos de una reunion individual especifica
    public BitReunionIndividual obtenerDetalleIndividual(Long idBitReuInd) {
        return individualRepository.findById(idBitReuInd)
                .orElseThrow(() -> new EntityNotFoundException("Reunión individual no encontrada: " + idBitReuInd));
    }

    // obtiene los detalles completos de una reunion general especifica
    public BitReunionGeneral obtenerDetalleGeneral(Long idBitReuGen) {
        return generalRepository.findById(idBitReuGen)
                .orElseThrow(() -> new EntityNotFoundException("Reunión general no encontrada: " + idBitReuGen));
    }
}