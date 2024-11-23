# 🏗️ Arquitetura do PDF Builder

## 📋 Visão Geral

O PDF Builder é estruturado em torno de um conjunto central de classes que trabalham juntas para criar documentos PDF. Cada classe tem uma responsabilidade específica e se integra com as outras de forma coesa.

## 🔄 Fluxo de Execução

1. O usuário começa criando uma `PDFConfiguration`
2. Essa configuração é usada para instanciar um `PDFBuilder`
3. O `PDFBuilder` usa internamente:
   - `SafeArea` para gerenciar margens
   - `Coordinates` para posicionamento
   - `ResourceManager` para recursos

## 🧩 Componentes Principais

### PDFBuilder
- **Papel**: Ponto de entrada principal da biblioteca
- **Responsabilidades**:
  - Gerencia o documento PDF
  - Coordena operações de escrita
  - Controla o fluxo de conteúdo
- **Dependências**:
  - `PDFConfiguration`
  - `SafeArea`
  - `ResourceManager`
  - `Coordinates`

### PDFConfiguration
- **Papel**: Configuração imutável do documento
- **Responsabilidades**:
  - Define tamanho da página
  - Configura margens
  - Define DPI e compressão
  - Estabelece configurações de fonte
- **Dependências**: Nenhuma (classe imutável)

### SafeArea
- **Papel**: Gerenciamento de área segura de escrita
- **Responsabilidades**:
  - Calcula áreas úteis
  - Valida posições
  - Gerencia margens
- **Dependências**:
  - `PDFConfiguration` (apenas na criação)

### Coordinates
- **Papel**: Sistema de coordenadas
- **Responsabilidades**:
  - Rastreia posição atual
  - Calcula movimentações
  - Valida limites
- **Dependências**:
  - `SafeArea`

### ResourceManager
- **Papel**: Gerenciamento de recursos do PDF
- **Responsabilidades**:
  - Gerencia fontes
  - Controla streams
  - Libera recursos
- **Dependências**:
  - `PDFConfiguration`

## 🔗 Diagrama de Dependências

```
PDFBuilder
├─> PDFConfiguration
├─> SafeArea
│   └─> PDFConfiguration
├─> Coordinates
│   └─> SafeArea
└─> ResourceManager
    └─> PDFConfiguration
```

## 📊 Fluxo de Dados

1. **Configuração**:
   ```
   PDFConfiguration (imutável)
   └─> Usado por todas as outras classes
   ```

2. **Posicionamento**:
   ```
   PDFBuilder
   ├─> Coordinates (calcula posição)
   └─> SafeArea (valida posição)
   ```

3. **Renderização**:
   ```
   PDFBuilder
   ├─> ResourceManager (prepara recursos)
   └─> Apache PDFBox (renderiza)
   ```

## 🔒 Garantias e Invariantes

1. **PDFConfiguration**
   - Sempre imutável
   - Valores sempre validados na criação
   - Nunca null após construção

2. **SafeArea**
   - Margens sempre positivas
   - Área útil sempre menor que página
   - Coordenadas sempre validadas

3. **Coordinates**
   - Sempre dentro da SafeArea
   - Movimentos sempre validados
   - Posição atual sempre conhecida

4. **ResourceManager**
   - Recursos sempre liberados
   - Fontes sempre carregadas
   - Streams sempre fechados

## 🔄 Ciclo de Vida

1. **Inicialização**
   ```
   new PDFConfiguration()
   └─> new PDFBuilder(config)
       ├─> new SafeArea(config)
       ├─> new Coordinates(safeArea)
       └─> new ResourceManager(config)
   ```

2. **Operação**
   ```
   PDFBuilder.addText()
   ├─> Coordinates.getPosition()
   ├─> SafeArea.validate()
   └─> ResourceManager.getFont()
   ```

3. **Finalização**
   ```
   PDFBuilder.save()
   └─> ResourceManager.close()
   ```

## 📝 Convenções de Código

1. **Imutabilidade**
   - `PDFConfiguration` é completamente imutável
   - Outros objetos são mutáveis mas thread-safe

2. **Validação**
   - Todas as entradas são validadas
   - Falhas geram exceções descritivas
   - Estado inválido é impossível

3. **Recursos**
   - Todos os recursos são gerenciados
   - Cleanup automático no fechamento
   - Sem vazamentos de memória

## 🚀 Pontos de Extensão

1. **Componentes Futuros**
   - Sistema de tabelas
   - Suporte a imagens
   - Estilos avançados

2. **Melhorias Planejadas**
   - Cache de recursos
   - Otimização de renderização
   - Suporte a threads

## 🎯 Decisões de Design

1. **Por que Imutável?**
   - `PDFConfiguration` é imutável para thread-safety
   - Facilita cache e otimização
   - Previne bugs de estado

2. **Por que SafeArea?**
   - Abstrai complexidade de margens
   - Facilita validação de posições
   - Melhora usabilidade

3. **Por que ResourceManager?**
   - Centraliza gestão de recursos
   - Previne vazamentos
   - Facilita otimizações

## 📚 Exemplos de Uso

Ver `PDFBuilderDemo.java` para exemplos práticos de:
- Configuração básica
- Manipulação de texto
- Movimentação no documento
- Gestão de recursos

## Componentes de Texto

### SimpleText
- Componente básico para renderização de texto
- Suporta:
  * Fonte e tamanho
  * Cor
  * Quebra automática de linha
  * Espaçamento entre linhas
- Implementa builder pattern para configuração
- Respeita área segura do documento

### Paragraph
- Componente avançado para parágrafos com formatação rica
- Suporta todos os recursos do SimpleText mais:
  * Múltiplos estilos no mesmo parágrafo
    - Negrito (Bold)
    - Itálico (Oblique)
    - Sublinhado com espessura configurável
    - Cores personalizadas
  * Alinhamentos
    - Esquerda (LEFT)
    - Centro (CENTER)
    - Direita (RIGHT)
    - Justificado (JUSTIFIED)
  * Justificação com distribuição uniforme de espaços
  * Quebra inteligente de palavras
- Classes auxiliares:
  * TextStyle: Define estilo de formatação
  * StyledText: Associa texto com estilo
  * TextAlignment: Enum de alinhamentos

### Estratégias de Implementação

#### Renderização de Texto
- Usa PDPageContentStream para desenho direto
- Calcula posições baseado em métricas da fonte
- Respeita margens e área segura
- Atualiza coordenadas após renderização

#### Alinhamento Justificado
- Calcula espaço extra disponível na linha
- Distribui uniformemente entre palavras
- Última linha não é justificada
- Mantém formatação individual das palavras

#### Quebra de Linha
- Respeita largura máxima disponível
- Preserva palavras inteiras
- Mantém estilos ao quebrar linha
- Calcula altura baseada no maior tamanho de fonte da linha

## Exemplos
Veja `examples/ParagraphDemo.java` para demonstração completa das funcionalidades de texto.
