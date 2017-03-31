package org.egov.pgr.read.persistence.repository;

import org.egov.pgr.common.entity.ComplaintTypeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintTypeCategoryRepository extends JpaRepository<ComplaintTypeCategory, Long> {

    ComplaintTypeCategory findByName(String name);

}
