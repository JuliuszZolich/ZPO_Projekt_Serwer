package org.example.Database.Services;

import org.example.Database.Objects.Prowadzacy;
import org.example.Database.Repositories.ProwadzacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProwadzacyService {

    @Autowired
    private ProwadzacyRepository repo;

    public List<Prowadzacy> listAll() {
        return repo.findAll();
    }
}