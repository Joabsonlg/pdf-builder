# PDF Builder

Uma biblioteca Java de alto nível para construção de PDFs, abstraindo toda a complexidade do Apache PDFBox.

## Requisitos

- Java 17 ou superior
- Apache PDFBox 3.0.1
- Maven 3.x

## Qualidade de Código

O projeto utiliza várias ferramentas para garantir a qualidade do código:

### Checkstyle
- Garante consistência no estilo de código
- Verifica padrões de codificação Java
- Configurado com regras baseadas no Google Java Style
- Execute com: `mvn checkstyle:check`

### JaCoCo
- Cobertura mínima de código: 80%
- Monitora execução de testes
- Gera relatórios detalhados de cobertura
- Relatório disponível em: `target/site/jacoco/index.html`
- Execute com: `mvn test jacoco:report`

### SpotBugs
- Análise estática para encontrar bugs potenciais
- Verifica problemas de segurança
- Identifica más práticas de programação
- Execute com: `mvn spotbugs:check`

## Dependências Principais

- Apache PDFBox 3.0.1
- Commons IO 2.15.1
- SLF4J 2.0.11
- JUnit Jupiter 5.10.1 (para testes)

## Instalação

Para incluir o PDF Builder em seu projeto Maven, adicione a seguinte dependência ao seu `pom.xml`:

```xml
<dependency>
    <groupId>io.github.joabsonlg</groupId>
    <artifactId>pdf-builder</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Estrutura do Projeto

```
pdf-builder/
├── src/
│   ├── main/java/br/com/nutit/pdfbuilder/
│   │   ├── components/              
│   │   │   ├── image/              # Componentes de imagem
│   │   │   ├── list/               # Componentes de lista
│   │   │   ├── logo/               # Componentes de logo
│   │   │   ├── page/               # Componentes de página
│   │   │   ├── table/              # Componentes de tabela
│   │   │   └── text/               # Componentes de texto
│   │   ├── core/                   # Classes principais
│   │   │   ├── Coordinates.java    # Sistema de coordenadas
│   │   │   ├── PDFBuilder.java     # Classe principal
│   │   │   ├── PDFConfiguration.java # Configurações
│   │   │   ├── ResourceManager.java  # Gerenciamento de recursos
│   │   │   └── SafeArea.java        # Áreas seguras
│   │   ├── examples/               # Exemplos de uso
│   │   ├── exceptions/             # Exceções customizadas
│   │   └── utils/                  # Classes utilitárias
│   └── test/                       # Testes unitários
└── pom.xml                         # Configuração Maven
```

## Funcionalidades

### 1. Configuração do Documento
- Tamanho de página personalizado (A4, A3, etc.)
- Margens personalizadas
- DPI para imagens
- Qualidade de compressão
- Tamanho de fonte e espaçamento padrão
- Área segura com suporte a cabeçalho e rodapé

### 2. Sistema de Coordenadas
- Movimentação relativa (direita, abaixo)
- Movimentação absoluta (topo, início, fundo)
- Sistema de margens e áreas seguras
- Posicionamento percentual na página

### 3. Componentes de Texto
- Texto simples com formatação
- Parágrafos com alinhamento
- Títulos e subtítulos (H1-H6)
- Estilos de texto personalizados
- Fontes e cores customizáveis

### 4. Componentes de Imagem
- Suporte a diversos formatos (JPG, PNG)
- Redimensionamento automático
- Manutenção de proporção
- Alinhamento flexível
- Legendas opcionais

### 5. Componentes de Lista
- Listas ordenadas e não-ordenadas
- Estilos de marcadores personalizáveis
- Suporte a múltiplos níveis
- Formatação flexível

### 6. Componentes de Tabela
- Tabelas simples com cabeçalho
- Alinhamento de células
- Estilos de borda personalizáveis
- Cores alternadas para linhas

### 7. Cabeçalho e Rodapé
- Logo personalizado com imagens
- Numeração de páginas automática
- Rodapé confidencial
- Estilos predefinidos

## Exemplos de Uso

### Documento Básico
```java
// Configuração do documento
PDFConfiguration config = PDFConfiguration.create()
    .withPageSize(PDRectangle.A4)
    .withMargins(50f, 40f, 30f, 40f)
    .withDPI(300)
    .build();

PDFBuilder builder = new PDFBuilder(config);

// Adiciona título
builder.addHeading(Heading.builder()
    .withText("Meu Documento")
    .withLevel(HeadingLevel.H1)
    .build());

// Adiciona parágrafo
builder.addParagraph(Paragraph.builder()
    .addStyledText("Este é um exemplo de parágrafo com ", TextStyle.DEFAULT)
    .addStyledText("texto em negrito", TextStyle.builder().withBold(true).build())
    .build());

// Salva o documento
builder.save("exemplo.pdf");
```

### Logo e Numeração de Páginas
```java
// Configuração do logo
LogoStyle logoStyle = LogoStyle.builder()
    .withFontSize(16f)
    .withColor(Color.BLACK)
    .withImageWidth(50f)
    .withImageHeight(30f)
    .withMaintainAspectRatio(true)
    .build();

// Configuração da numeração de páginas
PageNumbering pageNumbering = PageNumbering.builder()
    .withFormat(PageNumbering.Format.WITH_TOTAL)
    .withPosition(PageNumbering.Position.BOTTOM)
    .withAlignment(TextAlignment.RIGHT)
    .build();

// Adiciona logo e numeração
builder.setLogo("Minha Empresa", logoStyle, "logo-left.png", "logo-right.png");
builder.setPageNumbering(pageNumbering);
```

### Tabelas e Listas
```java
// Criação de tabela
Table table = Table.builder()
    .withHeaders("Nome", "Idade", "Cidade")
    .addRow("João", "25", "São Paulo")
    .addRow("Maria", "30", "Rio de Janeiro")
    .build();

// Criação de lista
List list = List.builder()
    .withOrdered(true)
    .addItem("Primeiro item")
    .addItem("Segundo item")
    .addItem("Terceiro item")
    .build();

// Adiciona ao documento
builder.addTable(table);
builder.addList(list);
```

## Development

For development guidelines, please see [CONTRIBUTING.md](CONTRIBUTING.md).

## Testes

Para executar os testes:

```bash
mvn clean test
```

## Próximos Passos

- Tabelas complexas com merge de células
- Marcas d'água
- Links e âncoras
- Índice automático
- QR Codes e códigos de barras
- Templates pré-definidos

## Licença

Este projeto está sob a licença [MIT](LICENSE).

## Contribuindo

Contribuições são bem-vindas! Por favor, leia o guia de contribuição para saber como contribuir com o projeto.
