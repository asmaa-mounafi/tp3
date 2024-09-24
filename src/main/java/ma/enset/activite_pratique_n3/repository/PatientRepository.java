package ma.enset.activite_pratique_n3.repository;

import ma.enset.activite_pratique_n3.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface  PatientRepository extends JpaRepository<Patient,Long>{
    //1ere facon (respect convention)
    //si une methode qui retourne une page il est obligqtoire d'ajouter une parametre de type Pageable(pour faire pagination(size,page))
    Page<Patient> findByNomContains(String keyword, Pageable pageable);
    //2eme  facon
    @Query("select p from Patient p where p.nom like :x")
    Page<Patient> chercher(@Param("x") String keyword, Pageable pageable);
}
