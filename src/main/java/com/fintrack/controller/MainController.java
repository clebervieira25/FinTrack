
package com.fintrack.controller;

import com.fintrack.model.Transacao;
import com.fintrack.model.TipoTransacao;
import com.fintrack.service.FinancaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class MainController {

    @FXML private TableView<Transacao> tabelaTransacoes;
    @FXML private TableColumn<Transacao, String> colData;
    @FXML private TableColumn<Transacao, String> colDescricao;
    @FXML private TableColumn<Transacao, Double> colValor;
    @FXML private TableColumn<Transacao, String> colTipo;
    @FXML private Label lblSaldo;
    @FXML private Label lblTotalReceitas;
    @FXML private Label lblTotalDespesas;

    private FinancaService service;
    private ObservableList<Transacao> transacoesList;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        service = new FinancaService();
        transacoesList = FXCollections.observableArrayList();

        // Configurar colunas
        colData.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getData().format(dateFormatter)
                )
        );
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colTipo.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getTipo().getDescricao()
                )
        );

        // Formatar coluna valor
        colValor.setCellFactory(column -> new TableCell<Transacao, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    Transacao t = getTableView().getItems().get(getIndex());
                    String cor = t.getTipo() == TipoTransacao.RECEITA ? "green" : "red";
                    setText(String.format("R$ %.2f", item));
                    setStyle("-fx-text-fill: " + cor + ";");
                }
            }
        });

        carregarDados();
    }

    private void carregarDados() {
        try {
            transacoesList.setAll(service.listarTransacoes());
            tabelaTransacoes.setItems(transacoesList);
            atualizarResumo();
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar dados", e.getMessage());
        }
    }

    private void atualizarResumo() throws SQLException {
        lblSaldo.setText(String.format("R$ %.2f", service.getSaldo()));
        lblTotalReceitas.setText(String.format("R$ %.2f", service.getTotalReceitas()));
        lblTotalDespesas.setText(String.format("R$ %.2f", service.getTotalDespesas()));

        // Cor do saldo
        double saldo = service.getSaldo();
        if (saldo >= 0) {
            lblSaldo.setStyle("-fx-text-fill: green; -fx-font-size: 18px;");
        } else {
            lblSaldo.setStyle("-fx-text-fill: red; -fx-font-size: 18px;");
        }
    }

    @FXML
    private void abrirNovaTransacao() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/nova_transacao.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Nova Transação");
            stage.setScene(scene);
            stage.showAndWait();
            carregarDados();
        } catch (Exception e) {
            mostrarErro("Erro", "Não foi possível abrir a janela");
        }
    }

    @FXML
    private void removerTransacao() {
        Transacao selecionada = tabelaTransacoes.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAviso("Selecione uma transação", "Por favor, selecione uma transação para remover");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Remoção");
        alert.setHeaderText("Remover transação");
        alert.setContentText("Tem certeza que deseja remover: " + selecionada.getDescricao() + "?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                service.removerTransacao(selecionada.getId());
                carregarDados();
            } catch (SQLException e) {
                mostrarErro("Erro", "Não foi possível remover: " + e.getMessage());
            }
        }
    }

    @FXML
    private void abrirRelatorio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/relatorio.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Relatório Financeiro");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            mostrarErro("Erro", "Não foi possível abrir o relatório");
        }
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAviso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}