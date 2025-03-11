package com.SOOFT.ChallengeBackendSOOFT.infrastructure.outputAdapter.persistence.repository;

import com.SOOFT.ChallengeBackendSOOFT.domain.model.Empresa;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.out.EmpresaRepository;
import com.SOOFT.ChallengeBackendSOOFT.infrastructure.outputAdapter.persistence.entity.EmpresaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface DevEmpresaRepositoryImpl extends EmpresaRepository, JpaRepository<EmpresaEntity,String> {
    @Override
    default Empresa save(Empresa empresa){
        EmpresaEntity entity = EmpresaEntity.fromDomain(empresa); //Se convierte a la Entidad de persistencia
        EmpresaEntity guardada = this.save(entity); //Se guarda
        return guardada.toDomain();
    }

    @Override
    default Optional<Empresa> findByCuit(String cuit){
        return this.findById(cuit) //Se usa el findById de JpaRepository, ya que el ID de EmpresaEntity es el cuit
                .map(EmpresaEntity::toDomain); //Se convierte a la entidad de dominio
    }

    @Override
    default Page<Empresa> findByFechaAdhesion(LocalDate startDate, LocalDate endDate, Pageable pageable){
        Page<EmpresaEntity> entities = this.findAllByFechaAdhesionBetween(startDate, endDate, pageable); //Se busca, utilizando un método custom de JpaRepository
        return entities.map(EmpresaEntity::toDomain); //Se convierte a Page<Empresa>
    }

    Page<EmpresaEntity> findAllByFechaAdhesionBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
