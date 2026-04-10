# 💰 FinTrack - Controle de Finanças Pessoais

Sistema de controle financeiro pessoal desenvolvido em Java com interface gráfica JavaFX.

![Java](https://img.shields.io/badge/Java-17-blue)
![JavaFX](https://img.shields.io/badge/JavaFX-17-green)
![SQLite](https://img.shields.io/badge/SQLite-3-blue)
![Maven](https://img.shields.io/badge/Maven-3.9-red)

## 📋 Funcionalidades

- ✅ Cadastrar receitas e despesas
- ✅ Listar todas as transações
- ✅ Calcular saldo automaticamente
- ✅ Remover transações
- ✅ Relatórios com gráficos (Pizza e Barras)
- ✅ Banco de dados SQLite (persistência)

## 🛠️ Tecnologias utilizadas

| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| Java | 17 | Linguagem principal |
| JavaFX | 17 | Interface gráfica |
| SQLite | 3.42 | Banco de dados |
| Maven | 3.9 | Gerenciamento de dependências |
| JUnit | 5.9 | Testes unitários |

## 🚀 Como executar

### Pré-requisitos
- JDK 17 ou superior
- Maven 3.9 ou superior

### Passos

```bash
# 1. Clonar o repositório
git clone https://github.com/clebervieira25/FinTrack.git

# 2. Entrar na pasta do projeto
cd FinTrack

# 3. Compilar o projeto
mvn clean compile

# 4. Executar o programa
mvn javafx:run

