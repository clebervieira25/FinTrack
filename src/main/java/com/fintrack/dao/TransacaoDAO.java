package com.fintrack.dao;

import com.fintrack.model.Transacao;
import com.fintrack.model.TipoTransacao;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {

    public void inserir(Transacao transacao) throws SQLException {
        String sql = "INSERT INTO transacoes (data, descricao, valor, tipo) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, transacao.getData().toString());
            pstmt.setString(2, transacao.getDescricao());
            pstmt.setDouble(3, transacao.getValor());
            pstmt.setString(4, transacao.getTipo().name());
            pstmt.executeUpdate();
        }
    }

    public List<Transacao> listarTodas() throws SQLException {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT * FROM transacoes ORDER BY data DESC";

        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                transacoes.add(extrairTransacao(rs));
            }
        }
        return transacoes;
    }

    public Transacao buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM transacoes WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extrairTransacao(rs);
            }
        }
        return null;
    }

    public void atualizar(Transacao transacao) throws SQLException {
        String sql = "UPDATE transacoes SET data = ?, descricao = ?, valor = ?, tipo = ? WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, transacao.getData().toString());
            pstmt.setString(2, transacao.getDescricao());
            pstmt.setDouble(3, transacao.getValor());
            pstmt.setString(4, transacao.getTipo().name());
            pstmt.setInt(5, transacao.getId());
            pstmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM transacoes WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public double calcularSaldo() throws SQLException {
        String sql = """
            SELECT 
                SUM(CASE WHEN tipo = 'RECEITA' THEN valor ELSE 0 END) as totalReceitas,
                SUM(CASE WHEN tipo = 'DESPESA' THEN valor ELSE 0 END) as totalDespesas
            FROM transacoes
        """;

        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            double receitas = rs.getDouble("totalReceitas");
            double despesas = rs.getDouble("totalDespesas");
            return receitas - despesas;
        }
    }

    private Transacao extrairTransacao(ResultSet rs) throws SQLException {
        return new Transacao(
                rs.getInt("id"),
                LocalDate.parse(rs.getString("data")),
                rs.getString("descricao"),
                rs.getDouble("valor"),
                TipoTransacao.valueOf(rs.getString("tipo"))
        );
    }
}