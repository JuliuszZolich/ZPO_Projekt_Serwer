package org.example.Database.Services;

import org.example.Database.Objects.Termin;
import org.example.Database.Repositories.TerminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TerminService {

    @Autowired
    private TerminRepository repo;

    public List<Termin> listAll() {
        return repo.findAll();
    }
}