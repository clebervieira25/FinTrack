package com.fintrack.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RepositorioGenerico<T> {
    private List<T> itens = new ArrayList<>();

    public void adicionar(T item) {
        itens.add(item);
    }

    public boolean remover(int index) {
        if (index >= 0 && index < itens.size()) {
            itens.remove(index);
            return true;
        }
        return false;
    }

    public boolean remover(T item) {
        return itens.remove(item);
    }

    public List<T> listarTodos() {
        return new ArrayList<>(itens);
    }

    public List<T> buscar(Predicate<T> condicao) {
        List<T> resultado = new ArrayList<>();
        for (T item : itens) {
            if (condicao.test(item)) {
                resultado.add(item);
            }
        }
        return resultado;
    }

    public T obter(int index) {
        if (index >= 0 && index < itens.size()) {
            return itens.get(index);
        }
        return null;
    }

    public int contar() {
        return itens.size();
    }

    public void limpar() {
        itens.clear();
    }
}