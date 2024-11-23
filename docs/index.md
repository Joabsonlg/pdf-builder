---
layout: home
title: Home
nav_order: 1
description: "PDF Builder is a high-level Java library that simplifies PDF generation by providing an intuitive API on top of Apache PDFBox."
permalink: /
---

# PDF Builder Documentation
{: .fs-9 }

A high-level Java library that simplifies PDF generation by providing an intuitive and type-safe API.
{: .fs-6 .fw-300 }

[Get started now](#getting-started){: .btn .btn-primary .fs-5 .mb-4 .mb-md-0 .mr-2 }
[View it on GitHub][GitHub Repository]{: .btn .fs-5 .mb-4 .mb-md-0 }

---

## Overview

PDF Builder is designed to make PDF generation in Java as simple as possible while maintaining flexibility and power. It provides a high-level abstraction over Apache PDFBox, handling all the complex details of PDF generation for you.

### Key Features

- **🎯 Type-Safe Builder API**
  - Fluent interface for all components
  - Compile-time error detection
  - IDE-friendly with great code completion

- **📄 Rich Component Library**
  - Headers and footers
  - Tables with advanced styling
  - Lists (ordered and unordered)
  - Images with automatic scaling
  - Text with rich formatting
  - Page numbers and watermarks

- **🎨 Advanced Styling**
  - Custom fonts and colors
  - Flexible layouts
  - Borders and backgrounds
  - Automatic page breaks
  - Header/footer templates

- **🔧 Highly Configurable**
  - Page sizes and margins
  - DPI settings
  - Compression options
  - Safe areas
  - Custom metadata

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.x

### Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.joabsonlg</groupId>
    <artifactId>pdf-builder</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Quick Example

```java
PDFBuilder builder = new PDFBuilder();

// Configure document
PDFConfiguration config = PDFConfiguration.create()
    .withPageSize(PDRectangle.A4)
    .withMargins(50f, 40f, 50f, 40f)
    .withDPI(300)
    .build();

// Create document with configuration
PDFBuilder builder = new PDFBuilder(config);

// Add header
builder.setLogo(Logo.builder()
    .withText("Company Name")
    .withLeftImage("logo.png")
    .build());

// Add content
builder.addHeading(Heading.builder()
    .withText("Welcome to PDF Builder")
    .withLevel(HeadingLevel.H1)
    .build());

builder.addParagraph(Paragraph.builder()
    .addText("This is a simple example showing how easy it is to create PDFs.")
    .withAlignment(TextAlignment.JUSTIFIED)
    .build());

// Add table
Table table = Table.builder()
    .withHeaders("Name", "Age", "City")
    .addRow("John Doe", "30", "New York")
    .addRow("Jane Smith", "25", "London")
    .withStyle(TableStyle.DEFAULT)
    .build();

builder.addTable(table);

// Save the document
builder.save("welcome.pdf");
```

## Next Steps

- [Installation Guide](getting-started/installation) - Detailed installation instructions
- [Basic Concepts](getting-started/basic-concepts) - Learn the core concepts
- [Component Guide](components) - Explore available components
- [Examples](examples) - See more examples
- [API Reference](api-reference) - Complete API documentation

## About the Project

PDF Builder is an open-source project licensed under MIT. It's actively maintained and welcomes contributions from the community.

### Contributing

We welcome contributions! See our [Contributing Guide](contributing) for details.

### License

This project is licensed under the MIT License. See the [LICENSE](license) file for details.

[GitHub Repository]: https://github.com/joabsonlg/pdf-builder
