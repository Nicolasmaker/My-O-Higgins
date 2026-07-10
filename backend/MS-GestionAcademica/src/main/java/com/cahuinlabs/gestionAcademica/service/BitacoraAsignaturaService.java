package com.cahuinlabs.gestionAcademica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cahuinlabs.gestionAcademica.models.entities.Asignatura;
import com.cahuinlabs.gestionAcademica.models.entities.BitacoraAsignatura;
import com.cahuinlabs.gestionAcademica.models.request.BitacoraAsignatura.ActualizarBitAsignaturaRequest;
import com.cahuinlabs.gestionAcademica.models.request.BitacoraAsignatura.CrearBitAsignaturaRequest;
import com.cahuinlabs.gestionAcademica.repository.AsignaturaRepository;
import com.cahuinlabs.gestionAcademica.repository.BitacoraAsignaturaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BitacoraAsignaturaService {

    @Autowired
    private BitacoraAsignaturaRepository bitacoraRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    public BitacoraAsignatura crearBitAsignatura(CrearBitAsignaturaRequest request) {

        Asignatura asignatura = asignaturaRepository.findById(request.getIdAsignatura())
                .orElseThrow(() -> new RuntimeException("Error: La Asignatura con ID " + request.getIdAsignatura() + " no existe."));

        BitacoraAsignatura bitacora = new BitacoraAsignatura();
        bitacora.setBitAsiFechaClase(request.getBitAsiFecClase());
        bitacora.setBitAsiActividades(request.getBitAsiActividades());
        bitacora.setBitAsiContenidos(request.getBitAsiContenidos());
        bitacora.setBitAsiObs(request.getBitAsiObs());
        bitacora.setBitAsiObjApren(request.getBitAsiObjApren());
        bitacora.setAsignatura(asignatura);

        return bitacoraRepository.save(bitacora);
    }

    public BitacoraAsignatura actualizarBitAsignatura(Integer id, ActualizarBitAsignaturaRequest request) {

        BitacoraAsignatura bitacoraExistente = bitacoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: La Bitácora con ID " + id + " no existe."));

        bitacoraExistente.setBitAsiFechaClase(request.getBitAsiFecClase());
        bitacoraExistente.setBitAsiActividades(request.getBitAsiActividades());
        bitacoraExistente.setBitAsiContenidos(request.getBitAsiContenidos());
        bitacoraExistente.setBitAsiObs(request.getBitAsiObs());
        bitacoraExistente.setBitAsiObjApren(request.getBitAsiObjApren());

        return bitacoraRepository.save(bitacoraExistente);
    }

    public List<BitacoraAsignatura> listarTodas() {
        return bitacoraRepository.findAll();
    }

    public Optional<BitacoraAsignatura> buscarPorId(Integer id) {
        return bitacoraRepository.findById(id);
    }

    public void eliminar(Integer id) {
        bitacoraRepository.deleteById(id);
    }
}
