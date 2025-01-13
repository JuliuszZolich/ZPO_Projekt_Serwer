package org.example.Database.Services;

import org.example.Database.Objects.Grupa;
import org.example.Database.Repositories.GrupaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GrupyService {

    @Autowired
    private GrupaRepository repo;

    public List<Grupa> listAll() {
        return repo.findAll();
    }
}