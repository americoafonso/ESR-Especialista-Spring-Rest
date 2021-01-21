package com.algaworks.algafood.infrastructure.repository;

import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

public class CidadeRepositoryImpl implements CidadeRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Cidade> listar() {
        return manager.createQuery("from Cidade", Cidade.class).getResultList();
    }

    @Override
    public Cidade buscar(Long id) {
        return manager.find(Cidade.class, 1);
    }

    @Transactional
    @Override
    public Cidade salvar(Cidade cidade) {
        return manager.merge(cidade);
    }

    @Transactional
    @Override
    public void remover(Cidade cidade) {
        cidade = buscar(cidade.getId());
        manager.remove(cidade);
    }
}
