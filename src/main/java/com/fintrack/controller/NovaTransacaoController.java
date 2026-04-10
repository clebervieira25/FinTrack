package com.fintrack.controller;

import com.fintrack.model.TipoTransacao;
import com.fintrack.service.FinancaService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;

public class NovaTransacaoController {

    @FXML private TextField txtDescricao;
    @FXML private TextField txtValor;
    @FXML private RadioButton rbReceita;
    @FXML private RadioButton rbDespesa;
    @FXML private ToggleGroup tgTipo;
    @FXML private DatePicker dtData;
    @FXML private Button btnSalvar;

    private FinancaService service;

    @FXML
    public void initialize() {
        service = new FinancaService();
        dtData.setValue(java.time.LocalDate.now());
        rbReceita.setSelected(true);
    }

    @FXML
    private void salvar() {
        String descricao = txtDescricao.getText().trim();
        if (descricao.isEmpty()) {
            mostrarErro("Descrição obrigatória", "Por favor, informe uma descrição");
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(txtValor.getText().trim());
            if (valor <= 0) {
                mostrarErro("Valor inválido", "O valor deve ser maior que zero");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarErro("Valor inválido", "Por favor, informe um valor numérico válido");
            return;
        }

        TipoTransacao tipo = rbReceita.isSelected() ? TipoTransacao.RECEITA : TipoTransacao.DESPESA;

        try {
            service.adicionarTransacao(descricao, valor, tipo);
            fechar();
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar", "Não foi possível salvar a transação: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            mostrarErro("Dados inválidos", e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        fechar();
    }

    private void fechar() {
        Stage stage = (Stage) btnSalvar.getScene().getWindow();
        stage.close();
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}