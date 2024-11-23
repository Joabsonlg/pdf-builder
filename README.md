# PDF Builder

A high-level Java library for PDF generation that abstracts away the complexity of Apache PDFBox, providing an intuitive and developer-friendly API.

[![Build Status](https://github.com/joabsonlg/pdf-builder/actions/workflows/build.yml/badge.svg)](https://github.com/joabsonlg/pdf-builder/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java Version](https://img.shields.io/badge/java-17%2B-blue)](https://adoptium.net/)

## Features

- **Simple API**: Create professional PDFs with minimal code
- **Rich Components**: Headers, paragraphs, tables, lists, images, and more
- **Smart Layout**: Automatic page breaks and content positioning
- **Highly Configurable**: Customize every aspect of your PDF
- **Type-Safe**: Builder pattern with strong typing for error-free development
- **Well-Tested**: Extensive test coverage ensuring reliability
- **Well-Documented**: Comprehensive documentation with examples

## Requirements

- Java 17 or higher
- Maven 3.x

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.joabsonlg</groupId>
    <artifactId>pdf-builder</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Quick Start

```java
PDFBuilder builder = new PDFBuilder();

// Add a heading
builder.addHeading(Heading.builder()
    .withText("My Document")
    .withLevel(HeadingLevel.H1)
    .build());

// Add a paragraph
builder.addParagraph(Paragraph.builder()
    .addText("Hello, World!")
    .withAlignment(TextAlignment.CENTER)
    .build());

// Save the document
builder.save("output.pdf");
```

## Core Components

### Document Configuration
```java
PDFConfiguration config = PDFConfiguration.create()
    .withPageSize(PDRectangle.A4)
    .withMargins(50f, 40f, 30f, 40f)
    .withDPI(300)
    .build();

PDFBuilder builder = new PDFBuilder(config);
```

### Headers and Footers
```java
// Add company logo
builder.setLogo(Logo.builder()
    .withText("Company Name")
    .withLeftImage("path/to/logo.png")
    .withStyle(LogoStyle.DEFAULT)
    .build());

// Add page numbering
builder.setPageNumbering(PageNumbering.builder()
    .withFormat("Page {current} of {total}")
    .withPosition(Position.BOTTOM_RIGHT)
    .build());
```

### Tables
```java
Table table = Table.builder()
    .withHeaders("Name", "Age", "City")
    .addRow("John Doe", "30", "New York")
    .addRow("Jane Smith", "25", "London")
    .withStyle(TableStyle.builder()
        .withBorderWidth(1f)
        .withHeaderBackground(Color.LIGHT_GRAY)
        .build())
    .build();

builder.addTable(table);
```

### Lists
```java
List list = List.builder()
    .withOrdered(true)
    .addItem("First item")
    .addItem("Second item with nested list",
        List.builder()
            .withOrdered(false)
            .addItem("Nested item 1")
            .addItem("Nested item 2")
            .build())
    .build();

builder.addList(list);
```

## Project Structure

```
pdf-builder/
├── src/
│   ├── main/java/io/github/joabsonlg/pdfbuilder/
│   │   ├── components/              # PDF Components
│   │   │   ├── image/              # Image handling
│   │   │   ├── list/               # List components
│   │   │   ├── logo/               # Logo components
│   │   │   ├── page/               # Page components
│   │   │   ├── table/              # Table components
│   │   │   └── text/               # Text components
│   │   ├── core/                   # Core classes
│   │   ├── examples/               # Usage examples
│   │   ├── exceptions/             # Custom exceptions
│   │   └── utils/                  # Utility classes
│   └── test/                       # Unit tests
└── pom.xml                         # Maven configuration
```

## Quality Assurance

We maintain high code quality standards through:

- **Checkstyle**: Enforces Google Java Style Guide
- **SpotBugs**: Static analysis for potential bugs
- **JaCoCo**: Ensures 80%+ code coverage
- **JUnit**: Comprehensive unit testing

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Dependencies

- Apache PDFBox 3.0.1
- Commons IO 2.15.1
- SLF4J 2.0.11
- JUnit Jupiter 5.10.1 (test)
