package org.example.Database.Services;

import org.example.Database.Objects.Obecnosc;
import org.example.Database.Repositories.ObecnoscRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ObecnoscService {

    @Autowired
    private ObecnoscRepository repo;

    public List<Obecnosc> listAll() {
        return repo.findAll();
    }
}