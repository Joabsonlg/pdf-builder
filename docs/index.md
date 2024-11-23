---
layout: default
title: Home
nav_order: 1
---

# PDF Builder

A high-level Java library for PDF generation that abstracts away the complexity of Apache PDFBox.

## Features

- 📄 **Simple API**: Create professional PDFs with minimal code
- 🎨 **Rich Components**: Headers, paragraphs, tables, lists, images, and more
- 📐 **Smart Layout**: Automatic page breaks and content positioning
- 🔧 **Highly Configurable**: Customize every aspect of your PDF
- 🎯 **Type-Safe**: Builder pattern with strong typing for error-free development

## Quick Start

### Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.joabsonlg</groupId>
    <artifactId>pdf-builder</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Basic Usage

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

## Next Steps

- Check out the [Getting Started](getting-started) guide
- Explore available [Components](components)
- See more [Examples](examples)
- Browse the [API Reference](api)
