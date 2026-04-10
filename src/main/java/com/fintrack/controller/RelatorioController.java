package com.fintrack.controller;

import com.fintrack.model.Transacao;
import com.fintrack.model.TipoTransacao;
import com.fintrack.service.FinancaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class RelatorioController {

    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Number> barChart;
    @FXML private Label lblPeriodo;

    private FinancaService service;

    @FXML
    public void initialize() {
        service = new FinancaService();
        carregarGraficos();
    }

    private void carregarGraficos() {
        try {
            double receitas = service.getTotalReceitas();
            double despesas = service.getTotalDespesas();

            pieChart.getData().clear();
            pieChart.getData().add(new PieChart.Data("Receitas", receitas));
            pieChart.getData().add(new PieChart.Data("Despesas", despesas));

            barChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Valores");

            var transacoes = service.listarTransacoes();
            Map<String, Double> porCategoria = transacoes.stream()
                    .limit(6)
                    .collect(Collectors.groupingBy(
                            t -> t.getDescricao().length() > 15 ?
                                    t.getDescricao().substring(0, 15) + "..." :
                                    t.getDescricao(),
                            Collectors.summingDouble(Transacao::getValor)
                    ));

            porCategoria.forEach((cat, valor) ->
                    series.getData().add(new XYChart.Data<>(cat, valor))
            );

            barChart.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void atualizar() {
        carregarGraficos();
    }
}