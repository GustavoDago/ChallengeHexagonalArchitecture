package com.SOOFT.ChallengeBackendSOOFT.infrastructure.outputAdapter.persistence.repository;

import com.SOOFT.ChallengeBackendSOOFT.domain.model.Transferencia;
import com.SOOFT.ChallengeBackendSOOFT.domain.ports.out.TransferenciaRepository;
import com.SOOFT.ChallengeBackendSOOFT.infrastructure.outputAdapter.persistence.entity.TransferenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface TransferenciaRepositoryImpl extends TransferenciaRepository, JpaRepository<TransferenciaEntity, Long> {

    @Override
    default List<Transferencia> findByFechaBetweenAndIdEmpresa(LocalDate startDate, LocalDate endDate, String cuitEmpresa){
        List<TransferenciaEntity> entities;
        if (cuitEmpresa == null){
            entities = this.findAllByFechaBetween(startDate,endDate);
        }else {
            entities = this.findAllByFechaBetweenAndIdEmpresa(startDate,endDate,cuitEmpresa);
        }
        return entities.stream()
                .map(TransferenciaEntity::toDomain)
                .toList();
    }

    @Override
    default Transferencia save(Transferencia transferencia){
        TransferenciaEntity entity = TransferenciaEntity.fromDomain(transferencia);
        TransferenciaEntity savedEntity = this.save(entity);
        return savedEntity.toDomain();
    }

    List<TransferenciaEntity> findAllByFechaBetween(LocalDate startDate, LocalDate endDate);

    List<TransferenciaEntity> findAllByFechaBetweenAndIdEmpresa(LocalDate startDate, LocalDate endDate, String idEmpresa);
}
