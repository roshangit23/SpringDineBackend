package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.model.Company;

public interface CompanyAssociatedEntity {

    /**
     * Returns the associated company of the entity.
     *
     * @return the associated company
     */
    Company getCompany();

}
