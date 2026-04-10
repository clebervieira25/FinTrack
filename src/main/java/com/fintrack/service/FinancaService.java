package com.fintrack.service;

import com.fintrack.dao.TransacaoDAO;
import com.fintrack.model.Transacao;
import com.fintrack.model.TipoTransacao;
import java.sql.SQLException;
import java.util.List;

public class FinancaService {
    private TransacaoDAO transacaoDAO;

    public FinancaService() {
        this.transacaoDAO = new TransacaoDAO();
    }

    public void adicionarTransacao(String descricao, double valor, TipoTransacao tipo) throws SQLException {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição não pode ser vazia");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }

        Transacao transacao = new Transacao(descricao, valor, tipo);
        transacaoDAO.inserir(transacao);
    }

    public List<Transacao> listarTransacoes() throws SQLException {
        return transacaoDAO.listarTodas();
    }

    public double getSaldo() throws SQLException {
        return transacaoDAO.calcularSaldo();
    }

    public void removerTransacao(int id) throws SQLException {
        if (transacaoDAO.buscarPorId(id) == null) {
            throw new IllegalArgumentException("Transação não encontrada");
        }
        transacaoDAO.deletar(id);
    }

    public double getTotalReceitas() throws SQLException {
        return transacaoDAO.listarTodas().stream()
                .filter(t -> t.getTipo() == TipoTransacao.RECEITA)
                .mapToDouble(Transacao::getValor)
                .sum();
    }

    public double getTotalDespesas() throws SQLException {
        return transacaoDAO.listarTodas().stream()
                .filter(t -> t.getTipo() == TipoTransacao.DESPESA)
                .mapToDouble(Transacao::getValor)
                .sum();
    }
}