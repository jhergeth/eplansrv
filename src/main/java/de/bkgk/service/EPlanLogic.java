package de.bkgk.service;

import de.bkgk.domain.EPlan;
import de.bkgk.dto.EPlanSummen;
import de.bkgk.responses.ListResponse;
import de.bkgk.util.Link;
import de.bkgk.util.Node;

import java.util.List;

public interface EPlanLogic {
    void delete(Long id);

    void duplicate(Long id);

    public List<EPlan> getEPlan(String bereich);
    public List<String> getBereiche();
    public List<EPlanSummen> getSummen();
}
